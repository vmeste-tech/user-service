package ru.kolpakovee.userservice.records;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank String refresh_token
) {}
