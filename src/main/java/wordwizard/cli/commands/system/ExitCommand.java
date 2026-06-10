package wordwizard.cli.commands.system;

import org.springframework.stereotype.Component;
import wordwizard.cli.commands.Command;
import wordwizard.cli.commands.dto.CommandContext;

@Component
public class ExitCommand implements Command {

    @Override
    public String commandName() { return "exit"; }

    @Override
    public void execute(CommandContext ctx) {
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
