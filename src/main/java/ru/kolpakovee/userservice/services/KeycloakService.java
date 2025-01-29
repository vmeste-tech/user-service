package ru.kolpakovee.userservice.services;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
}
