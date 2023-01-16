package ru.yandex.kanban.data;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class EpicTask extends Task {
    private final ArrayList<Integer> subTaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    public EpicTask(String name, String description, LocalDateTime time, int duration) {
        super(name, description, time, duration);
    }

    public EpicTask (int id, String name, String description, LocalDateTime startTime, Status status, int duration) {
        super(id, name, description, startTime, status, duration);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }


    public void addSubTaskIds(int id) {
        subTaskIds.add(id);
    }

    public final TypeTask getTypeTask () {
        return TypeTask.EPIC_TASK;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "subTaskIDs=" + subTaskIds +
                ", id=" + id +
                ", name='" + name.length()  + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description.length() + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
