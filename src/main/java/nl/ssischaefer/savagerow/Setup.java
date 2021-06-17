package nl.ssischaefer.savagerow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class Setup {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Setup.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "9010"));
        app.run(args);
    }
}
