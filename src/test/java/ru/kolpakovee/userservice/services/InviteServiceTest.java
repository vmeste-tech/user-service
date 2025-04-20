package ru.kolpakovee.userservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.kolpakovee.userservice.entities.InviteCodeEntity;
import ru.kolpakovee.userservice.producer.NotificationEventProducer;
import ru.kolpakovee.userservice.records.CreateInviteCodeRequest;
import ru.kolpakovee.userservice.records.InviteCodeDto;
import ru.kolpakovee.userservice.repositories.InviteRepository;
import ru.kolpakovee.userservice.utils.CodeGenerator;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class InviteServiceTest {

    @Mock
    private InviteRepository inviteRepository;

    @Mock
    private NotificationEventProducer producer;

    @InjectMocks
    private InviteService inviteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInviteCode() {
        UUID apartmentId = UUID.randomUUID();
        CreateInviteCodeRequest request = new CreateInviteCodeRequest(apartmentId);
        Long generatedCode = 123456L;
        InviteCodeEntity savedEntity = new InviteCodeEntity();
        savedEntity.setCode(generatedCode);
        savedEntity.setApartmentId(apartmentId);

        try (var mockedCodeGenerator = mockStatic(CodeGenerator.class)) {
            mockedCodeGenerator.when(CodeGenerator::generate).thenReturn(generatedCode);
            when(inviteRepository.save(any(InviteCodeEntity.class))).thenReturn(savedEntity);

            InviteCodeDto result = inviteService.createInviteCode(request);
            assertEquals("123456", result.code());
        }
    }
}