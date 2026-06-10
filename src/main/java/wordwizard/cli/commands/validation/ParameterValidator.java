package wordwizard.cli.commands.validation;

import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.exceptions.CommandValidationException;

public final class ParameterValidator {
    private ParameterValidator() {}

    public static void requireArgCount(CommandContext ctx, int exact) {
        int actual = ctx.positionalArgs().size();
        if (actual != exact) throw new CommandValidationException(
                "Expected " + exact + " argument(s), got " + actual);
    }

    public static void requireMinArgs(CommandContext ctx, int min) {
        int actual = ctx.positionalArgs().size();
        if (actual < min) throw new CommandValidationException(
                "Expected at least " + min + " argument(s), got " + actual);
    }

    public static void requirePairedArgs(CommandContext ctx) {
        if (ctx.positionalArgs().size() % 2 != 0)
            throw new CommandValidationException("Arguments must be word-definition pairs (even count).");
    }

    public static Integer parseAndValidateLimit(String limitStr) {
        if (limitStr == null) return null;
        try {
            int n = Integer.parseInt(limitStr);
            if (n < 1) throw new CommandValidationException("Limit must be a positive number.");
            return n;
        } catch (NumberFormatException e) {
            throw new CommandValidationException("Limit must be a valid integer.");
        }
    }

    public static void requireOption(CommandContext ctx, String option) {
        if (!ctx.options().containsKey(option)) {
            throw new CommandValidationException("Missing required option: --" + option);
        }
    }
}