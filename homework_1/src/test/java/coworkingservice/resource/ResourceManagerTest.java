package coworkingservice.resource;

import coworkingservice.booking.BookingManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты для класса операций с ресурсами {@link BookingManager}
 */
class ResourceManagerTest {
    private ResourceManager resourceManager;
    private Resource resource1;
    private Resource resource2;
    long id;

    @BeforeEach
    public void setUp() {
        resourceManager = new ResourceManager();
        resource1 = resourceManager.addResource("Workspace 1", ResourceType.WORKSPACE);
        resource2 = resourceManager.addResource("Conference Room 1", ResourceType.CONFERENCE_ROOM);
        id = 1L;
    }

    @Test
    @DisplayName("Добавление нового ресурса")
    public void testAddResource() {
        Resource resource = resourceManager.addResource("Переговорная", ResourceType.CONFERENCE_ROOM);

        assertAll(
                () -> assertNotNull(resource),
                () -> assertEquals(resource.getName(), "Переговорная"),
                () -> assertEquals(resource.getType(), ResourceType.CONFERENCE_ROOM)
        );
    }

    @Test
    @DisplayName("Удаление ресурса")
    public void testDeleteResource() {
        Resource resource = resourceManager.addResource("Workspace No. 108", ResourceType.WORKSPACE);
        boolean result = resourceManager.deleteResource(resource.getId());
        assertTrue(result);
    }

    @Test
    @DisplayName("Удаление несуществующего ресурса")
    public void testDeleteNonExistentResource() {
        boolean result = resourceManager.deleteResource(999);
        assertFalse(result);
    }

    @Test
    @DisplayName("Получение всех ресурсов")
    public void testGetAllResources() {

        Map<Long, Resource> resources = resourceManager.getAllResources();

        assertAll(
                () -> assertNotNull(resources),
                () -> assertEquals(resources.get(1L), resource1),
                () -> assertEquals(resources.get(2L), resource2),
                () -> assertEquals(resources.size(), 2)
        );
    }

    @Test
    @DisplayName("Получение ресурса по валидному id")
    public void testGetResourceValidId() {
        Optional<Resource> resource = resourceManager.getResource(1L);

        assertAll(
                () -> assertTrue(resource.isPresent()),
                () -> assertEquals("Workspace 1", resource.get().getName())
        );
    }

    @Test
    @DisplayName("Получение ресурса по невалидному id")
    public void testGetResourceInvalidNegativeId() {
        Optional<Resource> resource = resourceManager.getResource(-1L);
        assertFalse(resource.isPresent());
    }

    @Test
    @DisplayName("Обновление наименования ресурса")
    public void testUpdateResourceName() {

        ResourceType type = resourceManager.getResource(id).get().getType();
        resourceManager.updateResource(id, "Updated resource name", type);
        Resource updatedResource = resourceManager.getResource(id).orElse(null);

        assertAll(
                () -> assertNotNull(updatedResource),
                () -> assertEquals("Updated resource name", updatedResource.getName()),
                () -> assertEquals(ResourceType.WORKSPACE, updatedResource.getType())
        );
    }

    @Test
    @DisplayName("Обновление типа ресурса")
    public void testUpdateResourceType() {
        String name = resourceManager.getResource(id).get().getName();
        resourceManager.updateResource(id, name, ResourceType.CONFERENCE_ROOM);
        Resource updatedResource = resourceManager.getResource(id).orElse(null);

        assertAll(
                () -> assertNotNull(updatedResource),
                () -> assertEquals("Workspace 1", updatedResource.getName()),
                () -> assertEquals(ResourceType.CONFERENCE_ROOM, updatedResource.getType())
        );
    }

    @Test
    @DisplayName("Выброс NullPointerException при обновлении несуществующего ресурса")
    public void testUpdateNonExistentResource() {
        assertThrows(NullPointerException.class, () -> {
            resourceManager.updateResource(10, "NonExistent", ResourceType.WORKSPACE);
        });
    }
}
