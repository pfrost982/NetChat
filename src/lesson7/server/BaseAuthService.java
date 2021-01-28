package lesson7.server;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {

    private static final List<User> users = List.of(
            new User("log1", "111", "user1"),
            new User("log2", "222", "user2"),
            new User("log3", "333", "user3")
    );

    @Override
    public String getNickByLoginPass(String login, String pass) {
        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPassword().equals(pass)) return user.getUsername();
        }
        return null;
    }

    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен");
    }

    @Override
    public void stop() {
        System.out.println("Сервис аутентификации остановлен");
    }
}
