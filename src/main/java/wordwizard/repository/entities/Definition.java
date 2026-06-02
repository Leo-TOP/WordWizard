package wordwizard.repository.entities;

import jakarta.persistence.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity
public record Definition(@NotNull
                         String definitionText,
                         List<String> Examples,
                         List<Integer> embeddingVector){}
