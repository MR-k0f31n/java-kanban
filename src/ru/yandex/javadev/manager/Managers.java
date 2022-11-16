package ru.yandex.javadev.manager;

import ru.yandex.javadev.manager.history.HistoryManager;
import ru.yandex.javadev.manager.history.InMemoryHistoryManager;
import ru.yandex.javadev.manager.task.InMemoryTaskManager;
import ru.yandex.javadev.manager.task.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
