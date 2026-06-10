package wordwizard.cli.commands;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import wordwizard.cli.commands.dto.CliDtoMapper;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.cli.commands.servicerelated.nooption.GetAllThemesCommand;
import wordwizard.cli.commands.servicerelated.nooption.GetStatisticsCommand;
import wordwizard.cli.commands.servicerelated.nooption.GetWordsCommand;
import wordwizard.cli.commands.servicerelated.withoptions.ExportCommand;
import wordwizard.cli.commands.servicerelated.withoptions.FilterCommand;
import wordwizard.cli.commands.servicerelated.withoptions.GetSimilarByDefinitionCommand;
import wordwizard.cli.commands.servicerelated.withoptions.GetSimilarByWordCommand;
import wordwizard.cli.commands.servicerelated.withoptions.GetWordCommand;
import wordwizard.cli.commands.servicerelated.withoptions.SaveCommand;
import wordwizard.cli.commands.system.HelpCommand;
import wordwizard.cli.output.formatters.*;
import wordwizard.exceptions.CommandValidationException;
import wordwizard.models.Definition;
import wordwizard.models.SimilarWord;
import wordwizard.models.Theme;
import wordwizard.models.VocabularyStats;
import wordwizard.models.Word;
import wordwizard.service.CentralService;
import wordwizard.service.save.message.SaveResult;
import wordwizard.service.save.message.SaveStatus;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class CommandsTest {

    private final CentralService service = Mockito.mock(CentralService.class);
    private final CliDtoMapper mapper = new CliDtoMapper();

    private static CommandContext ctx(List<String> args, Map<String, String> options) {
        return new CommandContext(args, options);
    }

    private static Word sampleWord() {
        return Word.createWordWithoutId("cat",
                List.of(Definition.createWithoutId("a small animal", "noun", "dict", null)));
    }

    @Test
    void getWordCommandFetchesAndPrints() {
        when(service.getWord(any())).thenReturn(sampleWord());
        GetWordCommand command = new GetWordCommand(service, mapper, new WordDisplayFormatter());

        command.execute(ctx(List.of("cat"), Map.of("verbose", "true")));

        verify(service).getWord(any());
        assertEquals("get-word", command.commandName());
    }

    @Test
    void getWordCommandRequiresExactlyOneArg() {
        GetWordCommand command = new GetWordCommand(service, mapper, new WordDisplayFormatter());
        assertThrows(CommandValidationException.class, () -> command.execute(ctx(List.of(), Map.of())));
    }

    @Test
    void getWordsCommandPassesAllArgs() {
        when(service.getWords(anyList())).thenReturn(List.of(sampleWord()));
        GetWordsCommand command = new GetWordsCommand(service, new WordsDisplayFormatter());

        command.execute(ctx(List.of("cat", "dog"), Map.of()));

        verify(service).getWords(List.of("cat", "dog"));
        assertEquals("get-words", command.commandName());
    }

    @Test
    void saveCommandRequiresPairsAndDelegates() {
        when(service.addUserWords(anyList())).thenReturn(
                List.of(new SaveResult("cat", SaveStatus.WORD_SAVED, "ok")));
        SaveCommand command = new SaveCommand(service, mapper, new SaveResultFormatter());

        command.execute(ctx(List.of("cat", "a small animal"), Map.of()));
        verify(service).addUserWords(anyList());

        assertThrows(CommandValidationException.class,
                () -> command.execute(ctx(List.of("cat"), Map.of())));
        assertEquals("add", command.commandName());
    }

    @Test
    void exportCommandRequiresOutputOption() {
        ExportCommand command = new ExportCommand(service, mapper, new ExportFormatter());

        command.execute(ctx(List.of(), Map.of("output", "out.md")));
        verify(service).exportWords(any());

        assertThrows(CommandValidationException.class,
                () -> command.execute(ctx(List.of(), Map.of())));
        assertEquals("export", command.commandName());
    }

    @Test
    void filterCommandDelegatesWithOptions() {
        when(service.filter(any())).thenReturn(Map.of("animals", List.of(sampleWord())));
        FilterCommand command = new FilterCommand(service, mapper,
                new ThemeGroupedWordsFormatter(new WordsDisplayFormatter()));

        command.execute(ctx(List.of(), Map.of("theme", "animals")));

        verify(service).filter(any());
        assertEquals("filter", command.commandName());
    }

    @Test
    void similarCommandsDelegate() {
        when(service.findSimilar(any())).thenReturn(
                List.of(new SimilarWord("dog", "a loyal animal", 0.3)));
        when(service.findByDefinition(any())).thenReturn(
                List.of(new SimilarWord("dog", "a loyal animal", 0.3)));

        GetSimilarByWordCommand byWord =
                new GetSimilarByWordCommand(service, mapper, new SimilarWordsFormatter());
        byWord.execute(ctx(List.of("cat"), Map.of()));
        verify(service).findSimilar(any());
        assertEquals("similar", byWord.commandName());

        GetSimilarByDefinitionCommand byDef =
                new GetSimilarByDefinitionCommand(service, mapper, new SimilarWordsFormatter());
        byDef.execute(ctx(List.of("small", "animal"), Map.of()));
        verify(service).findByDefinition(any());
        assertEquals("find-by-def", byDef.commandName());
    }

    @Test
    void statsAndThemesCommandsDelegate() {
        when(service.getStatistics()).thenReturn(
                new VocabularyStats(1, 1, 1.0, Map.of(), Map.of(), 0, 1, 1));
        when(service.getAllThemes()).thenReturn(List.of(new Theme(1L, "animals", 1)));

        GetStatisticsCommand stats = new GetStatisticsCommand(service, new StatisticsFormatter());
        stats.execute(ctx(List.of(), Map.of()));
        verify(service).getStatistics();
        assertEquals("stats", stats.commandName());

        GetAllThemesCommand themes = new GetAllThemesCommand(service, new ThemesListFormatter());
        themes.execute(ctx(List.of(), Map.of()));
        verify(service).getAllThemes();
        assertEquals("themes", themes.commandName());
    }

    @Test
    void helpCommandPrintsWithoutService() {
        HelpCommand command = new HelpCommand();
        assertDoesNotThrow(() -> command.execute(ctx(List.of(), Map.of())));
        assertEquals("help", command.commandName());
    }
}
