package ru.kolpakovee.userservice.records;

import ru.kolpakovee.userservice.enums.UserStatus;

public record UpdateUserProfileRequest(
    String firstName,
    String lastName,
    String email,
    UserStatus status,
    String profilePictureBase64
) {}
