package ru.kolpakovee.userservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolpakovee.userservice.entities.InviteCodeEntity;
import ru.kolpakovee.userservice.exceptions.ResourceNotFoundException;
import ru.kolpakovee.userservice.records.ApartmentInfo;
import ru.kolpakovee.userservice.records.CreateInviteCodeRequest;
import ru.kolpakovee.userservice.records.InviteCodeDto;
import ru.kolpakovee.userservice.records.UseInviteCodeRequest;
import ru.kolpakovee.userservice.repositories.InviteRepository;
import ru.kolpakovee.userservice.utils.CodeGenerator;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final ApartmentUserService apartmentUserService;
    private final ApartmentService apartmentService;

    private final InviteRepository inviteRepository;

    public InviteCodeDto createInviteCode(CreateInviteCodeRequest request) {
        Long code = CodeGenerator.generate();

        InviteCodeEntity entity = new InviteCodeEntity();
        entity.setCode(code);
        entity.setApartmentId(request.apartmentId());

        return InviteCodeDto.builder()
                .code(String.format("%06d", inviteRepository.save(entity).getCode()))
                .build();
    }


    @Transactional
    public ApartmentInfo useInviteCode(String userId, UseInviteCodeRequest request) {
        InviteCodeEntity codeEntity = inviteRepository.findById(request.code())
                .orElseThrow(() -> new ResourceNotFoundException("Срок кода действия истек или он был использован"));

        UUID userIdUuid = UUID.fromString(userId);
        apartmentUserService.addToApartment(codeEntity.getApartmentId(), userIdUuid);

        inviteRepository.deleteById(request.code());

        return apartmentService.findApartment(userIdUuid);
    }
}
