package wordwizard.cli.commands.dto;

import org.springframework.stereotype.Component;
import wordwizard.cli.commands.validation.ParameterValidator;
import wordwizard.exceptions.CommandValidationException;
import wordwizard.service.dictrequesting.dto.FilterRequest;
import wordwizard.service.export.dto.ExportRequest;
import wordwizard.service.save.dto.UserWordRequest;
import wordwizard.service.similarword.dto.SimilarWordRequestForDefinition;
import wordwizard.service.similarword.dto.SimilarWordRequestForWord;
import wordwizard.service.wordsfetching.dto.WordRequest;

import java.util.ArrayList;
import java.util.List;

@Component
public class CliDtoMapper {

    private static String firstArg(CommandContext ctx) {
        if (ctx.positionalArgs().isEmpty())
            throw new CommandValidationException("Missing required argument");
        return ctx.positionalArgs().getFirst();
    }

    public WordRequest toWordRequest(CommandContext ctx) {
        return new WordRequest(
                firstArg(ctx),
                ParameterValidator.parseAndValidateLimit(ctx.options().get("limit")),
                ctx.options().get("pos")
        );
    }

    public FilterRequest toFilterRequest(CommandContext ctx) {
        return new FilterRequest(
                ctx.options().get("theme"),
                ctx.options().get("pos"),
                ctx.options().get("starts-with")
        );
    }

    public List<UserWordRequest> toUserWordRequests(CommandContext ctx) {
        List<String> args = ctx.positionalArgs();
        if (args.size() % 2 != 0) {
            throw new CommandValidationException(
                    "Arguments must be word–definition pairs, got an odd count: " + args.size());
        }

        List<UserWordRequest> list = new ArrayList<>();
        for (int i = 0; i < args.size(); i += 2) {
            list.add(new UserWordRequest(args.get(i), args.get(i + 1)));
        }

        return list;
    }


    public ExportRequest toExportRequest(CommandContext ctx) {
        FilterRequest filter = toFilterRequest(ctx);
        return new ExportRequest(
                filter,
                ctx.options().getOrDefault("format", "markdown"),
                ctx.options().get("output")
        );
    }

    public SimilarWordRequestForWord toSimilarByWordRequest(CommandContext ctx) {
        return new SimilarWordRequestForWord(
                firstArg(ctx),
                ParameterValidator.parseAndValidateLimit(ctx.options().get("limit")),
                ctx.options().get("pos")
        );
    }

    public SimilarWordRequestForDefinition toSimilarByDefinitionRequest(CommandContext ctx) {
        return new SimilarWordRequestForDefinition(
                String.join(" ", ctx.positionalArgs()),
                ParameterValidator.parseAndValidateLimit(ctx.options().get("limit"))
        );
    }
}