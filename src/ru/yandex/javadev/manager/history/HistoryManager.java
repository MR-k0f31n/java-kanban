package ru.yandex.javadev.manager.history;

import ru.yandex.javadev.data.Task;

public interface HistoryManager {

    void add(Task task);

    Object getHistory();
}
