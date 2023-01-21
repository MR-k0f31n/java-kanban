package ru.yandex.kanban.manager;

import ru.yandex.kanban.manager.implemented.FileBackedTasksManager;
import ru.yandex.kanban.manager.implemented.InMemoryTaskManager;
import ru.yandex.kanban.manager.interfaces.HistoryManager;
import ru.yandex.kanban.manager.history.InMemoryHistoryManager;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.io.File;

public class Managers {

    private static final File FILE = new File("resources", "history.csv");
    public static TaskManager getInMemory() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return FileBackedTasksManager.loadFromFile(FILE);
    }

}
