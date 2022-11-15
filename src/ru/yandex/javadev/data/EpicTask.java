package ru.yandex.javadev.data;
import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public EpicTask(String name, String description) {
        super(name, description);
    }

    public EpicTask (int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    public void addSubTaskIds(Integer id) {
        subTaskIds.add(id);
    }

    public void removeIdsSubTask (Integer id) {
        subTaskIds.remove(id);
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
