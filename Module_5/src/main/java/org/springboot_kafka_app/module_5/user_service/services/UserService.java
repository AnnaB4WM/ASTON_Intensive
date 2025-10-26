package org.springboot_kafka_app.module_5.user_service.services;

import java.util.Collection;

public interface UserService<T> {

    T getUserById(Long id);
    Collection<T> getAllUsers();
    void createUser(T user);
    void updateUser(Long ud, T user);
    void deleteUser(Long id);
}
