package coworkingservice.user;

import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер пользователей в системе коворкинга.
 * Этот класс предоставляет методы для регистрации новых пользователей,
 * аутентификации существующих пользователей и получения информации о пользователях.
 */
public class UserManager {

    private Map<String, User> users = new HashMap<>();

    /**
     * Регистрирует нового пользователя с указанным именем пользователя и паролем.
     *
     * @param username имя пользователя для регистрации
     * @param password пароль пользователя для регистрации
     * @return true, если пользователь успешно зарегистрирован; false, если пользователь с таким именем уже существует
     */
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password));
        return true;
    }

    /**
     * Аутентифицирует пользователя по указанному имени пользователя и паролю.
     *
     * @param username имя пользователя для аутентификации
     * @param password пароль пользователя для аутентификации
     * @return объект пользователя, если аутентификация успешна; null, если пользователь не найден или пароль неверен
     */
    public User authenticateUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.isPasswordCorrect(password)) {
            return user;
        }
        return null;
    }

    /**
     * Возвращает пользователя по его имени.
     *
     * @param username имя пользователя
     * @return объект пользователя с указанным именем, если такой найден; null, если пользователь не найден
     */
    public User getUser(String username) {
        User returned = users.get(username);
        return returned;
    }
}
