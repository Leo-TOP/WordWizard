package wordwizard.cli.commands;

import wordwizard.cli.commands.dto.CommandContext;

public interface Command {
    String commandName();

    void execute(CommandContext commandContext);
}
