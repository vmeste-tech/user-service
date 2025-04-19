package ru.kolpakovee.userservice.services;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolpakovee.userservice.entities.ApartmentEntity;
import ru.kolpakovee.userservice.entities.ApartmentUserEntity;
import ru.kolpakovee.userservice.enums.UserStatus;
import ru.kolpakovee.userservice.models.apartments.*;
import ru.kolpakovee.userservice.records.GetUserResponse;
import ru.kolpakovee.userservice.records.ApartmentInfo;
import ru.kolpakovee.userservice.records.UserInfoDto;
import ru.kolpakovee.userservice.repositories.ApartmentRepository;
import ru.kolpakovee.userservice.repositories.ApartmentUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentUserRepository apartmentUserRepository;

    private final UserService userService;
    private final ApartmentUserService apartmentUserService;

    @Transactional
    public CreateApartmentResponse createApartment(CreateApartmentRequest request, UUID userId) {
        ApartmentEntity apartment = createApartmentEntity(request);
        apartmentUserService.addToApartment(apartment.getId(), userId);

        return CreateApartmentResponse.builder()
                .apartmentId(apartment.getId())
                .name(apartment.getName())
                .address(apartment.getAddress())
                .build();
    }

    public GetApartmentResponse getApartment(UUID apartmentId) {
        return apartmentRepository.findById(apartmentId)
                .map(this::mapToGetApartmentResponse)
                .orElseThrow(() -> new NotFoundException("Квартира не найдена."));
    }

    @Transactional
    public UpdateApartmentResponse updateApartment(UUID apartmentId, UpdateApartmentRequest request) {
        ApartmentEntity existedApartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new NotFoundException("Квартира не существует."));

        existedApartment.setAddress(request.getAddress());
        existedApartment.setName(request.getName());

        existedApartment = apartmentRepository.save(existedApartment);

        return UpdateApartmentResponse.builder()
                .id(existedApartment.getId())
                .address(existedApartment.getAddress())
                .name(existedApartment.getName())
                .build();
    }

    @Transactional
    public void deleteApartment(UUID apartmentId) {
        // TODO: удалить все правила и задачи
        apartmentUserRepository.deleteAllByIdApartmentId(apartmentId);
        apartmentRepository.deleteById(apartmentId);
    }

    private GetApartmentResponse mapToGetApartmentResponse(ApartmentEntity apartment) {
        return GetApartmentResponse.builder()
                .address(apartment.getAddress())
                .name(apartment.getName())
                .build();
    }

    private ApartmentEntity createApartmentEntity(CreateApartmentRequest request) {
        ApartmentEntity newApartment = new ApartmentEntity();
        newApartment.setName(request.getName());
        newApartment.setAddress(request.getAddress());

        return apartmentRepository.save(newApartment);
    }

    @Transactional
    public ApartmentInfo findApartment(UUID userId) {
        UUID apartmentId = apartmentUserRepository.findByIdUserId(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не проживает в квартирах."))
                .getId()
                .getApartmentId();

        List<ApartmentUserEntity> users = apartmentUserRepository.findAllByIdApartmentId(apartmentId);

        ApartmentEntity apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new NotFoundException("Квартира не найдена."));

        return ApartmentInfo.builder()
                .apartmentId(apartment.getId())
                .address(apartment.getAddress())
                .name(apartment.getName())
                .users(users.stream()
                        // TODO: оптимизировать кол-во запросов
                        .map(u -> toUserInfoDto(u.getJoinedAt(), userService.getUser(u.getId().getUserId())))
                        .toList())
                .build();
    }

    // TODO: использовать мапер
    private UserInfoDto toUserInfoDto(LocalDateTime joinedAt, GetUserResponse user) {
        return UserInfoDto.builder()
                .type("Пользователь")
                .status(UserStatus.ACTIVE)
                .name(user.firstName())
                .lastname(user.lastName())
                .photoUrl(user.profilePictureUrl())
                .joinedAt(joinedAt)
                .id(user.id())
                .build();
    }
}
