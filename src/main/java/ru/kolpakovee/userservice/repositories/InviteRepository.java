package ru.kolpakovee.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kolpakovee.userservice.entities.InviteCodeEntity;

@Repository
public interface InviteRepository extends JpaRepository<InviteCodeEntity, Long> {
}
