package wordwizard.cli.commands.servicerelated.withoptions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wordwizard.cli.commands.Command;
import wordwizard.cli.commands.dto.CliDtoMapper;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.cli.commands.validation.ParameterValidator;
import wordwizard.cli.output.dto.WordDisplayRequest;
import wordwizard.cli.output.formatters.WordDisplayFormatter;
import wordwizard.models.Word;
import wordwizard.service.CentralService;

@Component
@RequiredArgsConstructor
public class GetWordCommand implements Command {
    private final CentralService service;
    private final CliDtoMapper mapper;
    private final WordDisplayFormatter formatter;

    @Override
    public String commandName() { return "get-word"; }

    @Override
    public void execute(CommandContext ctx) {
        ParameterValidator.requireArgCount(ctx, 1);
        Word word = service.getWord(mapper.toWordRequest(ctx));
        boolean verbose = ctx.options().containsKey("verbose");
        System.out.println(formatter.format(new WordDisplayRequest(word, verbose)));
    }
}
