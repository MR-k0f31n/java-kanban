package ru.yandex.javadev.data;
import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public EpicTask(String name, String description) {
        super(name, description);
    }

    public EpicTask (int id, String name, String description, Status status, ArrayList<Integer> subTaskIds) {
        super(id, name, description, status);
        this.subTaskIds = subTaskIds;
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }


    public void addSubTaskIds(int id) {
        subTaskIds.add(id);
    }


    @Override
    public String toString() {
        return "EpicTask{" +
                "subTaskIDs=" + subTaskIds +
                ", id=" + id +
                ", name='" + name.length()  + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description.length() + '\'' +
                '}';
    }
}
