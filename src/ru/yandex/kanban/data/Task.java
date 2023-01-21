package ru.yandex.kanban.data;

import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

public class Task {

    protected Integer id;
    protected String name;
    protected Status status;
    protected String description;
    protected LocalDateTime startTime;
    protected Duration duration;


    public Task (String name, String description, LocalDateTime time, int duration) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = time;
        this.duration = Duration.ofMinutes(duration);
    }

    public Task (int id, String name, String description, LocalDateTime starTime, Status status, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = starTime;
        this.duration = Duration.ofMinutes(duration);
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime(){
        if (startTime == null|| duration == null){
            return null;
        }
        return startTime.plus(duration);
    }


    public TypeTask getTypeTask() {
        return TypeTask.TASK;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name.length()  + '\'' +
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
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && status == task.status
                && Objects.equals(description, task.description) && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration);
    }
}
