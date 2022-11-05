package ru.yandex.javadev.task;

public class SubTask extends Task {

    private Integer epicId;

    public SubTask (String name, String description) {
        super(name, description);
    }

    public SubTask (Integer id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Integer getEpicID() {
        return epicId;
    }

    public void setEpicID(int epicId) {
        this.epicId = epicId;
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
