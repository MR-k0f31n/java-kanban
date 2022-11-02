package ru.yandex.javadev.task;
import java.util.ArrayList;

public class EpicTask extends Task {
    protected ArrayList<Integer> subTaskIDs = new ArrayList<>();
    protected boolean isEpic;


    public EpicTask(int id, String name, String status, String description, ArrayList<Integer> subTaskIDs, boolean isEpic) {
        super(id, name, status, description);
        this.isEpic = isEpic;
    }
    public EpicTask(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getSubTaskIDs() {
        return subTaskIDs;
    }

    public void setSubTaskIDs(ArrayList<Integer> subTaskIDs) {
        this.subTaskIDs = subTaskIDs;
    }

    public boolean isEpic() {
        return isEpic;
    }

    public void setEpic(boolean epic) {
        isEpic = epic;
    }
}
