package ru.yandex.javadev.manager.history;

import ru.yandex.javadev.data.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> historyList = new ArrayList<>();

    @Override
    public final void add(Object object) {
        if(historyList.size() > 10) {
            historyList.remove(0);
        } else {
            historyList.add(object);
        }
    }

    @Override
    public Object getHistory() {
        return historyList;
    }
}
