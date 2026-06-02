package wordwizard.cli.commands;

import wordwizard.businesslogic.WordWizardService;

public abstract class Command {
    protected final WordWizardService mainGuy;

    protected Command(WordWizardService mainGuy) {
        this.mainGuy = mainGuy;
    }

    public abstract void execute();
}
