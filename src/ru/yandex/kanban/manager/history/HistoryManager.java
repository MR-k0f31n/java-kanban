package ru.yandex.kanban.manager.history;

import ru.yandex.kanban.data.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}
