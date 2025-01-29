package ru.kolpakovee.userservice.entities;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class ApartmentUserEntityPK {

    private UUID userId;

    private UUID apartmentId;
}
