package wordwizard.cli;

import org.springframework.stereotype.Controller;
import wordwizard.util.FileUtil;

@Controller
public class CliEngine {
    private static final String INTRO;
    private static final String INTRO_PATH = "src/main/resources/appIntro.txt";

    static {
        INTRO = FileUtil.readResource(INTRO_PATH);
    }

    public void run(){
        while (true) {
        }
    }

    private static void printIntro(){
        System.out.println(INTRO);
    }
}
