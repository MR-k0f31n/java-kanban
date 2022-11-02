package ru.yandex.javadev;
import ru.yandex.javadev.task.EpicTask;
import ru.yandex.javadev.task.SubTask;
import ru.yandex.javadev.task.Task;
import java.util.HashMap;

public class Manager {
    protected int currencyID = 1;
    protected String[] Status = {"New", "InProgress", "Done"};

    protected HashMap<Integer, Task> taskList = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    protected HashMap<Integer, EpicTask> epicTaskList = new HashMap<>();

    public void addNewTask (Task task) {
        task.setId(currencyID++);
        task.setStatus(Status[0]);
        taskList.put(task.getId(), task);
    }

    public void addNewEpicTask (EpicTask epicTask) {
        epicTask.setId(currencyID++);
        epicTask.setStatus(Status[0]);
        epicTaskList.put(epicTask.getId(), epicTask);
    }

    public void addNewSubTask (SubTask subTask){
        subTask.setId(currencyID++);
        subTask.setStatus(Status[0]);
        subTaskList.put(subTask.getId(), subTask);
    }

    public void syncTaskID (EpicTask epicTask) {
        for (int epicID : epicTask.getSubTaskIDs()) {
            SubTask subTask = subTaskList.get(epicID);
        }
    }
}
