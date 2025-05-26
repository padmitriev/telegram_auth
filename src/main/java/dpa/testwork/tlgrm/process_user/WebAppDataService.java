package dpa.testwork.tlgrm.process_user;

import com.google.gson.Gson;
import dpa.testwork.tlgrm.process_user.dto.TelegramWebAppData;
import dpa.testwork.tlgrm.process_user.dto.TelegramChat;
import dpa.testwork.tlgrm.process_user.dto.TelegramUser;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebAppDataService {

    private static final Gson gson = new Gson();

    // !заглушка!
    public boolean validateInitData(String initData) {
        System.out.println("Validation skipped");
        return true;
    }

    public TelegramUser parseUserData(String initData) {
        try {
            Map<String, String> params = parseInitData(initData);
            String userJson = params.get("user");
            if (userJson == null) {
                System.err.println("В initData нет поля 'user'");
                return null;
            }

            TelegramUser user = parseUserFromJson(userJson);
            if (user == null) {
                System.err.println("Не удалось распарсить данные пользователя");
                return null;
            }

            String authDateStr = params.get("auth_date");
            if (authDateStr != null) {
                try {
                    long authDate = Long.parseLong(authDateStr);
                    user.setLastAuth(new Date(authDate * 1000L));
                } catch (NumberFormatException e) {
                    System.err.println("Неверный формат auth_date: " + authDateStr);
                }
            }

            return user;

        } catch (Exception e) {
            System.err.println("Ошибка при извлечении данных пользователя: " + e.getMessage());
            return null;
        }
    }

    public void logWebAppData(String initData) {
        try {
            Map<String, String> params = parseInitData(initData);
            TelegramWebAppData data = new TelegramWebAppData();

            data.setQueryId(params.get("query_id"));
            data.setUser(parseUserFromJson(params.get("user")));
            data.setReceiver(parseUserFromJson(params.get("receiver")));
            data.setChat(parseChatFromJson(params.get("chat"))); // ← можно удалить, если не нужно
            data.setChatType(params.get("chat_type"));
            data.setChatInstance(params.get("chat_instance"));
            data.setStartParam(params.get("start_param"));
            if (params.containsKey("can_send_after")) {
                data.setCanSendAfter(Integer.parseInt(params.get("can_send_after")));
            }
            if (params.containsKey("auth_date")) {
                data.setAuthDate(Long.parseLong(params.get("auth_date")));
            }

            System.out.println("=== Данные от Telegram WebApp ===");
            System.out.println("Query ID: " + data.getQueryId());
            if (data.getUser() != null) {
                System.out.println("User ID: " + data.getUser().getId());
                System.out.println("First Name: " + data.getUser().getFirstName());
                System.out.println("Username: " + data.getUser().getUsername());
            }
            if (data.getChat() != null) {
                System.out.println("Chat Title: " + data.getChat().getTitle());
            }
            System.out.println("Auth Date: " + new Date(data.getAuthDate() * 1000L));
            System.out.println("=================================");
        } catch (Exception e) {
            System.err.println("Ошибка при парсинге данных: " + e.getMessage());
        }
    }

    private TelegramUser parseUserFromJson(String userJson) {
        if (userJson == null) return null;

        try {
            String decoded = URLDecoder.decode(userJson, StandardCharsets.UTF_8);
            return gson.fromJson(decoded, TelegramUser.class);
        } catch (Exception e) {
            System.err.println("Ошибка при декодировании или парсинге user JSON: " + e.getMessage());
            return null;
        }
    }

    private TelegramChat parseChatFromJson(String chatJson) {
        if (chatJson == null) return null;

        try {
            String decoded = URLDecoder.decode(chatJson, StandardCharsets.UTF_8);
            return gson.fromJson(decoded, TelegramChat.class);
        } catch (Exception e) {
            System.err.println("Ошибка при декодировании или парсинге chat JSON: " + e.getMessage());
            return null;
        }
    }

    private Map<String, String> parseInitData(String initData) {
        Map<String, String> result = new HashMap<>();
        for (String pair : initData.split("&")) {
            int idx = pair.indexOf('=');
            if (idx != -1) {
                String key = pair.substring(0, idx);
                String value = pair.substring(idx + 1);
                result.put(key, value);
            }
        }
        return result;
    }
}
