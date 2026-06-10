package wordwizard.cli.commands.system;

import org.springframework.stereotype.Component;
import wordwizard.cli.commands.Command;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.util.FileUtil;

@Component
public class HelpCommand implements Command {
    private static final String HELP_TEXT = FileUtil.readResource("clifiles/help.txt");

    @Override
    public String commandName() { return "help"; }

    @Override
    public void execute(CommandContext ctx) {
        System.out.println(HELP_TEXT);
    }
}
