package org.api_app_user_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {"user-events"})
public class NotificationServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @DynamicPropertySource
    static void kafkaProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> System.getProperty("spring.embedded.kafka.brokers"));
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        reset(mailSender);
    }

    @Test
    public void testSendNotification_viaRest() throws Exception {
        String email = "test@example.com";
        String operation = "CREATE";

        mockMvc.perform(post("/api/notifications/send")
                        .param("operation", operation)
                        .param("email", email))
                .andExpect(status().isOk());

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

        ArgumentCaptor<SimpleMailMessage> argument = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(argument.capture());

        SimpleMailMessage mailMessage = argument.getValue();
        assertNotNull(mailMessage.getTo());
        assertEquals(email, mailMessage.getTo()[0]);
        assertEquals("Уведомление о действии на сайте", mailMessage.getSubject());
        assertEquals("Здравствуйте! Ваш аккаунт на сайте был успешно создан.", mailMessage.getText());
    }

    @Test
    public void testSendNotification_viaKafkaConsumer() throws Exception {
        String email = "kafka@example.com";
        String payload = "DELETE|" + email;

        kafkaTemplate.send("user-events", payload).get();

        // Give the listener a moment to process
        Thread.sleep(1000);

        ArgumentCaptor<SimpleMailMessage> argument = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, atLeastOnce()).send(argument.capture());

        SimpleMailMessage sent = argument.getValue();
        assertNotNull(sent.getTo());
        assertEquals(email, sent.getTo()[0]);
        assertEquals("Уведомление о действии на сайте", sent.getSubject());
        assertEquals("Здравствуйте! Ваш аккаунт был удалён.", sent.getText());
    }
}
