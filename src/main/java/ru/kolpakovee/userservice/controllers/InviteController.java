package ru.kolpakovee.userservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.kolpakovee.userservice.records.ApartmentInfo;
import ru.kolpakovee.userservice.records.CreateInviteCodeRequest;
import ru.kolpakovee.userservice.records.InviteCodeDto;
import ru.kolpakovee.userservice.records.UseInviteCodeRequest;
import ru.kolpakovee.userservice.services.InviteService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/invite")
@RequiredArgsConstructor
@Tag(name = "Управление приглашениями", description = "API для управления приглашениями")
public class InviteController {

    private final InviteService inviteService;

    @PostMapping("/create")
    @Operation(summary = "Создание приглашения",
            description = "Позволяет создать приглашение для добавления нового соседа")
    public InviteCodeDto createInviteCode(@RequestBody CreateInviteCodeRequest request) {
        return inviteService.createInviteCode(request);
    }

    @PostMapping("/use")
    @Operation(summary = "Использование приглашения",
            description = "Позволяет использовать приглашение вступления в квартиру")
    public ApartmentInfo useInviteCode(@AuthenticationPrincipal Jwt jwt,
                                       @RequestBody UseInviteCodeRequest request) {
        String userId = jwt.getSubject();
        return inviteService.useInviteCode(userId, request);
    }
}
