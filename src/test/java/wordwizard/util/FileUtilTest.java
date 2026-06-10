package wordwizard.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wordwizard.exceptions.FileException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

    @TempDir
    Path tempDir;

    @Test
    void readsExistingClasspathResource() {
        String content = FileUtil.readResource("clifiles/help.txt");
        assertFalse(content.isBlank());
    }

    @Test
    void missingResourceThrowsFileExceptionWithPath() {
        FileException e = assertThrows(FileException.class,
                () -> FileUtil.readResource("nope/missing.txt"));
        assertTrue(e.getMessage().contains("nope/missing.txt"));
    }

    @Test
    void writesBinaryContentToFile() throws Exception {
        Path target = tempDir.resolve("out.bin");
        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);

        FileUtil.writeBinaryToFile(target, data);

        assertArrayEquals(data, Files.readAllBytes(target));
    }

    @Test
    void writeFailureThrowsFileExceptionWithPath() {
        Path impossible = tempDir.resolve("no-such-dir").resolve("out.bin");

        FileException e = assertThrows(FileException.class,
                () -> FileUtil.writeBinaryToFile(impossible, new byte[]{1}));
        assertTrue(e.getMessage().contains("out.bin"));
    }
}
