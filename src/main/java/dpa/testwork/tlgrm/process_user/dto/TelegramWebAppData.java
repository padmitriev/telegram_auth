package dpa.testwork.tlgrm.process_user.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TelegramWebAppData {
    private String queryId;
    private TelegramUser user;
    private TelegramUser receiver;
    private TelegramChat chat;
    private String chatType;
    private String chatInstance;
    private String startParam;
    private Integer canSendAfter;
    private Long authDate;
    private String hash;
    private String signature;
}
