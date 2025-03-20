package ru.kolpakovee.userservice.records;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.kolpakovee.userservice.enums.UserStatus;

import java.time.LocalDateTime;

@Builder
public record GetUserResponse(
        @NotNull String id,
        @Email String email,
        @NotEmpty
        String firstName,
        @NotEmpty
        String lastName,
        @Nullable String profilePictureUrl,
        LocalDateTime createdAt,
        @NotNull UserStatus status
) {
}
