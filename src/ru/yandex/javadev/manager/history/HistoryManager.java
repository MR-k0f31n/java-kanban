package ru.yandex.javadev.manager.history;

public interface HistoryManager {

    void add(Object object);

    Object getHistory();
}
