package wordwizard.cli.commands.servicerelated.withoptions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wordwizard.cli.commands.Command;
import wordwizard.cli.commands.dto.CliDtoMapper;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.cli.output.formatters.ThemeGroupedWordsFormatter;
import wordwizard.models.Word;
import wordwizard.service.CentralService;
import wordwizard.service.dictrequesting.dto.FilterRequest;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FilterCommand implements Command {
    private final CentralService service;
    private final CliDtoMapper mapper;
    private final ThemeGroupedWordsFormatter formatter;

    @Override
    public String commandName() { return "filter"; }

    @Override
    public void execute(CommandContext ctx) {
        FilterRequest request = mapper.toFilterRequest(ctx);
        Map<String, List<Word>> groupedWords = service.filter(request);
        String output = formatter.format(groupedWords);
        System.out.println(output);
    }
}
