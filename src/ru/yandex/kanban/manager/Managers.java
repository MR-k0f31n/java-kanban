package ru.yandex.kanban.manager;

import ru.yandex.kanban.manager.implemented.FileBackedTasksManager;
import ru.yandex.kanban.manager.implemented.HttpTaskManager;
import ru.yandex.kanban.manager.implemented.InMemoryTaskManager;
import ru.yandex.kanban.manager.interfaces.HistoryManager;
import ru.yandex.kanban.manager.history.InMemoryHistoryManager;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Managers {

    private static final File FILE = new File("resources", "history.csv");
    public static TaskManager getInMemory() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager loadFromFileManager() {
        return FileBackedTasksManager.loadFromFile(FILE);
    }

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager(FILE);
    }
}
