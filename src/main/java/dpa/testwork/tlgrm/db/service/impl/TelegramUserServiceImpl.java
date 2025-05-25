package dpa.testwork.tlgrm.db.service.impl;

import dpa.testwork.tlgrm.db.repository.TelegramUserRepository;
import dpa.testwork.tlgrm.db.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;
}
