package ru.yandex.javadev.task;
import java.util.ArrayList;

public class EpicTask extends Task {
    protected ArrayList<Integer> subTaskIDs = new ArrayList<>();

    public EpicTask(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getSubTaskIDs() {
        return subTaskIDs;
    }

    public void addSubTaskIDs(int id) {
        subTaskIDs.add(id);
    }

    @Override
    public String toString() {
        return "\n" + "EpicTask{" +
                "subTaskIDs=" + subTaskIDs +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
