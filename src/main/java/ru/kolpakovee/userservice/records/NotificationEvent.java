package ru.kolpakovee.userservice.records;

import lombok.Builder;
import ru.kolpakovee.userservice.enums.NotificationCategory;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record NotificationEvent(
        UUID userId,
        String message,
        NotificationCategory category,
        LocalDateTime timestamp
) {
}
