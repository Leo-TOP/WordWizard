package wordwizard.dat.entities;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record Word(@NotNull String wordName, List<String> definition){}
