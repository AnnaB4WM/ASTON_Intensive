package org.api_app_user_service.services;

import java.util.Collection;

public interface UserService<T> {

    T getUserById(Long id);
    Collection<T> getAllUsers();
    void createUser(T user);
    void updateUser(Long ud, T user);
    void deleteUser(Long id);
}
