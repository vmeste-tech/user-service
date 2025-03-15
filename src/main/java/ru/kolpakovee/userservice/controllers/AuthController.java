package ru.kolpakovee.userservice.controllers;

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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Mono<TokenResponse> login(@Valid @RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/refresh")
    public Mono<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }
}