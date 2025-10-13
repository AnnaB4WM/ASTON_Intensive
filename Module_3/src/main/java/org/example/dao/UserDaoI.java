package org.example.dao;

import org.example.entity.User;

import java.util.Collection;

public interface UserDaoI {

    void save(User user);

    User get(Long id);

    Collection<User> getAll();

    void update(User user);

    void delete(Long id);
}
