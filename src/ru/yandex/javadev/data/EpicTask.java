package ru.yandex.javadev.data;
import java.util.ArrayList;

public class EpicTask extends Task {
    private static ArrayList<Integer> subTaskIds = new ArrayList<>();

    public EpicTask(String name, String description) {
        super(name, description);
    }

    public EpicTask (Integer id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public static ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public static void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        EpicTask.subTaskIds = subTaskIds;
    }

    public static void addSubTaskIds(int id) {
        subTaskIds.add(id);
    }

    public static void removeIdsSubTask (Integer id) {
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
