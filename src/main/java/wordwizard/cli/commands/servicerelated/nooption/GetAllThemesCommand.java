package wordwizard.cli.commands.servicerelated.nooption;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wordwizard.cli.commands.Command;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.cli.output.formatters.ThemesListFormatter;
import wordwizard.service.CentralService;

@Component
@RequiredArgsConstructor
public class GetAllThemesCommand implements Command {
    private final CentralService service;
    private final ThemesListFormatter formatter;

    @Override
    public String commandName() { return "themes"; }

    @Override
    public void execute(CommandContext ctx) {
        String output = formatter.format(service.getAllThemes());
        System.out.println(output);
    }
}
