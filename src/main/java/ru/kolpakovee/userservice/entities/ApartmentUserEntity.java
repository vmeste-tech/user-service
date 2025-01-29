package ru.kolpakovee.userservice.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "apartment_users")
public class ApartmentUserEntity {

    // TODO: подумать как сделать PK (apartmentId, userId)
    @Id
    private UUID apartmentId;

    @Id
    private UUID userId;

    private LocalDateTime joinedAt;
}
