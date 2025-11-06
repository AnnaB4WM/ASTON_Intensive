package org.api_app_user_service.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api_app_user_service.services.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final EmailService emailService;

    @KafkaListener(topics = "user-events", groupId = "notification-service")
    public void onMessage(String message) {
        try {
            if (message == null || !message.contains("|")) {
                log.warn("Skip invalid message: {}", message);
                return;
            }
            String[] parts = message.split("\\|", 2);
            String op = parts[0];
            String email = parts[1];
            log.info("Received event op={}, email={}", op, email);
            if ("CREATE".equalsIgnoreCase(op)) {
                emailService.sendCreate(email);
            } else if ("DELETE".equalsIgnoreCase(op)) {
                emailService.sendDelete(email);
            } else {
                log.warn("Unknown operation: {}", op);
            }
        } catch (Exception e) {
            log.error("Failed to process message: {}", message, e);
            throw e;
        }
    }
}
