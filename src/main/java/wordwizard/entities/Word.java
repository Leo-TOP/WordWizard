package wordwizard.entities;


import org.jetbrains.annotations.NotNull;

public record Word(@NotNull String wordName, String definition){}
