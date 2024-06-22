package coworkingservice.booking;

import coworkingservice.resource.Resource;
import coworkingservice.resource.ResourceType;
import coworkingservice.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    private Resource resource;
    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    public void setUp() {
        resource = new Resource(1, "Workspace", ResourceType.WORKSPACE);
        user = new User("user1", "password");
        startTime = LocalDateTime.of(2024, 6, 22, 9, 0);
        endTime = LocalDateTime.of(2024, 6, 22, 10, 0);

        booking1 = new Booking(1, resource, user, startTime, endTime);
        booking2 = new Booking(1, resource, user, startTime, endTime);
        booking3 = new Booking(2, resource, user, startTime.plusHours(1), endTime.plusHours(1));
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(booking1.equals(booking1));
    }

    @Test
    public void testEquals_EqualObjects() {
        assertTrue(booking1.equals(booking2));
    }

    @Test
    public void testEquals_DifferentObjects() {
        assertFalse(booking1.equals(booking3));
    }

    @Test
    public void testEquals_NullObject() {
        assertFalse(booking1.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(booking1.equals(new Object()));
    }

    @Test
    public void testHashCode_EqualObjects() {
        assertEquals(booking1.hashCode(), booking2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects() {
        assertNotEquals(booking1.hashCode(), booking3.hashCode());
    }
}
