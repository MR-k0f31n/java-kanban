package ru.yandex.kanban.manager;

import ru.yandex.kanban.manager.implemented.FileBackedTasksManager;
import ru.yandex.kanban.manager.implemented.InMemoryTaskManager;
import ru.yandex.kanban.manager.interfaces.HistoryManager;
import ru.yandex.kanban.manager.history.InMemoryHistoryManager;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {

    public static TaskManager getInMemory() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        //Спасибо!!!!! Я еще даже не джун, я пелёночник) ресурсов мало знаю.
        return FileBackedTasksManager.loadFromFile(new File("resource/history.csv"));
    }

}
