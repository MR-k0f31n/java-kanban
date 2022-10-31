package ru.yandex.javadev.task;
import ru.yandex.javadev.task.Task;
import java.util.ArrayList;

public class EpicTask extends Task {
    protected ArrayList<Integer> subTaskIDs = new ArrayList<>();


    public EpicTask(int id, String name, String status, String description, ArrayList<Integer> subTaskIDs) {
        super(id, name, status, description);
    }

    public EpicTask(String name, String status, String description, ArrayList<Integer> subTaskIDs) {
        super(name, status, description);
    }
    public void addSubTuskID (int id) {
        subTaskIDs.add(id);
    }

    public ArrayList<Integer> getSubTaskIDs() {
        return subTaskIDs;
    }

    public boolean isEpic () {
        return true;
    }

    public void cleanSubTuskID() {
        subTaskIDs.clear();
    }


}
