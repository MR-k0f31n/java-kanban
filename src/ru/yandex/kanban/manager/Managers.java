package ru.yandex.kanban.manager;

import ru.yandex.kanban.manager.implemented.FileBackedTasksManager;
import ru.yandex.kanban.manager.implemented.InMemoryTaskManager;
import ru.yandex.kanban.manager.interfaces.HistoryManager;
import ru.yandex.kanban.manager.history.InMemoryHistoryManager;
import ru.yandex.kanban.manager.interfaces.TaskManager;

public class Managers {
    public static TaskManager getInMemory() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        // Александр, я тупой, пожалуйста покажите как правильно передать путь в рут папку я замучался =((
        return new FileBackedTasksManager("resources/history.csv");
    }

}
