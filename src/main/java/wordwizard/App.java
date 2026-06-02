package wordwizard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import wordwizard.cli.CliEngine;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(App.class);
        CliEngine cliEngine = applicationContext.getBean(CliEngine.class);
        cliEngine.run();
    }
}
