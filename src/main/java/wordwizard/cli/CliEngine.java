package wordwizard.cli;

import org.springframework.stereotype.Controller;
import wordwizard.util.FileUtil;
import wordwizard.util.ResourceFileUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class CliEngine {
    private static final String INTRO;
    private static final String INTRO_PATH = "src/main/resources/appIntro.txt";

    static {
        INTRO = ResourceFileUtil.readResource(INTRO_PATH);
    }

    public void run(){
        while (true) {
        }
    }

    private static void printIntro(){
        System.out.println(INTRO);
    }
}
