package dpa.testwork.tlgrm.db.repository;

import dpa.testwork.tlgrm.db.entity.TelegramUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUserEntity, Long> {
}
