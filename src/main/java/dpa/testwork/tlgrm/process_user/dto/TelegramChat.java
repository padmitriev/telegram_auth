package dpa.testwork.tlgrm.process_user.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TelegramChat {
    private Long id;
    private String type;
    private String title;
    private String username;
    private String photo_url;
}