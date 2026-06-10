package wordwizard.cli.commands.dto;

import java.util.List;
import java.util.Map;

public record CommandContext(List<String> positionalArgs,
                             Map<String, String> options) {}
