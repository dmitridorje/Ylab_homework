package coworkingservice.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceTest {
    private Resource resource1;
    private Resource resource2;
    private Resource resource3;

    @BeforeEach
    public void setUp() {
        resource1 = new Resource(1, "Workspace A", ResourceType.WORKSPACE);
        resource2 = new Resource(1, "Workspace A", ResourceType.WORKSPACE);
        resource3 = new Resource(2, "Conference Room B", ResourceType.CONFERENCE_ROOM);
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(resource1.equals(resource1));
    }

    @Test
    public void testEquals_EqualObjects() {
        assertTrue(resource1.equals(resource2));
    }

    @Test
    public void testEquals_DifferentObjects() {
        assertFalse(resource1.equals(resource3));
    }

    @Test
    public void testEquals_NullObject() {
        assertFalse(resource1.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(resource1.equals(new Object()));
    }

    @Test
    public void testHashCode_EqualObjects() {
        assertEquals(resource1.hashCode(), resource2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects() {
        assertNotEquals(resource1.hashCode(), resource3.hashCode());
    }
}
