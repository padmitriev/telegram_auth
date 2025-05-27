package dpa.testwork.tlgrm;

import dpa.testwork.tlgrm.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class MyBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            String chatId = update.getMessage().getChatId().toString();

            InlineKeyboardButton webAppButton = new InlineKeyboardButton("Open WebApp");
            webAppButton.setWebApp(new WebAppInfo(botConfig.getWebAppUrl()));

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(Collections.singletonList(Collections.singletonList(webAppButton)));

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Нажми, чтобы открыть WebApp:");
            message.setReplyMarkup(markup);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Error sending message", e);
            }
        }
    }
}


