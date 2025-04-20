package ru.kolpakovee.userservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ru.kolpakovee.userservice.constants.NotificationMessages;
import ru.kolpakovee.userservice.entities.UserEntity;
import ru.kolpakovee.userservice.enums.UserStatus;
import ru.kolpakovee.userservice.models.apartments.ChangePasswordRequest;
import ru.kolpakovee.userservice.producer.NotificationEventProducer;
import ru.kolpakovee.userservice.records.GetUserResponse;
import ru.kolpakovee.userservice.records.UserRegistrationRequest;
import ru.kolpakovee.userservice.records.UserResponse;
import ru.kolpakovee.userservice.repositories.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakService keycloakService;
    private final UserRepository repository;

    private final NotificationEventProducer producer;

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

    private UserEntity createUser(String userId) {
        UserEntity newUser = new UserEntity();
        newUser.setId(UUID.fromString(userId));
        newUser.setStatus(UserStatus.ACTIVE);

        return repository.save(newUser);
    }
}
