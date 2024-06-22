package coworkingservice.booking;

import coworkingservice.user.User;
import coworkingservice.resource.Resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Представляет бронирование в сервисе коворкинга.
 * Этот класс инкапсулирует детали бронирования, включая забронированный ресурс,
 * пользователя, сделавшего бронирование, а также время начала и окончания бронирования.
 */
public class Booking {

    private long id;
    private Resource resource;
    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    DateTimeFormatter formatter;

    /**
     * Создает новый экземпляр Booking.
     *
     * @param id        уникальный идентификатор бронирования
     * @param resource  забронированный ресурс
     * @param user      пользователь, сделавший бронирование
     * @param startTime время начала бронирования
     * @param endTime   время окончания бронирования
     */
    public Booking(long id, Resource resource, User user, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.resource = resource;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Возвращает уникальный идентификатор бронирования.
     *
     * @return ID бронирования
     */
    public long getId() {
        return id;
    }

    /**
     * Возвращает забронированный ресурс.
     *
     * @return забронированный ресурс
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Возвращает пользователя, сделавшего бронирование.
     *
     * @return пользователь, сделавший бронирование
     */
    public User getUser() {
        return user;
    }

    /**
     * Возвращает время начала бронирования.
     *
     * @return время начала бронирования
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Возвращает форматированное время начала бронирования.
     *
     * @return форматированное время начала в виде строки
     */
    public String getStartTimeFormatted() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return startTime.format(formatter);
    }

    /**
     * Возвращает время окончания бронирования.
     *
     * @return время окончания бронирования
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Возвращает форматированное время окончания бронирования.
     *
     * @return форматированное время окончания в виде строки
     */
    public String getEndTimeFormatted() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return endTime.format(formatter);
    }

    /**
     * Указывает, равен ли некоторый другой объект этому.
     *
     * @param o объект для сравнения с этим
     * @return true, если этот объект равен аргументу obj; false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id && Objects.equals(resource, booking.resource) && Objects.equals(user, booking.user) && Objects.equals(startTime, booking.startTime) && Objects.equals(endTime, booking.endTime) && Objects.equals(formatter, booking.formatter);
    }

    /**
     * Возвращает значение хэш-кода для объекта.
     *
     * @return значение хэш-кода для этого объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, resource, user, startTime, endTime, formatter);
    }
}
