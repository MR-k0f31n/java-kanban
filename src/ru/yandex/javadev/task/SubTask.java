package ru.yandex.javadev.task;

public class SubTask extends Task {
    protected int id;
    protected int epicID;

    public SubTask(int id, String name, String status, String description, int epicID) {
        super(id, name, status, description);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
}
