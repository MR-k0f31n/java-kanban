package ru.yandex.javadev.data;

public class SubTask extends Task {

    private Integer epicId;

    public SubTask (String name, String description, Integer epicId {
        super(name, description);
        this.epicId = epicId;
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
