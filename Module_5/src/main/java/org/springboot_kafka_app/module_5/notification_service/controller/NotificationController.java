package org.springboot_kafka_app.module_5.notification_service.controller;

import org.springboot_kafka_app.module_5.notification_service.consumer.UserEventConsumer;
import org.springboot_kafka_app.module_5.notification_service.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Класс для отправки уведомлений напрямую через API
@RestController
@RequestMapping("/api/notifications")

public class NotificationController {

    private final EmailService emailService;
    private final UserEventConsumer userEventConsumer;

    @Autowired
    public NotificationController(EmailService emailService, UserEventConsumer userEventConsumer) {
        this.emailService = emailService;
        this.userEventConsumer = userEventConsumer;
    }

    @PostMapping("/send")
    public void sendNotification(@RequestBody NotificationRequest notificationRequest) {
        emailService.sendEmail(notificationRequest.getEmail(), notificationRequest.getSubject(), notificationRequest.getText());
        userEventConsumer.receive("CREATE|" + notificationRequest.getEmail());
    }
}
