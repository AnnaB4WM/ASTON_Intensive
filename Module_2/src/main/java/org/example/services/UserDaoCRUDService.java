package org.example.services;


import org.example.dao.UserDaoCRUD;
import org.example.entity.User;

import java.util.Collection;

// логика консоли
public class UserDaoCRUDService {

    private static final UserDaoCRUD userDaoCRUD = new UserDaoCRUD();

    public UserDaoCRUDService() {
    }

    public void save(User user) {
        userDaoCRUD.save(user);
    }

    public User get(Long id) {
        return userDaoCRUD.get(id);
    }

    public Collection<User> getAll() {
        return userDaoCRUD.getAll();
    }

    public void update(User user) {
        userDaoCRUD.update(user);
    }

    public void delete(Long id) {
        userDaoCRUD.delete(id);
    }
}
