package ru.kolpakovee.userservice.services;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.kolpakovee.userservice.records.UserRegistrationRequest;
import ru.kolpakovee.userservice.records.UserResponse;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    @Value("${keycloak.realm-name}")
    private String realmName;

    private final Keycloak keycloak;

    public UserRepresentation getUserById(String userId) throws NotFoundException {
        return keycloak.realm(realmName)
                .users()
                .get(userId)
                .toRepresentation();
    }

    public UserResponse registerUser(UserRegistrationRequest request) {
        UserRepresentation user = createRepresentation(request);

        try (Response response = keycloak.realm(realmName).users().create(user)) {
            if (response.getStatus() == 201) {
                String userId = getCreatedId(response);
                setUserPassword(userId, request.getPassword());
                return new UserResponse(userId, user.getEmail(), user.getFirstName(), user.getLastName());
            }
            throw new RuntimeException("Error creating user: " + response.getStatusInfo());
        }
    }

    public void updatePassword(String userId, String newPassword) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newPassword);
        credential.setTemporary(false);

        keycloak.realm(realmName)
                .users()
                .get(userId)
                .resetPassword(credential);
    }

    private String getCreatedId(Response response) {
        String location = response.getHeaderString("Location");
        return location.substring(location.lastIndexOf('/') + 1);
    }

    private void setUserPassword(String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        keycloak.realm(realmName).users().get(userId)
                .resetPassword(credential);
    }

    private UserRepresentation createRepresentation(UserRegistrationRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getEmail());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(false);
        return user;
    }
}
