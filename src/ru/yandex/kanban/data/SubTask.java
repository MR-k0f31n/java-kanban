package ru.yandex.kanban.data;

import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

import java.time.LocalDateTime;

public class SubTask extends Task {

    private final int epicId;

    public SubTask (String name, String description, LocalDateTime time, int duration, int epicId) {
        super(name, description, time, duration);
        this.epicId = epicId;
    }

    public SubTask (int id, String name, String description, LocalDateTime startTime,  Status status, int duration, int epicId) {
        super(id, name, description, startTime, status, duration);
        this.epicId = epicId;
    }

    public Integer getEpicID() {
        return epicId;
    }

    public final TypeTask getTypeTask () {
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
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }
}
