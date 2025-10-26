package org.springboot_kafka_app.module_5.user_service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springboot_kafka_app.module_5.user_service.dto.UserDTO;
import org.springboot_kafka_app.module_5.user_service.entity.User;
import org.springboot_kafka_app.module_5.user_service.producer.UserEventProducer;
import org.springboot_kafka_app.module_5.user_service.repositories.UserRepository;
import org.springboot_kafka_app.module_5.user_service.services.UserServiceImpl;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = "user-events")
class UserServiceApplicationTests {

    private final UserRepository userRepository =
            Mockito.mock(UserRepository.class);

    private final UserEventProducer userEventProducer =
            Mockito.mock(UserEventProducer.class);

    private final UserServiceImpl userServiceImpl =
            new UserServiceImpl(userRepository, userEventProducer);

    @Test
    @DisplayName("Тестирование метода getUserById")
    public void testGetUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDTO userDTO = userServiceImpl.getUserById(userId);
        assertEquals(userId, userDTO.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Тестирование метода getAllUsers")
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);
        Collection<UserDTO> userDTOs = userServiceImpl.getAllUsers();
        assertEquals(users.size(), userDTOs.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Тестирование метода createUser")
    public void testCreateUser() {
        UserDTO userDTO = new UserDTO();
        userServiceImpl.createUser(userDTO);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Тестирование метода updateUser")
    public void testUpdateUser() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userServiceImpl.updateUser(userId, userDTO);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Тестирование метода deleteUser")
    public void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userServiceImpl.deleteUser(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).deleteById(1L);
    }
}
