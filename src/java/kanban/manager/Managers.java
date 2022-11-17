package java.kanban.manager;

import java.kanban.manager.history.HistoryManager;
import java.kanban.manager.history.InMemoryHistoryManager;
import java.kanban.manager.task.InMemoryTaskManager;
import java.kanban.manager.task.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
