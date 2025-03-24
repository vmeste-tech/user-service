package ru.kolpakovee.userservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.kolpakovee.userservice.models.apartments.ChangePasswordRequest;
import ru.kolpakovee.userservice.records.GetUserResponse;
import ru.kolpakovee.userservice.records.UserRegistrationRequest;
import ru.kolpakovee.userservice.records.UserResponse;
import ru.kolpakovee.userservice.services.UserService;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Управление пользователями", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Получение информации о пользователе по JWT токену",
            description = "Позволяет получить информацию о пользователе по JWT токену")
    public GetUserResponse getUser(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return userService.getUser(UUID.fromString(userId));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать пользователя в системе")
    public UserResponse register(@RequestBody UserRegistrationRequest request) {
        return userService.registerUser(request);
    }

    @PatchMapping("/{userId}/password")
    @Operation(summary = "Обновление пароля",
            description = "Позволяет обновить пароль пользователя")
    public void changePassword(@PathVariable UUID userId,
                               @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }
}
