package ru.yandex.kanban.manager.util;

import ru.yandex.kanban.manager.implemented.FileBackedTasksManager;
import ru.yandex.kanban.manager.interfaces.HistoryManager;
import ru.yandex.kanban.manager.history.InMemoryHistoryManager;
import ru.yandex.kanban.manager.implemented.InMemoryTaskManager;
import ru.yandex.kanban.manager.interfaces.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
