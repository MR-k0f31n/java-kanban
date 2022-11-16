package ru.yandex.javadev.manager.history;

import ru.yandex.javadev.data.Task;

import java.util.ArrayList;

public interface HistoryManager {

    void addHistory(Task task);

    ArrayList<Task> getHistory();
}
