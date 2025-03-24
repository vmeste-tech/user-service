package ru.kolpakovee.userservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.kolpakovee.userservice.records.AuthRequest;
import ru.kolpakovee.userservice.records.RefreshTokenRequest;
import ru.kolpakovee.userservice.records.TokenResponse;
import ru.kolpakovee.userservice.services.AuthService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Управление авторизацией", description = "API для управления авторизацией")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Получение JWT токена",
            description = "Позволяет получить JWT токен для доступа к защищенным ресурсам")
    public Mono<TokenResponse> login(@Valid @RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Обновление JWT токена",
            description = "Позволяет обновить JWT токен для доступа к защищенным ресурсам")
    public Mono<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }
}