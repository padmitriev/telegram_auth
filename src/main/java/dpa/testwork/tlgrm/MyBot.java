package dpa.testwork.tlgrm;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;

@Component
public class MyBot extends TelegramLongPollingBot {

    private final String BOT_TOKEN = "";
    private final String BOT_USERNAME = "";

    private static final String WEB_APP_URL = "";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            String chatId = update.getMessage().getChatId().toString();

            InlineKeyboardButton webAppButton = new InlineKeyboardButton("Open WebApp");
            webAppButton.setWebApp(new WebAppInfo(WEB_APP_URL));

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(Collections.singletonList(Collections.singletonList(webAppButton)));

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Нажми, чтобы открыть WebApp:");
            message.setReplyMarkup(markup);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}