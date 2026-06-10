package wordwizard.cli.commands.servicerelated.withoptions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wordwizard.cli.commands.Command;
import wordwizard.cli.commands.dto.CliDtoMapper;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.cli.commands.validation.ParameterValidator;
import wordwizard.cli.output.formatters.SimilarWordsFormatter;
import wordwizard.service.CentralService;

@Component
@RequiredArgsConstructor
public class GetSimilarByDefinitionCommand implements Command {
    private final CentralService service;
    private final CliDtoMapper mapper;
    private final SimilarWordsFormatter formatter;

    @Override
    public String commandName() { return "find-by-def"; }

    @Override
    public void execute(CommandContext ctx) {
        ParameterValidator.requireMinArgs(ctx, 1);
        var request = mapper.toSimilarByDefinitionRequest(ctx);
        var response = service.findByDefinition(request);
        String output = formatter.format(response);
        System.out.println(output);
    }
}
