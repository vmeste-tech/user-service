package ru.kolpakovee.userservice.records;

import lombok.Builder;

@Builder
public record InviteCodeDto(
        String code
) {
}
