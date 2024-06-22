package coworkingservice.resource;

import java.util.Objects;

/**
 * Представляет ресурс в сервисе коворкинга.
 * Класс содержит информацию о идентификаторе, имени и типе ресурса.
 */
public class Resource {

    long id;
    String name;
    ResourceType type;

    /**
     * Создает новый ресурс с указанными идентификатором, именем и типом.
     *
     * @param id   уникальный идентификатор ресурса
     * @param name имя ресурса
     * @param type тип ресурса
     */
    public Resource(long id, String name, ResourceType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    /**
     * Возвращает уникальный идентификатор ресурса.
     *
     * @return уникальный идентификатор ресурса
     */
    public long getId() {
        return id;
    }

    /**
     * Возвращает имя ресурса.
     *
     * @return имя ресурса
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает новое имя ресурса.
     *
     * @param name новое имя ресурса
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает тип ресурса.
     *
     * @return тип ресурса
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Устанавливает новый тип ресурса.
     *
     * @param type новый тип ресурса
     */
    public void setType(ResourceType type) {
        this.type = type;
    }

    /**
     * Сравнивает этот ресурс с другим объектом на равенство.
     *
     * @param o объект для сравнения
     * @return true, если объекты равны; false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return id == resource.id && Objects.equals(name, resource.name) && type == resource.type;
    }

    /**
     * Возвращает хэш-код этого ресурса.
     *
     * @return хэш-код ресурса
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }
}
