package ru.kolpakovee.userservice.models.apartments;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CreateApartmentRequest {

    String name;

    String address;
}
