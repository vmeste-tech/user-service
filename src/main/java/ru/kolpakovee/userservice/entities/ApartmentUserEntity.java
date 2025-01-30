package ru.kolpakovee.userservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "apartment_users")
public class ApartmentUserEntity {

    @EmbeddedId
    private ApartmentUserId id;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}
