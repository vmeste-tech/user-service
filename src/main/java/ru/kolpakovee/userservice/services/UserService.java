package ru.kolpakovee.userservice.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ru.kolpakovee.userservice.constants.NotificationMessages;
import ru.kolpakovee.userservice.entities.UserEntity;
import ru.kolpakovee.userservice.enums.UserStatus;
import ru.kolpakovee.userservice.records.UpdateUserProfileRequest;
import ru.kolpakovee.userservice.models.apartments.ChangePasswordRequest;
import ru.kolpakovee.userservice.producer.NotificationEventProducer;
import ru.kolpakovee.userservice.records.GetUserResponse;
import ru.kolpakovee.userservice.records.UserRegistrationRequest;
import ru.kolpakovee.userservice.records.UserResponse;
import ru.kolpakovee.userservice.repositories.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakService keycloakService;
    private final UserRepository repository;
    private final NotificationEventProducer producer;
    private final S3Service s3Service;

    public GetUserResponse getUser(UUID userId) {
        UserRepresentation userRepresentation = keycloakService.getUserById(String.valueOf(userId));

        UserEntity user = repository.findById(userId)
                .orElseGet(() -> createUser(userRepresentation.getId()));

        return GetUserResponse.builder()
                .id(userRepresentation.getId())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .email(userRepresentation.getEmail())
                .createdAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(userRepresentation.getCreatedTimestamp()), ZoneId.systemDefault()))
                .profilePictureUrl(user.getProfilePictureUrl())
                .status(user.getStatus())
                .build();
    }

    @Transactional
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        producer.send(userId, NotificationMessages.PASSWORD_UPDATE);
        keycloakService.updatePassword(String.valueOf(userId), request.getNewPassword());
    }

    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        UserResponse user = keycloakService.registerUser(request);
        createUser(user.id());
        producer.send(UUID.fromString(user.id()), NotificationMessages.REGISTER_USER);
        return user;
    }

    public void deleteUser(UUID userId) {
        repository.deleteById(userId);
    }

    @Transactional
    public GetUserResponse updateUserProfile(UUID userId, UpdateUserProfileRequest request) {
        updateKeycloakInfo(userId, request);
        updateLocalUserData(userId, request);
        producer.send(userId, NotificationMessages.PROFILE_UPDATE);
        return getUser(userId);
    }

    private void updateKeycloakInfo(UUID userId, UpdateUserProfileRequest request) {
        if (hasKeycloakUpdates(request)) {
            UserRepresentation user = keycloakService.getUserById(String.valueOf(userId));
            Optional.ofNullable(request.firstName()).ifPresent(user::setFirstName);
            Optional.ofNullable(request.lastName()).ifPresent(user::setLastName);
            Optional.ofNullable(request.email()).ifPresent(user::setEmail);
            keycloakService.updateUser(user);
        }
    }

    private boolean hasKeycloakUpdates(UpdateUserProfileRequest request) {
        return request.firstName() != null || request.lastName() != null || request.email() != null;
    }

    private void updateLocalUserData(UUID userId, UpdateUserProfileRequest request) {
        if (hasLocalUpdates(request)) {
            UserEntity user = repository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

            Optional.ofNullable(request.profilePictureBase64())
                    .filter(pic -> !pic.isEmpty())
                    .ifPresent(pic -> user.setProfilePictureUrl(s3Service.uploadBase64File(pic)));

            Optional.ofNullable(request.status()).ifPresent(user::setStatus);
            repository.save(user);
        }
    }

    private boolean hasLocalUpdates(UpdateUserProfileRequest request) {
        return request.status() != null ||
                (request.profilePictureBase64() != null && !request.profilePictureBase64().isEmpty());
    }

    private UserEntity createUser(String userId) {
        UserEntity newUser = new UserEntity();
        newUser.setId(UUID.fromString(userId));
        newUser.setStatus(UserStatus.ACTIVE);

        return repository.save(newUser);
    }
}
