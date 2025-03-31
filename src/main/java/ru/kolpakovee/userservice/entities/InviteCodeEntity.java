package ru.kolpakovee.userservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "invite_codes")
public class InviteCodeEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private Long code;

    @Column(updatable = false, nullable = false)
    private UUID apartmentId;
}
