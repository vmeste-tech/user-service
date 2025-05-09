package ru.kolpakovee.userservice.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.kolpakovee.userservice.enums.NotificationCategory;
import ru.kolpakovee.userservice.records.NotificationEvent;
import ru.kolpakovee.userservice.repositories.ApartmentUserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationEventProducer {

    @Value("${spring.kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    private final ApartmentUserRepository apartmentUserRepository;

    public void send(UUID userId, String message) {
        NotificationEvent event = NotificationEvent.builder()
                .category(NotificationCategory.USER)
                .message(message)
                .userId(userId)
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(topic, event);
    }

    public void sendToAllApartmentUsers(UUID apartmentId, String message) {
        apartmentUserRepository.findAllByIdApartmentId(apartmentId).stream()
                .map(user -> NotificationEvent.builder()
                        .category(NotificationCategory.USER)
                        .message(message)
                        .userId(user.getId().getUserId())
                        .timestamp(LocalDateTime.now())
                        .build())
                .forEach(event -> kafkaTemplate.send(topic, event));
    }
}
