package coworkingservice.user;

import coworkingservice.booking.BookingManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты для класса операций с пользователями {@link BookingManager}
 */
class UserManagerTest {
    private UserManager userManager;

    @BeforeEach
    public void setUp() {

        userManager = new UserManager();
        userManager.registerUser("user1", "password1");
        userManager.registerUser("user2", "password2");
    }

    @Test
    @DisplayName("Регистрация пользователя")
    public void testRegisterUser() {
        boolean result = userManager.registerUser("testUser", "password");
        assertTrue(result);
    }

    @Test
    @DisplayName("Регистрация дубликата пользователя")
    public void testRegisterDuplicateUser() {
        userManager.registerUser("testUser", "password");
        boolean result = userManager.registerUser("testUser", "newPassword");
        assertFalse(result);
    }

    @Test
    @DisplayName("Аутентификация пользователя")
    public void testAuthenticateUser() {
        userManager.registerUser("testUser", "password");
        User user = userManager.authenticateUser("testUser", "password");
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
    }

    @Test
    @DisplayName("Аутентификация с неверным паролем")
    public void testAuthenticateUserWithWrongPassword() {
        userManager.registerUser("testUser", "password");
        User user = userManager.authenticateUser("testUser", "wrongPassword");
        assertNull(user);
    }

    @Test
    @DisplayName("Аутентификация несуществующего пользователя")
    public void testAuthenticateNonExistentUser() {
        User user = userManager.authenticateUser("nonExistentUser", "password");
        assertNull(user);
    }

    @Test
    @DisplayName("Получение существующего пользователя")
    public void testGetUser_UserExists() {
        User user = userManager.getUser("user1");
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
    }

    @Test
    @DisplayName("Получение несуществующего пользователя")
    public void testGetUser_UserDoesNotExist() {
        User user = userManager.getUser("nonexistent");
        assertNull(user);
    }
}
