package org.example.dao;

import java.util.Collection;

public interface UserDao<T> {

    void save(T user);

    T get(Long id);

    Collection<T> getAll();

    void update(T user);

    void delete(Long id);
}