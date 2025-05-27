package dpa.testwork.tlgrm.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "telegram.bot")
@Validated
@Getter
@Setter
public class BotConfig {
    @NotBlank(message = "Bot token must be configured")
    private String token;

    @NotBlank(message = "Bot username must be configured")
    private String username;

    @NotBlank(message = "WebApp URL must be configured")
    private String webAppUrl;
}
