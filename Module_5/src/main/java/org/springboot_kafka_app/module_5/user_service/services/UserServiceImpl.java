package org.springboot_kafka_app.module_5.user_service.services;

import lombok.extern.slf4j.Slf4j;
import org.springboot_kafka_app.module_5.user_service.dto.UserDTO;
import org.springboot_kafka_app.module_5.user_service.entity.User;
import org.springboot_kafka_app.module_5.user_service.producer.UserEventProducer;
import org.springboot_kafka_app.module_5.user_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService<UserDTO> {

    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userEventProducer = userEventProducer;
    }

    @Override
    public Collection<UserDTO> getAllUsers() {
        log.info("Все пользователи");
        return userRepository.findAll()
                .stream()
                .map(UserServiceImpl::mapToDto)
                .toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        log.info("Пользователь с id {}", id);
        User user = userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Пользователь с id " + id + " не найден"));
        return mapToDto(user);
    }

    @Override
    public void updateUser(Long id, UserDTO userDTO) {
        log.info("Обновление пользователя с id {}", id);
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = mapToEntity(userDTO);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Пользователь с id " + id + " не найден");
        }
    }

    @Override
    public void createUser(UserDTO userDTO) {
        User user = mapToEntity(userDTO);
        user.setCreatedAt(Instant.now());
        userEventProducer.sendMessage("CREATE|" + userDTO.getEmail());
        userRepository.save(user);
        log.info("Создание пользователя с id {}", user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Удаление пользователя с id {}", id);
        User user = userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Пользователь с id " + id + " не найден"));
        userEventProducer.sendMessage("DELETE|" + user.getEmail());
        userRepository.deleteById(user.getId());
    }

    public static User mapToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setCreatedAt(userDTO.getCreatedAt());
        return user;
    }

    public static UserDTO mapToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        userDTO.setCreatedAt(user.getCreatedAt());
        return userDTO;
    }
}
