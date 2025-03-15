package ru.kolpakovee.userservice.services;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.kolpakovee.userservice.records.AuthRequest;
import ru.kolpakovee.userservice.records.RefreshTokenRequest;
import ru.kolpakovee.userservice.records.TokenResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebClient keycloakWebClient;

    @Value("${keycloak.realm-name}")
    private String realmName;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public Mono<TokenResponse> authenticate(AuthRequest request) {
        return keycloakWebClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realmName)
                .body(BodyInserters.fromFormData(createAuthBody(request)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(TokenResponse.class);
    }

    public Mono<TokenResponse> refreshToken(RefreshTokenRequest request) {
        return keycloakWebClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realmName)
                .body(BodyInserters.fromFormData(createRefreshBody(request)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(TokenResponse.class);
    }

    private MultiValueMap<String, String> createAuthBody(AuthRequest request) {
        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", request.username());
        formData.add("password", request.password());
        formData.add("grant_type", "password");
        return formData;
    }

    private MultiValueMap<String, String> createRefreshBody(RefreshTokenRequest request) {
        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", request.refresh_token());
        formData.add("grant_type", "refresh_token");
        return formData;
    }

    private Mono<? extends Throwable> handleError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(body -> Mono.error(new AuthException(
                        "Keycloak error: " + response.statusCode() + " - " + body
                )));
    }
}
