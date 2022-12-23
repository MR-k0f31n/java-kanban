package ru.yandex.kanban.data;

import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

public class Task {

    protected Integer id;

    protected String name;

    protected Status status;

    protected String description;

    public Task (String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public Task (int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
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

    public TypeTask getTypeTask () {
        return TypeTask.TASK;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name.length()  + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description.length() + '\'' +
                '}';
    }
}
