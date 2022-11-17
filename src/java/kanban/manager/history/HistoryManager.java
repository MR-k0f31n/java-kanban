package java.kanban.manager.history;

import java.kanban.data.Task;

import java.util.ArrayList;

public interface HistoryManager {

    void addHistory(Task task);

    ArrayList<Task> getHistory();
}
