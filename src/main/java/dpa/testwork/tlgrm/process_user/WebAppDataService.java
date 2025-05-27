package dpa.testwork.tlgrm.process_user;

import dpa.testwork.tlgrm.db.service.TelegramUserService;
import dpa.testwork.tlgrm.process_user.dto.TelegramUser;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.HmacUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebAppDataService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final TelegramUserService telegramUserService;
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

            log.debug("Calculated hash: {}", calculatedHash);
            log.debug("Received hash:   {}", receivedHash);

            boolean isValid = calculatedHash.equals(receivedHash);
            if (isValid) {
                log.info("Hashes match. Signature is valid.");
            } else {
                log.warn("Hashes do not match. Signature is invalid.");
            }

            if (isValid) {
                TelegramUser telegramUser = parseUserData(initData);
                if (telegramUser != null) {
                    telegramUser.setLastAuth(new Date());
                    telegramUserService.saveOrUpdateUser(telegramUser);
                }
            }

            return isValid;

        } catch (Exception e) {
            log.error("Error during initData validation", e);
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
                log.warn("User field is missing in initData");
                return null;
            }

            String decoded = URLDecoder.decode(userJson, StandardCharsets.UTF_8);
            TelegramUser user = gson.fromJson(decoded, TelegramUser.class);

            if (user == null) {
                log.warn("Failed to parse user JSON data");
                return null;
            }

            user.setLastAuth(new Date());

            log.info("User recognized:");
            log.info("ID: {}", user.getId());
            log.info("First Name: {}", user.getFirstName());
            log.info("Username: {}", user.getUsername());
            log.info("Last Auth: {}", user.getLastAuth());

            return user;

        } catch (Exception e) {
            log.error("Error parsing user data", e);
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