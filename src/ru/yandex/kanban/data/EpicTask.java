package ru.yandex.kanban.data;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

import java.util.ArrayList;

public class EpicTask extends Task {
    private final ArrayList<Integer> subTaskIds = new ArrayList<>();

    public EpicTask(String name, String description) {
        super(name, description);
    }

    public EpicTask (int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }


    public void addSubTaskIds(int id) {
        subTaskIds.add(id);
    }

    public TypeTask getTypeTask () {
        return TypeTask.EPIC_TASK;
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
