package ru.kolpakovee.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kolpakovee.userservice.models.apartments.ChangePasswordRequest;
import ru.kolpakovee.userservice.models.users.GetUserResponse;
import ru.kolpakovee.userservice.records.UserRegistrationRequest;
import ru.kolpakovee.userservice.records.UserResponse;
import ru.kolpakovee.userservice.services.UserService;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public GetUserResponse getUser(@PathVariable UUID userId) {
        return userService.getUser(userId);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRegistrationRequest request) {
        return userService.registerUser(request);
    }

    @PatchMapping("/{userId}/password")
    public void changePassword(@PathVariable UUID userId,
                               @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request);
    }
}
