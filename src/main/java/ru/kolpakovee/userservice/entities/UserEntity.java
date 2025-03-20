package ru.kolpakovee.userservice.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.kolpakovee.userservice.enums.UserStatus;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(length = 100)
    private String profilePictureUrl;
}
