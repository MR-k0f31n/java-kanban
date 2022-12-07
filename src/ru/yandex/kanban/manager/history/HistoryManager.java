package ru.yandex.kanban.manager.history;

import ru.yandex.kanban.data.Task;

import java.util.List;

public interface HistoryManager {

    void addHistory(Task task);

    void removeHistory(int id);

    List<Task> getHistory();
}
