package dpa.testwork.tlgrm.process_user.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class TelegramUser {
    private Long id;

    @SerializedName("is_bot")
    private Boolean isBot;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String username;

    @SerializedName("language_code")
    private String languageCode;

    @SerializedName("is_premium")
    private Boolean isPremium;

    @SerializedName("added_to_attachment_menu")
    private Boolean addedToAttachmentMenu;

    @SerializedName("allows_write_to_pm")
    private Boolean allowsWriteToPm;

    @SerializedName("photo_url")
    private String photoUrl;

    private Date lastAuth;
}
