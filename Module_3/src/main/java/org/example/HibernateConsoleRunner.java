package org.example;

import org.example.dao.UserDaoI;
import org.example.dao.UserDaoICRUD;
import org.example.entity.User;
import org.example.services.UserDaoServiceICRUD;
import org.example.util.HibernateUtil;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Scanner;

public class HibernateConsoleRunner {

    private static final UserDaoI userDao = new UserDaoICRUD();
    private static final UserDaoServiceICRUD UserDaoServiceICRUD = new UserDaoServiceICRUD(userDao);
    private static final Scanner scanner = new Scanner(System.in);

    static void main() {

        System.out.println("Выберите действие:");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Получить пользователя по ID");
        System.out.println("3. Получить всех пользователей");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
        while (scanner.hasNext()) {
            int console = scanner.nextInt();
            scanner.nextLine();

            switch (console) {
                case 1:
                    createUser();
                    break;
                case 2:
                    getUserById();
                    break;
                case 3:
                    getAllUsers();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    deleteUserById();
                    break;
                case 0:
                    System.out.println("Выход");
                    return;
                default:
                    System.out.println("Неверный выбор");
                    break;
            }
        }
        scanner.close();
        HibernateUtil.getSessionFactory().close();
    }


    private static void createUser() {
        System.out.println("Введите имя пользователя: ");
        String name = scanner.nextLine();

        System.out.println("Введите его email: ");
        String email = scanner.nextLine();

        System.out.println("Введите его возраст: ");
        int age = scanner.nextInt();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        user.setCreatedAt(LocalDate.now());

        UserDaoServiceICRUD.save(user);
        System.out.println("Пользователь добавлен успешно: " + user);
    }

    private static void getUserById() {
        System.out.println("Введите ID пользователя: ");
        Long id = scanner.nextLong();

        User user = UserDaoServiceICRUD.get(id);
        if (user != null) {
            System.out.println("Пользователь найден: " + user);
        } else {
            System.out.println("Пользователь с таким ID не найден");
        }
    }

    private static void getAllUsers() {
        Collection<User> users = UserDaoServiceICRUD.getAll();
        System.out.println("Список всех пользователей:");
        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void updateUser() {
        System.out.println("Введите ID пользователя: ");
        Long id = scanner.nextLong();
        User user = UserDaoServiceICRUD.get(id);
        if (user != null) {
            System.out.println("Текущие данные пользователя: " + user);
            System.out.println("Введите новое имя: ");
            String name = scanner.nextLine();

            System.out.println("Введите новый возраст: ");
            int age = scanner.nextInt();

            System.out.println("Введите новый email: ");
            String email = scanner.nextLine();

            user.setName(name);
            user.setEmail(email);
            user.setAge(age);
            user.setCreatedAt(LocalDate.now());

            UserDaoServiceICRUD.update(user);
            System.out.println("Данные пользователя обновлены успешно");
        } else {
            System.out.println("Пользователь с таким ID не найден");
        }
    }

    private static void deleteUserById() {
        System.out.println("Введите ID пользователя для удаления: ");
        Long id = scanner.nextLong();
        User user = UserDaoServiceICRUD.get(id);
        if (user != null) {
            UserDaoServiceICRUD.delete(id);
            System.out.println("Пользователь удален успешно");
        } else {
            System.out.println("Пользователь с таким ID не найден");
        }
    }
}

