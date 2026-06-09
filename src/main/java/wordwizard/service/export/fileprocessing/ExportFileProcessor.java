package wordwizard.service.export.fileprocessing;

import wordwizard.exceptions.FileException;
import wordwizard.exceptions.InvalidFileExtensionException;
import wordwizard.exceptions.PermissionDeniedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExportFileProcessor {

    private final Path filePath;
    private final ExportFormat format;

    public ExportFileProcessor(String path, String format) {
        this.format = ExportFormat.fromString(format);
        this.filePath = Path.of(path);
    }

    public Path process() {
        validateExtension();
        ensureParentDirectory();
        return filePath;
    }

    private void validateExtension() {
        String filename = filePath.getFileName().toString();
        String ext = getExtension(filename);

        if (ext.isEmpty()) {
            throw new InvalidFileExtensionException(
                "File \"" + filename + "\" has no extension. " +
                "Expected one of: " + format.getValidExtensions());
        }

        if (!format.supportsExtension(ext)) {
            throw new InvalidFileExtensionException(
                "Extension \"" + ext + "\" is not valid for format \"" + format.name().toLowerCase() + "\". " +
                "Expected one of: " + format.getValidExtensions());
        }
    }


    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot == -1 ? "" : filename.substring(dot).toLowerCase();
    }

    private void ensureParentDirectory() {
        Path parent = filePath.getParent();
        if (parent == null) return;

        if (!Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new FileException("Cannot create directory: " + parent + " — " + e.getMessage());
            }
        }

        if (!Files.isWritable(parent)) {
            throw new PermissionDeniedException("No write permission for directory: " + parent);
        }
    }
}
