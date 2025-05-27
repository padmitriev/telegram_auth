package dpa.testwork.tlgrm.db.service;

import dpa.testwork.tlgrm.db.entity.TelegramUserEntity;
import dpa.testwork.tlgrm.process_user.dto.TelegramUser;

import java.util.Date;

public interface TelegramUserService {
    /**
     * Сохраняет или обновляет данные пользователя
     * @param telegramUser Данные пользователя из Telegram
     * @return Сохраненная сущность
     */
    TelegramUserEntity saveOrUpdateUser(TelegramUser telegramUser);

    /**
     * Находит пользователя по ID
     * @param id ID пользователя в Telegram
     * @return Найденный пользователь или null
     */
    TelegramUserEntity findById(Long id);

    /**
     * Обновляет время последней авторизации пользователя
     * @param userId ID пользователя
     * @param authTime Время авторизации
     */
    void updateLastAuthTime(Long userId, Date authTime);
}