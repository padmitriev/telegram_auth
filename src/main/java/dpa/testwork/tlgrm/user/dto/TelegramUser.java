package dpa.testwork.tlgrm.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
public class TelegramUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDateTime lastAuth;
}
