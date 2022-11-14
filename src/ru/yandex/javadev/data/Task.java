package ru.yandex.javadev.data;

public class Task {

    protected Integer id;

    protected String name;

    protected Enum status;

    protected String description;

    public Task (String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public  Task (Integer id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
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

    public Enum getStatus() {
        return status;
    }

    public void setStatus(Enum status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
