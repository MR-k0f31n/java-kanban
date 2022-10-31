package ru.yandex.javadev.task;

import ru.yandex.javadev.task.Task;

public class SubTask extends Task {
    protected int epicID;


    public SubTask(int id, String name, String status, String description, int epicID) {
        super(id, name, status, description);
        this.epicID = epicID;
    }

    public SubTask(String name, String status, String description, int epicID) {
        super(name, status, description);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }
}
