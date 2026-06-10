package wordwizard.cli;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import wordwizard.cli.commands.CommandRegistry;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.cli.parsing.InputParser;
import wordwizard.exceptions.exceptionhandling.GlobalExceptionHandler;
import wordwizard.util.FileUtil;

import java.util.Scanner;

@Slf4j
@Component
@RequiredArgsConstructor
public class CliEngine implements CommandLineRunner {
    private static final String INTRO;
    private static final String INTRO_PATH = "clifiles/appIntro.txt";
    static {
        INTRO = FileUtil.readResource(INTRO_PATH);
    }

    private final CommandRegistry registry;
    private final GlobalExceptionHandler exceptionHandler;
    private final InputParser parser;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        displayIntro();

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            try {
                var input = parser.parse(line);
                var cmd = registry.getCommand(input.commandName());
                cmd.execute(new CommandContext(input.args(), input.options()));

            } catch (Exception e) {
                String message = exceptionHandler.handle(e);
                printExceptionMessage(message);
            }
        }
    }

    private void printExceptionMessage(String message){
        System.out.println(message);
    }

    private static void displayIntro(){
        System.out.println(INTRO);
    }
}
