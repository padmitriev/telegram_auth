package dpa.testwork.tlgrm.db.mapper;

import dpa.testwork.tlgrm.db.entity.TelegramUserEntity;
import dpa.testwork.tlgrm.process_user.dto.TelegramUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface TelegramUserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "lastAuth", expression = "java(convertDateToLocalDateTime(user.getLastAuth()))")
    TelegramUserEntity toEntity(TelegramUser user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "lastAuth", expression = "java(convertLocalDateTimeToDate(entity.getLastAuth()))")
    TelegramUser toDto(TelegramUserEntity entity);

    default LocalDateTime convertDateToLocalDateTime(Date date) {
        return date != null ?
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() :
                null;
    }

    default Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        return localDateTime != null ?
                Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()) :
                null;
    }
}


//@Mapper(componentModel = "spring")
//public interface TelegramUserMapper {
//
//    @Mapping(target = "lastAuth", source = "lastAuth", dateFormat = "java.time.LocalDateTime")
//    TelegramUserEntity toEntity(TelegramUser user);
//
//    @Mapping(target = "lastAuth", source = "lastAuth", dateFormat = "java.util.Date")
//    TelegramUser toDto(TelegramUserEntity entity);
//
//    default LocalDateTime map(Date date) {
//        if (date == null) {
//            return null;
//        }
//        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//    }
//
//    default Date map(LocalDateTime localDateTime) {
//        if (localDateTime == null) {
//            return null;
//        }
//        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
//    }
//}

//@Mapper(componentModel = "spring")
//public interface TelegramUserMapper {
//
//    @Mapping(target = "lastAuth",
//            expression = "java(user.getLastAuth() != null ? user.getLastAuth().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null)")
//    TelegramUserEntity toEntity(TelegramUser user);
//
//    @Mapping(target = "lastAuth",
//            expression = "java(entity.getLastAuth() != null ? Date.from(entity.getLastAuth().atZone(ZoneId.systemDefault()).toInstant()) : null)")
//    TelegramUser toDto(TelegramUserEntity entity);
//}
