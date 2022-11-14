package ru.yandex.javadev.data;
import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public EpicTask(String name, String description) {
        super(name, description);
    }

    public EpicTask (Integer id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void addSubTaskIds(int id) {
        subTaskIds.add(id);
    }

    public void removeIdsSubTask (Integer id) {
        subTaskIds.remove(id);
    }

    public void updateSubTaskIds (ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
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
