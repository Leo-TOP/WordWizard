package wordwizard.businesslogic.ai.responseprocessing.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public record BatchThemeResponse(
        @SerializedName("theme_assignments")
        List<ThemeAssignment> themeAssignments
) {}