package org.api_app_user_service.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class UserEventConsumer {

    private final JavaMailSender mailSender;

    @Autowired
    public UserEventConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void receive(String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        String[] parts = message.split("\\|");
        if (parts.length < 2) {
            return;
        }
        String operation = parts[0].trim();
        String email = parts[1].trim();
        if (email.isEmpty()) {
            return;
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Уведомление о действии на сайте");

        if ("CREATE".equalsIgnoreCase(operation)) {
            mailMessage.setText("Здравствуйте! Ваш аккаунт на сайте был успешно создан.");
        } else if ("DELETE".equalsIgnoreCase(operation)) {
            mailMessage.setText("Здравствуйте! Ваш аккаунт был удалён.");
        } else {
            return;
        }
        mailSender.send(mailMessage);
    }
}
