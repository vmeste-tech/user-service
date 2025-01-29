package ru.kolpakovee.userservice.services;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolpakovee.userservice.entities.ApartmentEntity;
import ru.kolpakovee.userservice.models.apartments.*;
import ru.kolpakovee.userservice.repositories.ApartmentRepository;
import ru.kolpakovee.userservice.repositories.ApartmentUserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;

    public CreateApartmentResponse createApartment(CreateApartmentRequest request) {
        ApartmentEntity newApartment = new ApartmentEntity(UUID.randomUUID(), request.getName(), request.getAddress());
        ApartmentEntity apartment = apartmentRepository.save(newApartment);

        return CreateApartmentResponse.builder()
                .apartmentId(apartment.getId())
                .name(apartment.getName())
                .address(apartment.getAddress())
                .build();
    }

    public GetApartmentResponse getApartment(UUID apartmentId) {
        return apartmentRepository.findById(apartmentId)
                .map(this::mapToGetApartmentResponse)
                .orElseThrow(() -> new NotFoundException("Квартира не найдена."));
    }

    public UpdateApartmentResponse updateApartment(UUID apartmentId, UpdateApartmentRequest request) {
        ApartmentEntity existedApartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new NotFoundException("Квартира не существует."));

        ApartmentEntity newApartment =
                new ApartmentEntity(existedApartment.getId(), request.getName(), request.getAddress());

        newApartment = apartmentRepository.save(newApartment);

        return UpdateApartmentResponse.builder()
                .id(newApartment.getId())
                .address(newApartment.getAddress())
                .name(newApartment.getName())
                .build();
    }

    public void deleteApartment(UUID apartmentId) {
        apartmentRepository.deleteById(apartmentId);
    }

    private GetApartmentResponse mapToGetApartmentResponse(ApartmentEntity apartment) {
        return GetApartmentResponse.builder()
                .address(apartment.getAddress())
                .name(apartment.getName())
                .build();
    }
}
