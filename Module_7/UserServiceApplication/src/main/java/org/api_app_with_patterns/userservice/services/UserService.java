package org.api_app_with_patterns.userservice.services;

import java.util.Collection;

public interface UserService<T> {

    T getUserById(Long id);
    Collection<T> getAllUsers();
    void createUser(T user);
    void updateUser(Long id, T user);
    void deleteUser(Long id);
}
