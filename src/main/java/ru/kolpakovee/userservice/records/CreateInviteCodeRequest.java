package ru.kolpakovee.userservice.records;

import java.util.UUID;

public record CreateInviteCodeRequest(
        UUID apartmentId
) {
}
