package ru.kolpakovee.userservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.kolpakovee.userservice.records.UpdateUserProfileRequest;
import ru.kolpakovee.userservice.models.apartments.ChangePasswordRequest;
import ru.kolpakovee.userservice.records.GetUserResponse;
import ru.kolpakovee.userservice.records.UserRegistrationRequest;
import ru.kolpakovee.userservice.records.UserResponse;
import ru.kolpakovee.userservice.services.UserService;
import ru.kolpakovee.userservice.utils.JwtUtils;

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

    @PatchMapping("/profile")
    @Operation(summary = "Обновление профиля пользователя",
            description = "Позволяет обновить имя, фамилию, почту и фото профиля пользователя (фото в формате base64)")
    public GetUserResponse updateProfile(@AuthenticationPrincipal Jwt jwt,
                                  @RequestBody UpdateUserProfileRequest request) {
        return userService.updateUserProfile(JwtUtils.getUserId(jwt), request);
    }

    @PatchMapping("/password")
    @Operation(summary = "Обновление пароля",
            description = "Позволяет обновить пароль пользователя")
    public void changePassword(@AuthenticationPrincipal Jwt jwt,
                               @RequestBody ChangePasswordRequest request) {
        userService.changePassword(JwtUtils.getUserId(jwt), request);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }
}
