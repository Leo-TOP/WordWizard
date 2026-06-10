package wordwizard.cli.commands.servicerelated.withoptions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wordwizard.cli.commands.Command;
import wordwizard.cli.commands.dto.CliDtoMapper;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.cli.commands.validation.ParameterValidator;
import wordwizard.cli.output.formatters.ExportFormatter;
import wordwizard.service.CentralService;

@Component
@RequiredArgsConstructor
public class ExportCommand implements Command {
    private final CentralService service;
    private final CliDtoMapper mapper;
    private final ExportFormatter formatter;

    @Override
    public String commandName() { return "export"; }

    @Override
    public void execute(CommandContext ctx) {
        ParameterValidator.requireOption(ctx, "output");
        service.exportWords(mapper.toExportRequest(ctx));
        System.out.println(formatter.format(ctx.options().get("output")));
    }
}
