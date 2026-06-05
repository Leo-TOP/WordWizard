package wordwizard.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public final class FileUtil {
    private FileUtil(){}

    public static String readResource(String resourcePath) {
        try {
            ClassPathResource resource = new ClassPathResource(resourcePath);
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource: " + resourcePath, e);
        }
    }

    public static void writeBinaryToFile(Path filePath, byte[] data){
        try{
            Files.write(filePath, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}