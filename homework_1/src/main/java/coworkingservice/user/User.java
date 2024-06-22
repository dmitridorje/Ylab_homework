package coworkingservice.user;

/**
 * Представляет пользователя в системе коворкинга.
 * Класс содержит информацию о имени пользователя, пароле и роли администратора.
 */
public class User {

    private String username;
    private String password;
    private boolean isAdmin;

    private static boolean isInitialAdminSet = false;

    /**
     * Создает нового пользователя с указанным именем пользователя и паролем.
     * Первый созданный пользователь автоматически получает роль администратора.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        if (!isInitialAdminSet) {
            this.isAdmin = true;
            isInitialAdminSet = true;
        } else {
            this.isAdmin = false;
        }
    }

    /**
     * Возвращает имя пользователя.
     *
     * @return имя пользователя
     */
    public String getUsername() {
        return username;
    }

    /**
     * Проверяет корректность пароля пользователя.
     *
     * @param password пароль для проверки
     * @return true, если указанный пароль совпадает с паролем пользователя; false в противном случае
     */
    public boolean isPasswordCorrect(String password) {
        return this.password.equals(password);
    }

    /**
     * Проверяет, является ли пользователь администратором.
     *
     * @return true, если пользователь является администратором; false в противном случае
     */
    public boolean isAdmin() {
        return isAdmin;
    }
}
