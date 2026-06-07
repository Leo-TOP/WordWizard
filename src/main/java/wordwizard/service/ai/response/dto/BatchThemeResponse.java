package wordwizard.service.ai.response.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public record BatchThemeResponse(
        @SerializedName("theme_assignments")
        List<ThemeAssignment> themeAssignments
) {}