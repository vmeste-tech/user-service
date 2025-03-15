package ru.kolpakovee.userservice.records;

public record TokenResponse(
        String access_token,
        String refresh_token,
        Long expires_in,
        String token_type
) {}