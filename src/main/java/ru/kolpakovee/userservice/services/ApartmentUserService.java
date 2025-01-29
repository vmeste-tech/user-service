package ru.kolpakovee.userservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolpakovee.userservice.entities.ApartmentUserEntity;
import ru.kolpakovee.userservice.entities.ApartmentUserEntityPK;
import ru.kolpakovee.userservice.entities.UserEntity;
import ru.kolpakovee.userservice.models.apartments.AddToApartmentResponse;
import ru.kolpakovee.userservice.models.apartments.GetApartmentResponse;
import ru.kolpakovee.userservice.models.users.GetUserResponse;
import ru.kolpakovee.userservice.repositories.ApartmentUserRepository;
import ru.kolpakovee.userservice.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApartmentUserService {

    private final ApartmentUserRepository apartmentUserRepository;
    private final UserRepository userRepository;

    public AddToApartmentResponse addToApartment(UUID apartmentId, UUID userId) {
        // TODO: подумать, нужна ли проверка на существование юзера или квартиры?
        ApartmentUserEntity newApartmentUser = new ApartmentUserEntity(apartmentId, userId, LocalDateTime.now());
        newApartmentUser = apartmentUserRepository.save(newApartmentUser);

        return AddToApartmentResponse.builder()
                .apartmentId(newApartmentUser.getApartmentId())
                .userId(newApartmentUser.getUserId())
                .joinedAt(newApartmentUser.getJoinedAt())
                .build();
    }

    public void deleteFromApartment(UUID apartmentId, UUID userId) {
        apartmentUserRepository.deleteById(new ApartmentUserEntityPK(userId, apartmentId));
    }

    public List<GetUserResponse> getApartmentUsers(UUID apartmentId) {
        // TODO: Может можно как-то достать только UUIDs, а не все данные, а потом преобразовывать?
        List<UUID> userIds = apartmentUserRepository.findAllByApartmentId(apartmentId)
                .stream()
                .map(ApartmentUserEntity::getUserId)
                .toList();

        List<UserEntity> users = userRepository.findAllById(userIds);

        return users.stream()
                .map(user -> GetUserResponse.builder()
                        .id(String.valueOf(user.getId()))
                        .username(user.getUsername())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build())
                .toList();
    }
}
