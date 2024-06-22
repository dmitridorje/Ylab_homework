package coworkingservice.util;

import coworkingservice.booking.BookingManager;
import coworkingservice.resource.ResourceType;
import coworkingservice.user.UserManager;
import coworkingservice.resource.ResourceManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс для создания исходных данных для работы приложения.
 */
public class DataInitializer {

    public static void initializeData(ResourceManager resourceManager, UserManager userManager, BookingManager bookingManager) {

        // Создание администратора
        userManager.registerUser("admin", "admin");

        //Создание пользователей
        userManager.registerUser("John Doe", "123456");
        userManager.registerUser("Kate Smith", "123456");

        // Создание ресурсов
        resourceManager.addResource("Рабочая станция 42", ResourceType.WORKSPACE);
        resourceManager.addResource("Конференц-зал А", ResourceType.CONFERENCE_ROOM);
        resourceManager.addResource("Рабочее место 84", ResourceType.WORKSPACE);
        resourceManager.addResource("Переговорная", ResourceType.CONFERENCE_ROOM);

        // Создание бронирований
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        bookingManager.addBooking(resourceManager.getResource(1).get(), userManager.getUser("John Doe"),
                LocalDateTime.parse("2024-06-22 12:00", formatter), LocalDateTime.parse("2024-06-22 13:00", formatter));
        bookingManager.addBooking(resourceManager.getResource(2).get(), userManager.getUser("John Doe"),
                LocalDateTime.parse("2024-06-22 14:14", formatter), LocalDateTime.parse("2024-06-22 15:15", formatter));
        bookingManager.addBooking(resourceManager.getResource(3).get(), userManager.getUser("Kate Smith"),
                LocalDateTime.parse("2023-06-22 18:42", formatter), LocalDateTime.parse("2023-06-22 18:59", formatter));
    }
}
