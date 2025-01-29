package ru.kolpakovee.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kolpakovee.userservice.entities.ApartmentEntity;

import java.util.UUID;

@Repository
public interface ApartmentRepository extends JpaRepository<ApartmentEntity, UUID> {
}
