package ru.kolpakovee.userservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "apartments")
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentEntity {

    @Id
    private UUID id;

    @Size(max = 50)
    @NotBlank
    private String name;

    @NotBlank
    private String address;
}
