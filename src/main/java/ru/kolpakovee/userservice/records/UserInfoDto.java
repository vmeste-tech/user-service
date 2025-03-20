package ru.kolpakovee.userservice.records;

import lombok.Builder;
import ru.kolpakovee.userservice.enums.UserStatus;

import java.time.LocalDateTime;

@Builder
public record UserInfoDto(
        String id,
        String name,
        String lastname,
        String photoUrl,
        String type,
        LocalDateTime joinedAt,
        UserStatus status
) {
}
