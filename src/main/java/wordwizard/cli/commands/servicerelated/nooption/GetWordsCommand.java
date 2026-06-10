package wordwizard.cli.commands.servicerelated.nooption;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wordwizard.cli.commands.Command;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.cli.commands.validation.ParameterValidator;
import wordwizard.cli.output.formatters.WordsDisplayFormatter;
import wordwizard.models.Word;
import wordwizard.service.CentralService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetWordsCommand implements Command {
    private final CentralService service;
    private final WordsDisplayFormatter formatter;

    @Override
    public String commandName() { return "get-words"; }

    @Override
    public void execute(CommandContext ctx) {
        ParameterValidator.requireMinArgs(ctx, 1);
        List<Word> words = service.getWords(ctx.positionalArgs());
        String output =  formatter.format(words);
        System.out.println(output);
    }
}
