package wordwizard.cli.commands.servicerelated.nooption;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wordwizard.cli.commands.Command;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.cli.output.formatters.StatisticsFormatter;
import wordwizard.models.VocabularyStats;
import wordwizard.service.CentralService;

@Component
@RequiredArgsConstructor
public class GetStatisticsCommand implements Command {
    private final CentralService service;
    private final StatisticsFormatter formatter;

    @Override
    public String commandName() { return "stats"; }

    @Override
    public void execute(CommandContext ctx) {
        VocabularyStats stats = service.getStatistics();
        String output = formatter.format(stats);
        System.out.println(output);
    }
}
