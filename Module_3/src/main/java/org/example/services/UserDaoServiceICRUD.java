package org.example.services;

import org.example.dao.UserDaoI;
import org.example.entity.User;
import java.util.Collection;

public class UserDaoServiceICRUD {

    private final UserDaoI userDao;

    public UserDaoServiceICRUD(UserDaoI userDao) {
        this.userDao = userDao;
    }

    public void save(User user) {
        userDao.save(user);
    }

    public User get(Long id) {
        return userDao.get(id);
    }

    public Collection<User> getAll() {
        return userDao.getAll();
    }

    public void update(User user) {
        userDao.update(user);
    }

    public void delete(Long id) {
        userDao.delete(id);
    }
}