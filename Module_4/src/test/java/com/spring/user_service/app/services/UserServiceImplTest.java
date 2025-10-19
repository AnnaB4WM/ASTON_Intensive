package com.spring.user_service.app.services;

import com.spring.user_service.app.dto.UserDTO;
import com.spring.user_service.app.entity.User;
import com.spring.user_service.app.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private final UserRepository userRepository =
            Mockito.mock(UserRepository.class);

    private final UserServiceImpl userServiceImpl =
            new UserServiceImpl(userRepository);

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
        Long userId = 1L;
        userServiceImpl.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
