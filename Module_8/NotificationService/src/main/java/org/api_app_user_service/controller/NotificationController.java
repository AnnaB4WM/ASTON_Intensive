package org.api_app_user_service.controller;

import org.api_app_user_service.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;

    @Autowired
    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@RequestParam String operation,
                                                 @RequestParam String email) {
        String subject = "Уведомление о действии на сайте";
        String text;
        if ("CREATE".equalsIgnoreCase(operation)) {
            text = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
        } else if ("DELETE".equalsIgnoreCase(operation)) {
            text = "Здравствуйте! Ваш аккаунт был удалён.";
        } else {
            return ResponseEntity.badRequest().build();
        }
        emailService.sendEmail(email, subject, text);
        return ResponseEntity.ok().build();
    }
}
