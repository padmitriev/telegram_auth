package dpa.testwork.tlgrm;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotRunner {

    private final MyBot myBot;
    public BotRunner(MyBot myBot) {
        this.myBot = myBot;
    }

    @PostConstruct
    public void registerBot() {
        try {
            var botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(myBot);
            System.out.println("Bot registered");
        } catch (Exception e) {
            System.err.println("Failed to register bot: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
