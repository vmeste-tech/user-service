package ru.kolpakovee.userservice.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import ru.kolpakovee.userservice.entities.ApartmentUserEntity;
import ru.kolpakovee.userservice.entities.ApartmentUserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApartmentUserRepository extends JpaRepository<ApartmentUserEntity, ApartmentUserId> {

    List<ApartmentUserEntity> findAllByIdApartmentId(UUID apartmentId);

    Optional<ApartmentUserEntity> findByIdUserId(UUID userId);

    @Transactional
    @Modifying
    void deleteAllByIdApartmentId(UUID apartmentId);
}
