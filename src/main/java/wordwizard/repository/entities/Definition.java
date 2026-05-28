package wordwizard.repository.entities;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record Definition(@NotNull
                         String definitionText,
                         List<String> Examples,
                         List<Integer> embeddingVector){}
