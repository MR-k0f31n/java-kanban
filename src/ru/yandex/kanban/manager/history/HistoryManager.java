package ru.yandex.kanban.manager.history;

import ru.yandex.kanban.data.Task;

import java.util.ArrayList;

public interface HistoryManager {

    void addHistory(Task task);

    ArrayList<Task> getHistory();
}
