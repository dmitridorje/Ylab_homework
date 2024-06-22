package coworkingservice.booking;

import coworkingservice.user.User;
import coworkingservice.resource.Resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Управляет бронированиями в сервисе коворкинга.
 * Этот класс предоставляет методы для добавления, удаления и поиска бронирований,
 * а также для проверки доступности ресурсов.
 */
public class BookingManager {
    private List<Booking> bookings = new ArrayList<>();
    private long nextBookingId = 1L;

    /**
     * Добавляет новое бронирование.
     *
     * @param resource  ресурс, который бронируется
     * @param user      пользователь, который делает бронирование
     * @param startTime время начала бронирования
     * @param endTime   время окончания бронирования
     * @return созданное бронирование
     */
    public Booking addBooking(Resource resource, User user, LocalDateTime startTime, LocalDateTime endTime) {
        Booking booking = new Booking(nextBookingId++, resource, user, startTime, endTime);
        bookings.add(booking);
        return booking;
    }

    /**
     * Удаляет бронирование по его идентификатору.
     *
     * @param id идентификатор бронирования
     * @return true, если бронирование было удалено; false, если бронирование не найдено
     */
    public boolean deleteBooking(long id) {
        return bookings.removeIf(booking -> booking.getId() == id);
    }

    /**
     * Возвращает список всех бронирований.
     *
     * @return список всех бронирований
     */
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    /**
     * Возвращает список бронирований для указанного пользователя.
     *
     * @param user пользователь, для которого нужно найти бронирования
     * @return список бронирований для указанного пользователя
     */
    public List<Booking> getBookingsByUser(User user) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUser().equals(user)) {
                result.add(booking);
            }
        }
        return result;
    }

    /**
     * Возвращает список бронирований для указанного ресурса.
     *
     * @param resource ресурс, для которого нужно найти бронирования
     * @return список бронирований для указанного ресурса
     */
    public List<Booking> getBookingsByResource(Resource resource) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getResource().equals(resource)) {
                result.add(booking);
            }
        }
        return result;
    }

    /**
     * Проверяет доступность ресурса в указанный период.
     *
     * @param resource  ресурс, который проверяется
     * @param startTime время начала периода
     * @param endTime   время окончания периода
     * @return true, если ресурс доступен; false, если ресурс занят
     */
    public boolean isResourceAvailable(Resource resource, LocalDateTime startTime, LocalDateTime endTime) {
        for (Booking booking : bookings) {
            if (booking.getResource().equals(resource) &&
                    ((startTime.isAfter(booking.getStartTime()) && startTime.isBefore(booking.getEndTime())) ||
                            (endTime.isAfter(booking.getStartTime()) && endTime.isBefore(booking.getEndTime())) ||
                            startTime.isEqual(booking.getStartTime()) ||
                            endTime.isEqual(booking.getEndTime()) ||
                            (startTime.isBefore(booking.getStartTime()) && endTime.isAfter(booking.getEndTime())))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Возвращает список доступных временных слотов для бронирования указанного ресурса на указанный день.
     *
     * @param resource ресурс, для которого нужно найти доступные слоты
     * @param date     день, для которого нужно найти доступные слоты
     * @return список доступных временных слотов
     */
    public List<String> getAvailableSlots(Resource resource, LocalDate date) {
        List<String> availableSlots = new ArrayList<>();
        LocalDateTime startOfDay = date.atTime(9, 0);
        LocalDateTime endOfDay = date.atTime(19, 0);

        List<LocalDateTime> bookingTimes = new ArrayList<>();

        for (Booking booking : getBookingsByResource(resource)) {
            LocalDateTime bookingStart = booking.getStartTime();
            LocalDateTime bookingEnd = booking.getEndTime();
            if (bookingStart.toLocalDate().equals(date)) {
                bookingTimes.add(bookingStart);
                bookingTimes.add(bookingEnd);
            }
        }

        bookingTimes.sort(LocalDateTime::compareTo);
        LocalDateTime currentStart = startOfDay;

        DateTimeFormatter formatterWithoutHours = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (LocalDateTime time : bookingTimes) {
            if (currentStart.isBefore(time)) {
                if (isResourceAvailable(resource, currentStart, time)) {
                    availableSlots.add("c " + currentStart.format(formatter) + " до " + time.format(formatter));
                }
                currentStart = time;
            }
        }

        if (currentStart.isBefore(endOfDay) && isResourceAvailable(resource, currentStart, endOfDay)) {
            availableSlots.add("c " + currentStart.format(formatter) + " до " + endOfDay.format(formatter));
        }
        return availableSlots;
    }

    /**
     * Возвращает список бронирований, отсортированных по дате и времени начала.
     *
     * @return отсортированный список бронирований
     */
    public List<Booking> filterBookingsByDate() {
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getStartTime))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список бронирований, отсортированных по имени пользователя и времени начала.
     *
     * @return отсортированный список бронирований
     */
    public List<Booking> filterBookingsByUser() {
        return bookings.stream()
                .sorted(Comparator.comparing((Booking booking) -> booking.getUser().getUsername())
                        .thenComparing(Booking::getStartTime))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список бронирований, отсортированных по имени ресурса, имени пользователя и времени начала.
     *
     * @return отсортированный список бронирований
     */
    public List<Booking> filterBookingsByResource() {
        return bookings.stream()
                .sorted(Comparator.comparing((Booking booking) -> booking.getResource().getName())
                        .thenComparing((Booking booking) -> booking.getUser().getUsername())
                        .thenComparing(Booking::getStartTime))
                .collect(Collectors.toList());
    }
}
