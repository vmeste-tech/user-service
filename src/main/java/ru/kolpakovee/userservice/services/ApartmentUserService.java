package ru.kolpakovee.userservice.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolpakovee.userservice.entities.ApartmentEntity;
import ru.kolpakovee.userservice.entities.ApartmentUserEntity;
import ru.kolpakovee.userservice.entities.ApartmentUserId;
import ru.kolpakovee.userservice.entities.UserEntity;
import ru.kolpakovee.userservice.models.apartments.AddToApartmentResponse;
import ru.kolpakovee.userservice.models.users.GetUserResponse;
import ru.kolpakovee.userservice.repositories.ApartmentRepository;
import ru.kolpakovee.userservice.repositories.ApartmentUserRepository;
import ru.kolpakovee.userservice.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApartmentUserService {

    private final ApartmentUserRepository apartmentUserRepository;
    private final ApartmentRepository apartmentRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public AddToApartmentResponse addToApartment(UUID apartmentId, UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден."));

        ApartmentEntity apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new EntityNotFoundException("Квартира не найдена."));

        ApartmentUserEntity newApartmentUser = apartmentUserRepository.save(createApartmentUser(user, apartment));

        return AddToApartmentResponse.builder()
                .apartmentId(newApartmentUser.getId().getApartmentId())
                .userId(newApartmentUser.getId().getUserId())
                .joinedAt(newApartmentUser.getJoinedAt())
                .build();
    }

    public void deleteFromApartment(UUID apartmentId, UUID userId) {
        ApartmentUserId id = new ApartmentUserId();
        id.setApartmentId(apartmentId);
        id.setUserId(userId);

        apartmentUserRepository.deleteById(id);
    }

    public List<GetUserResponse> getApartmentUsers(UUID apartmentId) {
        // TODO: Может можно как-то достать только UUIDs, а не все данные, а потом преобразовывать?
        return apartmentUserRepository.findAllByIdApartmentId(apartmentId)
                .stream()
                .map(a -> a.getId().getUserId())
                .map(userService::getUser)
                .toList();
    }

    private ApartmentUserEntity createApartmentUser(UserEntity user, ApartmentEntity apartment) {
        ApartmentUserEntity newApartmentUser = new ApartmentUserEntity();
        newApartmentUser.setId(createApartmentUserId(user.getId(), apartment.getId()));
        newApartmentUser.setJoinedAt(LocalDateTime.now());

        return newApartmentUser;
    }

    private ApartmentUserId createApartmentUserId(UUID userId, UUID apartmentId) {
        ApartmentUserId apartmentUserId = new ApartmentUserId();
        apartmentUserId.setApartmentId(apartmentId);
        apartmentUserId.setUserId(userId);

        return apartmentUserId;
    }
}
