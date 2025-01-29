package ru.kolpakovee.userservice.models.apartments;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class UpdateApartmentResponse {

    UUID id;

    String name;

    String address;
}
