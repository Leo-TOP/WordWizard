package wordwizard.businesslogic.ai.responseprocessing.dto;

import com.google.gson.annotations.SerializedName;

public record SingleThemeResponse(String theme,
                                  @SerializedName("is_new")
                                  boolean isNew) {
}
