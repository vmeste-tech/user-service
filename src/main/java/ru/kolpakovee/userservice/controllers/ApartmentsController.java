package ru.kolpakovee.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kolpakovee.userservice.models.apartments.*;
import ru.kolpakovee.userservice.models.users.GetUserResponse;
import ru.kolpakovee.userservice.services.ApartmentService;
import ru.kolpakovee.userservice.services.ApartmentUserService;

import java.util.List;
import java.util.UUID;

@RestController
//TODO: версия должна быть в конце или начале?
@RequestMapping("/api/apartments/v1")
@RequiredArgsConstructor
public class ApartmentsController {

    private final ApartmentService apartmentService;
    private final ApartmentUserService apartmentUserService;

    @PostMapping
    public CreateApartmentResponse createApartment(@RequestBody CreateApartmentRequest request) {
        return apartmentService.createApartment(request);
    }

    @GetMapping("/{apartmentId}")
    public GetApartmentResponse getApartment(@PathVariable UUID apartmentId) {
        return apartmentService.getApartment(apartmentId);
    }

    @PutMapping("/{apartmentId}")
    public UpdateApartmentResponse updateApartment(@PathVariable UUID apartmentId,
                                                   @RequestBody UpdateApartmentRequest request) {
        return apartmentService.updateApartment(apartmentId, request);
    }

    @DeleteMapping("/{apartmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApartment(@PathVariable UUID apartmentId) {
        apartmentService.deleteApartment(apartmentId);
    }

    @PostMapping("/{apartmentId}/users/{userId}")
    public AddToApartmentResponse addToApartment(@PathVariable UUID apartmentId, @PathVariable UUID userId) {
        return apartmentUserService.addToApartment(apartmentId, userId);
    }

    @DeleteMapping("/{apartmentId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFromApartment(@PathVariable UUID apartmentId, @PathVariable UUID userId) {
        apartmentUserService.deleteFromApartment(apartmentId, userId);
    }

    @GetMapping("/{apartmentId}/users")
    public List<GetUserResponse> getApartmentUsers(@PathVariable UUID apartmentId) {
        return apartmentUserService.getApartmentUsers(apartmentId);
    }
}
