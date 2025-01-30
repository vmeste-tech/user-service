package ru.kolpakovee.userservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

//TODO: подумать над минимальным набором аннотация для Entity
@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(length = 100)
    private String profilePictureUrl;
}
