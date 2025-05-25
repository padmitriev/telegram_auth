package dpa.testwork.tlgrm.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TelegramUserEntity {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDateTime lastAuth;
}
