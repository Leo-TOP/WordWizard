package wordwizard.service.ai.response.dto;

import com.google.gson.annotations.SerializedName;

public record ThemeAssignment(String word,
                              @SerializedName("definition_index")
                              int definitionIndex, String theme) {}
