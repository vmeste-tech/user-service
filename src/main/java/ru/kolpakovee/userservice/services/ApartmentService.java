package ru.kolpakovee.userservice.services;

import jakarta.transaction.Transactional;
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
    private final ApartmentUserRepository apartmentUserRepository;

    public CreateApartmentResponse createApartment(CreateApartmentRequest request) {
        ApartmentEntity apartment = createApartmentEntity(request);

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

    @Transactional
    public UpdateApartmentResponse updateApartment(UUID apartmentId, UpdateApartmentRequest request) {
        ApartmentEntity existedApartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new NotFoundException("Квартира не существует."));

        existedApartment.setAddress(request.getAddress());
        existedApartment.setName(request.getName());

        existedApartment = apartmentRepository.save(existedApartment);

        return UpdateApartmentResponse.builder()
                .id(existedApartment.getId())
                .address(existedApartment.getAddress())
                .name(existedApartment.getName())
                .build();
    }

    @Transactional
    public void deleteApartment(UUID apartmentId) {
        apartmentUserRepository.deleteAllByIdApartmentId(apartmentId);
        apartmentRepository.deleteById(apartmentId);
    }

    private GetApartmentResponse mapToGetApartmentResponse(ApartmentEntity apartment) {
        return GetApartmentResponse.builder()
                .address(apartment.getAddress())
                .name(apartment.getName())
                .build();
    }

    private ApartmentEntity createApartmentEntity(CreateApartmentRequest request) {
        ApartmentEntity newApartment = new ApartmentEntity();
        newApartment.setName(request.getName());
        newApartment.setAddress(request.getAddress());

        return apartmentRepository.save(newApartment);
    }
}
