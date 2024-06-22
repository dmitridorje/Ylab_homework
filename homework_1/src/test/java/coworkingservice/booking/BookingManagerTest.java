package coworkingservice.booking;

import coworkingservice.resource.ResourceType;
import coworkingservice.user.User;
import coworkingservice.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Модульные тесты для класса операций с бронированиями {@link BookingManager}
 */
class BookingManagerTest {

    private BookingManager bookingManager;
    private Resource resource;
    private Resource resource2;
    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<Booking> bookings;
    Booking booking;

    @BeforeEach
    public void setUp() {
        bookingManager = new BookingManager();
        user = new User("John", "Doe");
        resource = new Resource(1L, "Workspace 1", ResourceType.WORKSPACE);
        resource2 = new Resource(2L, "Conference room 1", ResourceType.CONFERENCE_ROOM);
        startTime = LocalDateTime.of(2024, 8, 8, 10, 0);
        endTime = startTime.plusHours(2);
        bookings = new ArrayList<>();
        booking = bookingManager.addBooking(resource, user, startTime, endTime);
    }

    @Test
    @DisplayName("Добавление нового бронирования")
    public void testAddBooking() {
        assertAll(
                () -> assertNotNull(booking),
                () -> assertEquals(resource, booking.getResource()),
                () -> assertEquals(user, booking.getUser())
        );
    }

    @Test
    @DisplayName("Удаление бронирования")
    public void testDeleteBooking() {
        boolean result = bookingManager.deleteBooking(booking.getId());
        assertTrue(result);
    }

    @Test
    @DisplayName("Удаление несуществующего бронирования")
    public void testDeleteNonExistentBooking() {
        boolean result = bookingManager.deleteBooking(999);
        assertFalse(result);
    }

    @Test
    @DisplayName("Доступность ресурса - положительный сценарий")
    public void testIsResourceAvailable_positive() {
        boolean available = bookingManager.isResourceAvailable(resource, startTime.plusHours(2), endTime.plusHours(2));
        assertTrue(available);
    }

    @Test
    @DisplayName("Доступность ресурса - отрицательный сценарий")
    public void testIsResourceAvailable_negative() {
        boolean available = bookingManager.isResourceAvailable(resource, startTime.plusMinutes(30), endTime.minusMinutes(15));
        assertFalse(available);
    }

    @Test
    @DisplayName("Получение всех бронирований")
    public void testGetAllBookings() {
        bookingManager.addBooking(resource, user, startTime.plusHours(2), endTime.plusHours(3));
        List<Booking> allBookings = bookingManager.getAllBookings();
        assertAll(
                () -> assertNotNull(allBookings),
                () -> assertEquals(allBookings.size(), 2),
                () -> assertEquals(allBookings.get(1).getStartTime(), LocalDateTime.of(2024, 8, 8, 12, 0)),
                () -> assertEquals(allBookings.get(1).getEndTime(), LocalDateTime.of(2024, 8, 8, 15, 0))
        );
    }

    @Test
    @DisplayName("Получение бронирований конкретного пользователя")
    public void testGetBookingsByUser() {
        bookingManager.addBooking(resource, user, startTime.plusHours(2), endTime.plusHours(3));
        List<Booking> bookings = bookingManager.getBookingsByUser(user);
        assertAll(
                () -> assertNotNull(bookings),
                () -> assertEquals(bookings.size(), 2),
                () -> assertEquals(bookings.get(0).getUser(), user),
                () -> assertEquals(bookings.get(1).getUser(), user)
        );
    }

    @Test
    @DisplayName("Доступные слоты при отсутствии бронирований")
    public void testGetAvailableSlotsNoBookings() {
        LocalDate date = LocalDate.of(2025, 8, 8);
        List<String> slots = bookingManager.getAvailableSlots(resource, date);

        assertAll(
                () -> assertEquals(1, slots.size()),
                () -> assertEquals("c 09:00 до 19:00", slots.get(0))
        );
    }

    @Test
    @DisplayName("Доступные слоты при наличии одного бронирования")
    public void testGetAvailableSlotsWithOneBooking() {
        LocalDate date = LocalDate.of(2024, 8, 8);
        List<String> slots = bookingManager.getAvailableSlots(resource, date);

        assertAll(
                () -> assertEquals(2, slots.size()),
                () -> assertEquals("c 09:00 до 10:00", slots.get(0)),
                () -> assertEquals("c 12:00 до 19:00", slots.get(1))
        );
    }

    @Test
    @DisplayName("Отсутствие слотов при наличии бронирования на полный день")
    public void testGetAvailableSlotsWithFullDayBooking() {
        LocalDate date = LocalDate.now();
        bookingManager.addBooking(resource, new User("user1", "pass1"), date.atTime(9, 0), date.atTime(19, 0));
        List<String> slots = bookingManager.getAvailableSlots(resource, date);

        assertTrue(slots.isEmpty());
    }

    @Test
    @DisplayName("Фильтрация бронирований по дате")
    public void testFilterBookingsByDate() {
        bookingManager.addBooking(resource, user, LocalDateTime.of(2023, 6, 21, 12, 0), LocalDateTime.of(2023, 6, 21, 14, 0));
        bookingManager.addBooking(resource, user, LocalDateTime.of(2023, 6, 21, 14, 0), LocalDateTime.of(2023, 6, 21, 16, 0));

        List<Booking> bookings = bookingManager.filterBookingsByDate();

        assertAll(
                () -> assertEquals(3, bookings.size()),
                () -> assertEquals(LocalDateTime.of(2023, 6, 21, 12, 0), bookings.get(0).getStartTime()),
                () -> assertEquals(LocalDateTime.of(2023, 6, 21, 14, 0), bookings.get(1).getStartTime()),
                () -> assertEquals(LocalDateTime.of(2024, 8, 8, 10, 0), bookings.get(2).getStartTime())
        );
    }
}