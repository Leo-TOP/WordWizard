package wordwizard.service.export;

import lombok.RequiredArgsConstructor;
import wordwizard.exceptions.FileException;
import wordwizard.exceptions.InvalidFileExtensionException;
import wordwizard.exceptions.InvalidFormatException;
import wordwizard.exceptions.PermissionDeniedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@RequiredArgsConstructor
public class ExportFileProcessor {
    private static final Set<String> FORMATS = Set.of("docx", "excel", "text", "markdown");
    private static final Set<String> EXTENSIONS = Set.of(".xlsx", ".xls", ".xlsm", ".docx", ".txt", ".md");

    private final String path;
    private final String format;

    public void validateExtensionAndFormat(){
        ExportFormat exportFormat = ExportFormat.fromString(format);
        boolean valid = exportFormat.supportsExtension(exportFormat);

        if (!valid) throw new InvalidFileExtensionException(
                "Unsupported extension. Supported for your format: " + exportFormat.getValidExtensions());
    }


    public Path process() {
        Path path = Path.of(this.path);
        ensureParentDirectory(path);
        return path;
    }

    private String getExtension(String ext)
    private void ensureParentDirectory(Path filePath) {
        Path parent = filePath.getParent();
        if (parent == null) {
            return;
        }

        if (!Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new FileException("Cannot create directory: " + parent + " - " + e.getMessage());
            }
        }

        if (!Files.isWritable(parent)) {
            throw new PermissionDeniedException("No write permission for directory: " + parent);
        }
    }
}
