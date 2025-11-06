package org.api_app_user_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String email, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
    }

    public void sendCreate(String email) {
        sendEmail(email, "Уведомление о действии на сайте", "Здравствуйте! Ваш аккаунт на сайте был успешно создан.");
    }

    public void sendDelete(String email) {
        sendEmail(email, "Уведомление о действии на сайте", "Здравствуйте! Ваш аккаунт был удалён.");
    }
}
