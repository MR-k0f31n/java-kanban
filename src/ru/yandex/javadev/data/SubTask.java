package ru.yandex.javadev.data;

public class SubTask extends Task {

    private int epicId;

    public SubTask (String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask (int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicID() {
        return epicId;
    }


    @Override
    public String toString() {
        return "SubTask{" +
                "epicID=" + epicId +
                ", id=" + id +
                ", name='" + name.length() + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description.length() + '\'' +
                '}';
    }
}
