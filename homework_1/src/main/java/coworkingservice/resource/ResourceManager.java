package coworkingservice.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Менеджер ресурсов в сервисе коворкинга.
 * Этот класс предоставляет методы для добавления, удаления, получения и обновления ресурсов.
 */
public class ResourceManager {

    private Map<Long, Resource> resources = new HashMap<>();
    private long nextResourceId = 1L;

    /**
     * Добавляет новый ресурс с указанным именем и типом.
     *
     * @param name имя ресурса
     * @param type тип ресурса
     * @return добавленный ресурс
     */
    public Resource addResource(String name, ResourceType type) {
        Resource resource = new Resource(nextResourceId++, name, type);
        resources.put(resource.getId(), resource);
        return resource;
    }

    /**
     * Удаляет ресурс по его идентификатору.
     *
     * @param id идентификатор ресурса для удаления
     * @return true, если ресурс был удален успешно; false, если ресурс не найден
     */
    public boolean deleteResource(long id) {
        return resources.remove(id) != null;
    }

    /**
     * Возвращает ресурс по его идентификатору в виде Optional.
     *
     * @param id идентификатор ресурса
     * @return Optional, содержащий ресурс, если такой найден; пустой Optional в противном случае
     */
    public Optional<Resource> getResource(long id) {
        if (id > 0 && id <= resources.size()) {
            return Optional.of(resources.get(id));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Возвращает все ресурсы в виде Map с ключами в виде их идентификаторов.
     *
     * @return Map всех ресурсов
     */
    public Map<Long, Resource> getAllResources() {
        return resources;
    }

    /**
     * Обновляет имя и тип ресурса с указанным идентификатором.
     *
     * @param id       идентификатор ресурса для обновления
     * @param newName  новое имя ресурса
     * @param newType  новый тип ресурса
     */
    public void updateResource(long id, String newName, ResourceType newType) {
        Resource resource = resources.get(id);
        if (!Objects.equals(newName, resource.getName())) {
            resource.setName(newName);
        }
        if (newType != resource.getType()) {
            resource.setType(newType);
        }
    }
}
