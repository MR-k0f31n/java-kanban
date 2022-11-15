package ru.yandex.javadev.manager.history;

import ru.yandex.javadev.data.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> historyList = new ArrayList<>();

    public final void add(Task task) {
        if(historyList.size() > 10) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    public Object getHistory() {
        return historyList;
    }
}
