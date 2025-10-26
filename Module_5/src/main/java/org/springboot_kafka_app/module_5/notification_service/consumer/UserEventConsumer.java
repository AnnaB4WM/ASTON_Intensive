package org.springboot_kafka_app.module_5.notification_service.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

// Класс для получения сообщений из Kafka и отправки уведомлений по электронной почте
@Service
public class UserEventConsumer {

    private final JavaMailSender mailSender;

    @Autowired
    public UserEventConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void receive(String message) {
        // Разделяем сообщение на операцию и email пользователя
        String[] parts = message.split("\\|");
        String operation = parts[0];
        String email = parts[1];

        // Создаем сообщение для отправки по электронной почте
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Уведомление о действии на сайте");

        // Определяем текст сообщения в зависимости от операции
        if ("CREATE".equals(operation)) {
            mailMessage.setText("Здравствуйте! Ваш аккаунт на сайте был успешно создан.");
        } else if ("DELETE".equals(operation)) {
            mailMessage.setText("Здравствуйте! Ваш аккаунт был удалён.");
        }
        // Отправляем сообщение
        mailSender.send(mailMessage);
    }
}
