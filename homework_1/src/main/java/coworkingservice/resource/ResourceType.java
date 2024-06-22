package coworkingservice.resource;

/**
 * Перечисление, представляющее типы ресурсов в системе коворкинга.
 * Каждый тип ресурса имеет отображаемое имя.
 */
public enum ResourceType {

    /**
     * Рабочее место.
     */
    WORKSPACE("Рабочее место"),

    /**
     * Конференц-зал.
     */
    CONFERENCE_ROOM("Конференц-зал");

    private final String displayName;

    /**
     * Конструктор перечисления ResourceType.
     *
     * @param displayName отображаемое имя типа ресурса
     */
    ResourceType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Возвращает строковое представление отображаемого имени типа ресурса.
     *
     * @return отображаемое имя типа ресурса
     */
    @Override
    public String toString() {
        return displayName;
    }
}
