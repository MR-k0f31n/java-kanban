package ru.yandex.javadev.task;

public class SubTask extends Task {

    protected int epicID;
    protected boolean isSubTask;

    public SubTask (String name, String description) {
        super(name, description);
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    public boolean isSubTask() {
        return isSubTask;
    }

    public void setSubTask () {
        isSubTask = true;
    }

    @Override
    public String toString() {
        return "\n" + "SubTask{" +
                "epicID=" + epicID +
                ", isSubTask=" + isSubTask +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
