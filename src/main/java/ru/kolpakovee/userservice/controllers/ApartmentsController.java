package ru.kolpakovee.userservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.kolpakovee.userservice.models.apartments.*;
import ru.kolpakovee.userservice.records.GetUserResponse;
import ru.kolpakovee.userservice.records.ApartmentInfo;
import ru.kolpakovee.userservice.services.ApartmentService;
import ru.kolpakovee.userservice.services.ApartmentUserService;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/apartments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Управление квартирой", description = "API для управления квартирой")
public class ApartmentsController {

    private final ApartmentService apartmentService;
    private final ApartmentUserService apartmentUserService;

    @GetMapping("/by-user")
    @Operation(summary = "Получение квартиры по JWT токену пользователя",
            description = "Позволяет получить квартиру по JWT токену пользователя")
    public ApartmentInfo findApartmentByUser(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return apartmentService.findApartment(UUID.fromString(userId));
    }

    @GetMapping("/{apartmentId}")
    @Operation(summary = "Получение квартиры идентификатору",
            description = "Позволяет получить квартиру по JWT токену пользователя")
    public GetApartmentResponse getApartment(@PathVariable UUID apartmentId) {
        return apartmentService.getApartment(apartmentId);
    }

    @GetMapping("/{apartmentId}/users")
    @Operation(summary = "Получение пользователей, проживающих в квартире",
            description = "Позволяет получить пользователей, проживающих в квартире")
    public List<GetUserResponse> getApartmentUsers(@PathVariable UUID apartmentId) {
        return apartmentUserService.getApartmentUsers(apartmentId);
    }

    @PostMapping
    @Operation(summary = "Создание квартиры",
            description = "Позволяет создать квартиру")
    public CreateApartmentResponse createApartment(@RequestBody CreateApartmentRequest request,
                                                   @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return apartmentService.createApartment(request, UUID.fromString(userId));
    }

    @PostMapping("/{apartmentId}/users/{userId}")
    @Operation(summary = "Добавление пользователя в квартиру",
            description = "Позволяет добавить пользователя в квартиру по идентификатору")
    public AddToApartmentResponse addToApartment(@PathVariable UUID apartmentId, @PathVariable UUID userId) {
        return apartmentUserService.addToApartment(apartmentId, userId);
    }

    @PutMapping("/{apartmentId}")
    @Operation(summary = "Обновление квартиры",
            description = "Позволяет обновить квартиру")
    public UpdateApartmentResponse updateApartment(@PathVariable UUID apartmentId,
                                                   @RequestBody UpdateApartmentRequest request) {
        return apartmentService.updateApartment(apartmentId, request);
    }

    @DeleteMapping("/{apartmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление квартиры",
            description = "Позволяет удалить квартиру")
    public void deleteApartment(@PathVariable UUID apartmentId) {
        apartmentService.deleteApartment(apartmentId);
    }

    @DeleteMapping("/{apartmentId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление пользователя из квартиры",
            description = "Позволяет удалить пользователя из квартиры")
    public void deleteFromApartment(@PathVariable UUID apartmentId, @PathVariable UUID userId) {
        apartmentUserService.deleteFromApartment(apartmentId, userId);
    }
}
