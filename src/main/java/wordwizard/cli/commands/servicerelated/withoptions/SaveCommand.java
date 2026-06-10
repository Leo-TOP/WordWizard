package wordwizard.cli.commands.servicerelated.withoptions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wordwizard.cli.commands.Command;
import wordwizard.cli.commands.dto.CliDtoMapper;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.cli.commands.validation.ParameterValidator;
import wordwizard.cli.output.formatters.SaveResultFormatter;
import wordwizard.service.CentralService;

@Component
@RequiredArgsConstructor
public class SaveCommand implements Command {
    private final CentralService service;
    private final CliDtoMapper mapper;
    private final SaveResultFormatter formatter;

    @Override
    public String commandName() { return "add"; }

    @Override
    public void execute(CommandContext ctx) {
        ParameterValidator.requireMinArgs(ctx, 2);
        ParameterValidator.requirePairedArgs(ctx);
        System.out.println(formatter.format(service.addUserWords(mapper.toUserWordRequests(ctx))));
    }
}
