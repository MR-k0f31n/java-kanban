package ru.yandex.kanban.data;

import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

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

    public TypeTask getTypeTask () {
        return TypeTask.SUB_TASK;
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
