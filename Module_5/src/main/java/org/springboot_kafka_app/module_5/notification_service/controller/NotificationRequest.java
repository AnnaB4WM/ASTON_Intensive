package org.springboot_kafka_app.module_5.notification_service.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    private String email;
    private String subject;
    private String text;
}
