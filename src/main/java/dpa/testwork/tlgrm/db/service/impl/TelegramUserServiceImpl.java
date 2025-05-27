package dpa.testwork.tlgrm.db.service.impl;

import dpa.testwork.tlgrm.db.entity.TelegramUserEntity;
import dpa.testwork.tlgrm.db.mapper.TelegramUserMapper;
import dpa.testwork.tlgrm.db.repository.TelegramUserRepository;
import dpa.testwork.tlgrm.db.service.TelegramUserService;
import dpa.testwork.tlgrm.process_user.dto.TelegramUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;
    private final TelegramUserMapper telegramUserMapper;

    @Override
    @Transactional
    public TelegramUserEntity saveOrUpdateUser(TelegramUser telegramUser) {
        Objects.requireNonNull(telegramUser, "TelegramUser must not be null");

        TelegramUserEntity entity = telegramUserMapper.toEntity(telegramUser);
        return telegramUserRepository.save(entity);
    }

    @Override
    public TelegramUserEntity findById(Long id) {
        return telegramUserRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void updateLastAuthTime(Long userId, Date authTime) {
        telegramUserRepository.findById(userId).ifPresent(user -> {
            user.setLastAuth(authTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            telegramUserRepository.save(user);
        });
    }
}
