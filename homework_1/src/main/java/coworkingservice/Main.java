package coworkingservice;

import coworkingservice.booking.Booking;
import coworkingservice.booking.BookingManager;
import coworkingservice.resource.ResourceType;
import coworkingservice.user.User;
import coworkingservice.user.UserManager;
import coworkingservice.resource.Resource;
import coworkingservice.resource.ResourceManager;
import coworkingservice.util.DataInitializer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final UserManager userManager = new UserManager();
    private static final ResourceManager resourceManager = new ResourceManager();
    private static final BookingManager bookingManager = new BookingManager();
    private static final Scanner scanner = new Scanner(System.in);
    private static User loggedInUser;
    private static final DateTimeFormatter withoutHours = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter hoursOnly = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter completeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {

        DataInitializer.initializeData(resourceManager, userManager, bookingManager);

        System.out.println("Добро пожаловать в систему управления коворкинг-пространством!");
        System.out.println("+--------------------------+");

        while (true) {
            if (loggedInUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showLoginMenu() {
        while (loggedInUser == null) {
            System.out.println("1. Регистрация пользователя");
            System.out.println("2. Залогиниться");
            System.out.println("3. Выйти из системы");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> register();
                    case 2 -> login();
                    case 3 -> System.exit(0);
                    default -> System.out.println("Неверный выбор, попробуйте еще раз.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Неверный ввод! Пожалуйста, введите число.");
                scanner.nextLine();
            }
        }
    }

    private static void register() {
        System.out.println("Введите имя пользователя:");
        String username = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Имя пользователя или пароль не могут быть пустыми. Попробуйте ещё раз!");
            System.out.println();
            register();
            return;
        }

        if (userManager.registerUser(username, password)) {
            System.out.println("Регистрация завершена успешно!");
            System.out.println();
        } else {
            System.out.println("Пользователь с таким именем уже существует. Попробуйте ещё раз!");
            System.out.println();
        }
    }

    private static void login() {
        System.out.println("Введите имя пользователя:");
        String username = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();

        User user = userManager.authenticateUser(username, password);
        if (user != null) {
            loggedInUser = user;
            System.out.println("Успешный вход!");
            System.out.println();
        } else {
            System.out.println("Неверное имя пользователя или пароль. Попробуйте ещё раз!");
            System.out.println();
        }
    }

    private static void showMainMenu() {
        System.out.println("1. Просмотр всех доступных рабочих мест и конференц-залов");
        System.out.println("2. Бронирование");
        System.out.println("3. Просмотр моих бронирований");
        System.out.println("4. Отмена бронирования");
        System.out.println("5. Просмотр всех бронирований (админ)");
        System.out.println("6. Управление ресурсами (админ)");
        System.out.println("7. Разлогиниться");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> viewAllResources();
                case 2 -> bookResource();
                case 3 -> viewMyBookings();
                case 4 -> cancelBooking();
                case 5 -> {
                    if (loggedInUser.isAdmin()) {
                        viewAllBookings();
                    } else {
                        System.out.println("Нет доступа.");
                    }
                }
                case 6 -> {
                    if (loggedInUser.isAdmin()) {
                        manageResources();
                    } else {
                        System.out.println("Нет доступа.");
                    }
                }
                case 7 -> loggedInUser = null;
                default -> System.out.println("Неверный выбор, попробуйте еще раз.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Неверный ввод! Пожалуйста, введите число.");
            scanner.nextLine();
        }
    }

    private static void manageResources() {
        viewAllResources();
        System.out.println("------------------");
        System.out.println("1. Добавить ресурс");
        System.out.println("2. Удалить ресурс");
        System.out.println("3. Обновить ресурс");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> addResource();
            case 2 -> deleteResource();
            case 3 -> updateResource();
            default -> System.out.println("Неверный выбор, попробуйте еще раз.");
        }
    }

    private static void viewMyBookings() {
        displayBookings(bookingManager.getBookingsByUser(loggedInUser));
    }

    public static void updateResource() {
        System.out.println("Введите ID ресурса для обновления:");
        int resourceId = scanner.nextInt();
        scanner.nextLine();

        Optional<Resource> optionalResource = resourceManager.getResource(resourceId);
        if (optionalResource.isEmpty()) {
            System.out.println("Ресурс не найден, попробуйте снова.");
            return;
        }

        Resource resource = optionalResource.get();
        System.out.println("Ресурс найден: " + resource.getName() + ", " + resource.getType());
        System.out.println("Что вы хотите обновить?");
        System.out.println("1. Имя ресурса");
        System.out.println("2. Тип ресурса");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                while (true) {
                    System.out.println("Введите новое имя ресурса:");
                    String newName = scanner.nextLine().trim();

                    if (newName.isBlank()) {
                        System.out.println("Имя ресурса не может быть пустым, попробуйте снова.");
                        continue;
                    }

                    boolean nameExists = resourceManager.getAllResources().values().stream()
                            .anyMatch(r -> r.getName().equals(newName));

                    if (nameExists) {
                        System.out.println("Ресурс с таким именем уже существует, попробуйте другое имя.");
                    } else {
                        resourceManager.updateResource(resource.getId(), newName, resource.getType());
                        System.out.println("Имя ресурса обновлено на: " + newName);
                        break;
                    }
                }
            }
            case 2 -> {
                boolean validInput = false;
                String newType = null;

                while (!validInput) {
                    System.out.println("Введите новый тип ресурса (р = рабочее место, к = конференц-зал):");
                    String input = scanner.nextLine().toLowerCase();

                    if (input.equals("р")) {
                        newType = "WORKSPACE";
                        validInput = true;
                    } else if (input.equals("к")) {
                        newType = "CONFERENCE_ROOM";
                        validInput = true;
                    } else {
                        System.out.println("Неверный ввод.");
                    }
                }

                try {
                    resourceManager.updateResource(resource.getId(), resource.getName(), ResourceType.valueOf(newType));
                    System.out.println("Тип ресурса обновлен на: " + ResourceType.valueOf(newType));
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный тип ресурса. Пожалуйста, введите WORKSPACE или CONFERENCE_ROOM.");
                }
            }
            default -> System.out.println("Неверный выбор, попробуйте еще раз.");
        }
    }

    private static void addResource() {
        while (true) {
            System.out.println("Введите имя ресурса:");
            String name = scanner.nextLine().trim();

            if (name.isBlank()) {
                System.out.println("Имя ресурса не может быть пустым, попробуйте снова.");
                continue;
            }

            boolean nameExists = resourceManager.getAllResources().values().stream()
                    .anyMatch(resource -> resource.getName().equals(name));

            if (nameExists) {
                System.out.println("Ресурс с таким именем уже существует, попробуйте другое имя.");
            } else {
                String type = null;
                while (type == null) {
                    System.out.println("Введите тип ресурса (Р - Рабочее место, К - Конференц-зал):");
                    String typeInput = scanner.nextLine().trim().toUpperCase();

                    if (typeInput.isBlank()) {
                        System.out.println("Тип ресурса не может быть пустым, попробуйте снова.");
                        continue;
                    }

                    try {
                        switch (typeInput) {
                            case "Р":
                                type = "WORKSPACE";
                                break;
                            case "К":
                                type = "CONFERENCE_ROOM";
                                break;
                            default:
                                System.out.println("Неверный тип ресурса. Пожалуйста, введите Р или К.");
                                type = null;
                                break;
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверный тип ресурса. Пожалуйста, введите WORKSPACE или CONFERENCE_ROOM.");
                    }
                }

                resourceManager.addResource(name, ResourceType.valueOf(type));
                System.out.println("Ресурс добавлен: " + name + ", " + ResourceType.valueOf(type));
                break;
            }
        }
    }

    private static void deleteResource() {
        while (true) {
            viewAllResources();
            System.out.println("Введите ID ресурса для удаления:");

            int id;
            try {
                id = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Неверный ввод! Пожалуйста, введите числовой ID.");
                scanner.nextLine();
                continue;
            }

            if (resourceManager.deleteResource(id)) {
                System.out.println("Ресурс удален.");
                break;
            } else {
                System.out.println("Ресурс не найден. Попробуйте снова.");
            }
        }
    }

    private static void viewAllResources() {
        Map<Long, Resource> resources = resourceManager.getAllResources();
        System.out.println("Список всех доступных рабочих мест и конференц-залов:");
        System.out.println("+----+-------------------------------+-------------------+");
        System.out.println("| ID |            Название           |        Тип        |");
        System.out.println("+----+-------------------------------+-------------------+");

        for (Map.Entry resource : resources.entrySet()) {
            String idStr = String.format("| %2s |", resource.getKey());
            String resourceNameStr = String.format(" %-29s |", ((Resource) resource.getValue()).getName());
            String resourceTypeStr = String.format(" %-17s |", ((Resource) resource.getValue()).getType());

            System.out.println(idStr + resourceNameStr + resourceTypeStr);
        }
        System.out.println("+----+-------------------------------+-------------------+");
        System.out.println();
    }

    private static void cancelBooking() {
        while (true) {
            viewMyBookings();
            System.out.println("Введите ID бронирования для отмены:");
            int bookingId;

            try {
                bookingId = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Неверный ввод! Пожалуйста, введите числовой ID.");
                scanner.nextLine();
                continue;
            }

            if (bookingManager.deleteBooking(bookingId)) {
                System.out.println("Бронирование отменено.");
                break;
            } else {
                System.out.println("Бронирование не найдено, попробуйте снова.");
            }
        }
    }

    private static void viewAllBookings() {

        System.out.println("1. Фильтровать по дате");
        System.out.println("2. Фильтровать по пользователю");
        System.out.println("3. Фильтровать по ресурсу");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> viewByDate();
            case 2 -> viewByUser();
            case 3 -> viewByResource();
            default -> System.out.println("Неверный выбор, попробуйте еще раз.");
        }
    }

    private static void viewByDate() {
        displayBookings(bookingManager.filterBookingsByDate());
    }

    private static void viewByUser() {
        displayBookings(bookingManager.filterBookingsByUser());
    }

    private static void viewByResource() {
        displayBookings(bookingManager.filterBookingsByResource());
    }

    private static void displayBookings(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            System.out.println("Нет активных бронирований.");
            System.out.println("+------------------------+");
        } else {
            System.out.println("+----+-------------------------+-------------------+------------------+-------------------+------------------+");
            System.out.println("| ID |     Название ресурса    |        Тип        |     Заказчик     |    Дата начала    |  Дата окончания  |");
            System.out.println("+----+-------------------------+-------------------+------------------+-------------------+------------------+");

            for (Booking booking : bookings) {
                String idStr = String.format("| %2s |", booking.getId());
                String resourceNameStr = String.format(" %-23s |", booking.getResource().getName());
                String resourceTypeStr = String.format(" %-17s |", booking.getResource().getType());
                String userStr = String.format(" %-16s |", booking.getUser().getUsername());
                String bookingStartStr = String.format(" %-17s |", booking.getStartTimeFormatted());
                String bookingEndStr = String.format(" %-16s |", booking.getEndTimeFormatted());

                System.out.println(idStr + resourceNameStr + resourceTypeStr + userStr + bookingStartStr + bookingEndStr);
            }
            System.out.println("+----+-------------------------+-------------------+------------------+-------------------+------------------+");

        }
    }

    public static void bookResource() {
        int resourceId = 0;
        Optional<Resource> resource = Optional.empty();
        while (resource.isEmpty()) {
            while (resourceId == 0) {
                System.out.println("Введите ID ресурса для бронирования:");
                try {
                    resourceId = scanner.nextInt();
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Неверный ввод! Пожалуйста, введите число.");
                    scanner.nextLine();
                }
            }

            resource = resourceManager.getResource(resourceId);
            if (resource.isEmpty()) {
                System.out.println("Ресурс не найден, попробуйте снова.");
                resourceId = 0;
            } else {
                System.out.println("Вы желаете забронировать ресурс \'" + resource.get().getName() + "\'.");
            }
        }

        LocalDate today = LocalDate.now();
        LocalDate parsedBookingDate = null;

        while (parsedBookingDate == null || parsedBookingDate.isBefore(today)) {
            System.out.println("На какую дату вы хотите забронировать ресурс? (Введите дату в формате гггг-мм-дд)");
            String bookingDate = scanner.nextLine();

            try {
                parsedBookingDate = LocalDate.parse(bookingDate, withoutHours);
                if (parsedBookingDate.isBefore(today)) {
                    System.out.println("Дата должна быть либо сегодняшней, либо более поздней. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Неправильный формат даты, попробуйте снова.");
            }
        }

        List<String> slots = bookingManager.getAvailableSlots(resource.get(), parsedBookingDate);
        if (slots.isEmpty()) {
            System.out.println("Доступные слоты на указанную дату отсутствуют.");
            System.out.println();
            return;
        } else {
            System.out.println("Доступные слоты на указанную дату");
            slots.forEach(System.out::println);
        }

        boolean isAvailable = false;
        while (!isAvailable) {

            System.out.println("Согласно режиму работы коворкинг-пространства " +
                    "ресурс можно забронировать в интервале с 09:00 до 19:00");

            LocalDateTime startTime;
            while (true) {
                System.out.println("Введите время начала (чч:мм):");
                String startDateTime = parsedBookingDate + " " + scanner.nextLine();

                try {
                    startTime = LocalDateTime.parse(startDateTime, completeFormatter);
                    if (startTime.toLocalTime().isBefore(LocalTime.of(9, 0)) || startTime.toLocalTime().isAfter(LocalTime.of(18, 30))) {
                        System.out.println("Время начала должно быть в интервале с 09:00 до 18:30.");
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Неправильный формат даты и времени. Попробуйте снова.");
                }
            }

            LocalDateTime endTime;
            while (true) {
                System.out.println("Введите время окончания (чч:мм):");
                String endDateTime = parsedBookingDate + " " + scanner.nextLine();

                try {
                    endTime = LocalDateTime.parse(endDateTime, completeFormatter);

                    if (!endTime.isAfter(startTime)) {
                        System.out.println("Время окончания должно быть позднее времени начала.");
                    } else if (endTime.toLocalTime().isAfter(LocalTime.of(19, 00))) {
                        System.out.println("Время окончания не должно быть позднее 19:00.");
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Неправильный формат даты и времени, попробуйте снова.");
                }
            }
            if (bookingManager.isResourceAvailable(resource.get(), startTime, endTime)) {
                Booking booking = bookingManager.addBooking(resource.get(), loggedInUser, startTime, endTime);
                System.out.println("Вы забронировали ресурс \'" + booking.getResource().getName() + "\' на " + booking.getStartTime().format(withoutHours) + ".\n" + "Время начала пользования: " + booking.getStartTime().format(hoursOnly) + "\n" + "Время окончания пользования: " + booking.getEndTime().format(hoursOnly) + "\n");
                isAvailable = true;
            } else {
                System.out.println("В указанное время ресурс занят. Попробуйте забронировать на другое время.");
                bookingManager.getAvailableSlots(resource.get(), parsedBookingDate);
            }
        }
    }
}
