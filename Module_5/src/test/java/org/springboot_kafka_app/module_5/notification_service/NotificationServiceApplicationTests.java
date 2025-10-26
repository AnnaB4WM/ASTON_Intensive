package org.springboot_kafka_app.module_5.notification_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springboot_kafka_app.module_5.notification_service.consumer.UserEventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {"user-events"})
public class NotificationServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JavaMailSender mailSender;

    @MockitoBean
    private UserEventConsumer userEventConsumer;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        // Инициализация моков перед каждым тестом
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        // Очистка моков перед каждым тестом
        reset(mailSender, userEventConsumer);
    }

    @Test
    public void testSendNotification() throws Exception {
        // Подготовка данных
        String email = "test@example.com";
        String subject = "Тестовое уведомление";
        String text = "Это тестовое сообщение.";

        // Вызов метода через mockMvc
        mockMvc.perform(post("/api/notifications/send")
                        .param("email", email)
                        .param("subject", subject)
                        .param("text", text))
                .andExpect(status().isOk());

        // Проверка, что метод receive вызывается один раз с правильным параметром email
        verify(userEventConsumer, times(1)).receive(eq("CREATE|" + email));

        // Проверка, что метод send был вызван один раз с любым SimpleMailMessage
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

        // Проверка, что созданный SimpleMailMessage имеет правильные параметры
        ArgumentCaptor<SimpleMailMessage> argument = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(argument.capture());

        SimpleMailMessage mailMessage = argument.getValue();
        assertNotNull(mailMessage.getTo());
        assertEquals(email, mailMessage.getTo()[0]);
        assertEquals(subject, mailMessage.getSubject());
        assertEquals(text, mailMessage.getText());
    }
}
