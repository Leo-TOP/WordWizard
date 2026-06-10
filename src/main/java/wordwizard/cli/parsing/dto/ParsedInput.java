package wordwizard.cli.parsing.dto;

import java.util.List;
import java.util.Map;

public record ParsedInput(String commandName,
                          List<String> args,
                          Map<String, String> options) {}
