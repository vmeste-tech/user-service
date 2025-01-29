package ru.kolpakovee.userservice.models.apartments;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class AddToApartmentResponse {

    UUID apartmentId;

    UUID userId;

    LocalDateTime joinedAt;
}
