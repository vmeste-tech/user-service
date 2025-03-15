package ru.kolpakovee.userservice.records;

public record UserResponse(
        String id,
        String email,
        String firstName,
        String lastName
) {}
