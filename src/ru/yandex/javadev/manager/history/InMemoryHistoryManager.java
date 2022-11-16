package ru.yandex.javadev.manager.history;

import ru.yandex.javadev.data.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> historyList = new ArrayList<>();

    @Override
    public final void addHistory(Task task) {
        if(historyList.size() > 10) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyList;
    }
}
