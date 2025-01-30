package ru.kolpakovee.userservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class ApartmentUserId implements Serializable {

    @Column(name = "apartment_id")
    private UUID apartmentId;

    @Column(name = "user_id")
    private UUID userId;
}
