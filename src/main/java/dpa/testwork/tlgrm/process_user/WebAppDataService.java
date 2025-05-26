package dpa.testwork.tlgrm.process_user;

import dpa.testwork.tlgrm.process_user.dto.TelegramUser;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.HmacUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebAppDataService {

    @Value("${telegram.bot.token}")
    private String botToken;
    private static final String WEB_APP_DATA = "WebAppData";
    private static final Gson gson = new Gson();

    public boolean validateInitData(String initData) {


        try {
            Map<String, String> initDataMap = parseQueryString(initData);


            String receivedHash = initDataMap.remove("hash");

            Map<String, String> sortedParams = new TreeMap<>(initDataMap);

            StringBuilder dataCheckString = new StringBuilder();
            for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
                if (dataCheckString.length() > 0) {
                    dataCheckString.append("\n");
                }
                dataCheckString.append(entry.getKey()).append("=").append(entry.getValue());
            }

            byte[] secretKey = new HmacUtils("HmacSHA256", WEB_APP_DATA).hmac(botToken);
            String calculatedHash = new HmacUtils("HmacSHA256", secretKey).hmacHex(dataCheckString.toString());

            System.out.println("Рассчитанный hash: " + calculatedHash);
            System.out.println("Полученный hash:   " + receivedHash);

            boolean isValid = calculatedHash.equals(receivedHash);
            if (isValid) {
                System.out.println("Хэши совпадают");
            } else {
                System.out.println("Хэши не совпадают");
            }

            return isValid;

        } catch (Exception e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
            return false;
        }
    }

    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> parameters = new TreeMap<>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8) : pair;
            String value = idx > 0 && pair.length() > idx + 1
                    ? URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8)
                    : null;
            parameters.put(key, value);
        }
        return parameters;
    }

        public TelegramUser parseUserData(String initData) {
        try {
            Map<String, String> params = parseInitData(initData);
            String userJson = params.get("user");
            if (userJson == null) {
                System.out.println("Ошибка: отсутствует user в initData");
                return null;
            }

            String decoded = URLDecoder.decode(userJson, StandardCharsets.UTF_8);
            TelegramUser user = gson.fromJson(decoded, TelegramUser.class);

            if (user == null) {
                System.out.println("Ошибка: не удалось распарсить user данные");
                return null;
            }

            System.out.println("\nДанные пользователя:");
            System.out.println("ID: " + user.getId());
            System.out.println("Имя: " + user.getFirstName());
            System.out.println("Username: " + user.getUsername());

            return user;

        } catch (Exception e) {
            System.out.println("Ошибка при парсинге user данных: " + e.getMessage());
            return null;
        }
    }

        private Map<String, String> parseInitData(String initData) {
        Map<String, String> result = new HashMap<>();
        for (String pair : initData.split("&")) {
            int idx = pair.indexOf('=');
            if (idx > 0 && idx < pair.length() - 1) {
                String key = pair.substring(0, idx);
                String value = pair.substring(idx + 1);
                result.put(key, value);
            }
        }
        return result;
    }
}