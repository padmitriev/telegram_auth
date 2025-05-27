package dpa.testwork.tlgrm;

import dpa.testwork.tlgrm.config.BotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(BotConfig.class)
public class TlgrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(TlgrmApplication.class, args);
    }
}