package org.examples.dao;

import org.example.dao.UserDaoICRUD;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.hibernate.cfg.Configuration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoICRUDTest {

    @Container
    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer("postgres:latest")
                    .withDatabaseName("postgres")
                    .withUsername("postgres")
                    .withPassword("pass");

    private SessionFactory sessionFactory;
    private UserDaoICRUD userDao =  new UserDaoICRUD();

    @BeforeEach
    public void setUp() {
        sessionFactory = new Configuration()
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url",postgres.getJdbcUrl())
                .setProperty("hibernate.connection.username", postgres.getUsername())
                .setProperty("hibernate.connection.password", postgres.getPassword())
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
        HibernateUtil.setSessionFactory(sessionFactory);
    }

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    @Test
    @DisplayName("Test save user")
    public void testSaveUser() {
        User user = new User(1L, "John Doe", "john.doe@example.com", 25, LocalDate.now());
        userDao.save(user);
        User savedUser = userDao.get(user.getId());
        assertEquals(user, savedUser);
    }

    @Test
    @DisplayName("Test get user")
    public void testGetUserById() {
        User user = new User(1L, "John Doe", "john.doe@example.com", 25, LocalDate.now());
        userDao.save(user);
        User getUser = userDao.get(user.getId());
        assertEquals(user, getUser);
    }

    @Test
    @DisplayName("Test get all users")
    public void testGetAllUsers() {
        User user1 = new User(1L, "John Doe", "john.doe@example.com", 25, LocalDate.now());
        User user2 = new User(2L, "Jane Doe", "jane.doe@example.com", 21, LocalDate.now());
        userDao.save(user1);
        userDao.save(user2);
        Collection<User> allUsers = userDao.getAll();
        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
    }

    @Test
    @DisplayName("Test update user")
    public void testUpdateUser() {
        User user = new User(1L, "John Doe", "john.doe@example.com", 25, LocalDate.now());
        userDao.save(user);
        user.setName("Jane Doe");
        userDao.update(user);
        User updatedUser = userDao.get(user.getId());
        assertEquals(user, updatedUser);
    }

    @Test
    @DisplayName("Test delete user")
    public void testDeleteUser() {
        User user = new User(1L, "John Doe", "john.doe@example.com", 25, LocalDate.now());
        userDao.save(user);
        userDao.delete(user.getId());
        User deletedUser = userDao.get(user.getId());
        assertNull(deletedUser);
    }
}
