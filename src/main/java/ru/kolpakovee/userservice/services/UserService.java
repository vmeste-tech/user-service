package ru.kolpakovee.userservice.services;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ru.kolpakovee.userservice.entities.UserEntity;
import ru.kolpakovee.userservice.models.apartments.ChangePasswordRequest;
import ru.kolpakovee.userservice.models.users.GetUserResponse;
import ru.kolpakovee.userservice.repositories.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakService keycloak;
    private final UserRepository repository;

    public GetUserResponse getUser(UUID userId) {
        UserRepresentation userRepresentation = keycloak.getUserById(String.valueOf(userId));

        UserEntity user = repository.findById(userId)
                .orElseGet(() -> createUser(userRepresentation));

        return GetUserResponse.builder()
                .id(userRepresentation.getId())
                .username(userRepresentation.getUsername())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .email(userRepresentation.getEmail())
                .createdAt(userRepresentation.getCreatedTimestamp())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }

    public void changePassword(UUID userId, ChangePasswordRequest request) {
        keycloak.updatePassword(String.valueOf(userId), request.getNewPassword());
    }

    private UserEntity createUser(UserRepresentation user) {
        UserEntity newUser = UserEntity.builder()
                .id(UUID.fromString(user.getId()))
                .username(user.getUsername())
                .createdAt(user.getCreatedTimestamp())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .build();

        return repository.save(newUser);
    }
}
