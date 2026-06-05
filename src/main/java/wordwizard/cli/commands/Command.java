package wordwizard.cli.commands;

import wordwizard.businesslogic.CentralService;

public abstract class Command {
    protected final CentralService mainGuy;

    protected Command(CentralService mainGuy) {
        this.mainGuy = mainGuy;
    }

    public abstract void execute();
}
