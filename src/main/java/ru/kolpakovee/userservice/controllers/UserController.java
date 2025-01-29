package ru.kolpakovee.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.kolpakovee.userservice.models.apartments.ChangePasswordRequest;
import ru.kolpakovee.userservice.models.users.GetUserResponse;
import ru.kolpakovee.userservice.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public GetUserResponse getUser(@PathVariable UUID userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/{userId}/password")
    public void changePassword(@PathVariable UUID userId,
                               @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request);
    }
}
