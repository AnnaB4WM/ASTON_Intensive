package org.examples.services;

import org.example.dao.UserDaoI;
import org.example.entity.User;
import org.example.services.UserDaoServiceICRUD;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDaoICRUDServiceTest {

    @Mock
    private UserDaoI userDaoI;

    private UserDaoServiceICRUD userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserDaoServiceICRUD(userDaoI);
    }

    @Test
    @DisplayName("Test save user")
    public void testSaveUser() {
        User user = new User(1L, "John Doe", "john.doe@example.com", 25, LocalDate.now());
        userService.save(user);
        verify(userDaoI, times(1)).save(user);
    }

    @Test
    @DisplayName("Test get user")
    public void testGetUserById() {
        User user = new User(1L, "John Doe", "john.doe@example.com", 25, LocalDate.now());
        when(userDaoI.get(1L)).thenReturn(user);
        User result = userService.get(1L);
        assertEquals(user, result);
        verify(userDaoI, times(1)).get(1L);
    }

    @Test
    @DisplayName("Test get all users")
    public void testGetAllUsers() {
        Collection<User> users = new ArrayList<>();
        User user1 = new User(1L, "John Doe", "john.doe@example.com", 25, LocalDate.now());
        User user2 = new User(2L, "Jane Doe", "jane.doe@example.com", 21, LocalDate.now());
        users.add(user1);
        users.add(user2);
        when(userDaoI.getAll()).thenReturn(users);
        Collection<User> result = userService.getAll();

        // Print the result for debugging
        System.out.println("Expected: " + users);
        System.out.println("Actual: " + result);

        assertEquals(users, result);
        verify(userDaoI, times(1)).getAll();
    }

    @Test
    @DisplayName("Test update user")
    public void testUpdateUser() {
        User user = new User(1L, "John Doe", "john.doe@example.com", 25, LocalDate.now());
        when(userDaoI.get(1L)).thenReturn(user);
        user.setName("Jane Doe");
        userService.update(user);
        verify(userDaoI, times(1)).update(user);
        User result = userService.get(1L);
        assertEquals("Jane Doe", result.getName());
        verify(userDaoI, times(1)).get(1L);
    }

    @Test
    @DisplayName("Test delete user")
    public void testDeleteUser() {
        User user = new User(1L, "John Doe", "john.doe@example.com", 25, LocalDate.now());
        userService.save(user);
        verify(userDaoI, times(1)).save(user);
        userService.delete(1L);
        verify(userDaoI, times(1)).delete(1L);
    }
}