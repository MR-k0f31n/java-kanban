package ru.yandex.javadev;
import ru.yandex.javadev.task.EpicTask;
import ru.yandex.javadev.task.Status;
import ru.yandex.javadev.task.SubTask;
import ru.yandex.javadev.task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    protected int currencyID = 1;

    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private HashMap<Integer, EpicTask> epicTaskList = new HashMap<>();

    public void addNewTask (Task task) {
        task.setId(currencyID++);
        taskList.put(task.getId(), task);
    }

    public void addNewEpicTask (EpicTask epicTask) {
        epicTask.setId(currencyID++);
        epicTaskList.put(epicTask.getId(), epicTask);
    }

    public void addNewSubTask (SubTask subTask){
        if (epicTaskList.containsKey(subTask.getEpicID())) {
            subTask.setId(currencyID++);
            epicTaskList.get(subTask.getEpicID()).addSubTaskIds(subTask.getId());
            subTaskList.put(subTask.getId(), subTask);
            syncEpicTaskStatus(subTask.getEpicID());
        } else {
            System.out.println("Error 404: Task not found");
        }
    }

    public void syncEpicTaskStatus (Integer idEpic) {
        int subNew = 0;
        int subDone = 0;

        if (epicTaskList.containsKey(idEpic)) {
            EpicTask epicTask = epicTaskList.get(idEpic);
            if (epicTask.getSubTaskIds().size() != 0) {
                for (Integer idSubs : epicTask.getSubTaskIds()) {
                    if (subTaskList.get(idSubs).getStatus().equals(Status.NEW)) {
                        subNew ++;
                    } else if (subTaskList.get(idSubs).getStatus().equals(Status.DONE)) {
                        subDone ++;
                    }
                }
                if (epicTask.getSubTaskIds().size() == subNew) {
                    epicTask.setStatus(Status.NEW);
                    epicTaskList.put(epicTask.getId(), epicTask);
                } else if (epicTask.getSubTaskIds().size() == subDone) {
                    epicTask.setStatus(Status.DONE);
                    epicTaskList.put(epicTask.getId(), epicTask);
                } else {
                    epicTask.setStatus(Status.IN_PROGRESS);
                    epicTaskList.put(epicTask.getId(), epicTask);
                }
            } else {
                epicTask.setStatus(Status.NEW);
                epicTaskList.put(epicTask.getId(), epicTask);
            }
        }
    }

    public ArrayList<SubTask> getAllSubByEpicTask (int id) {
        ArrayList <SubTask> subByEpicTaskList = new ArrayList<>();
        if (epicTaskList.containsKey(id)) {
            ArrayList<Integer> subTaskIds = epicTaskList.get(id).getSubTaskIds();
            for (Integer idSub : subTaskIds) {
                if (idSub != null) {
                    subByEpicTaskList.add(subTaskList.get(idSub));
                }
            }
        }
        return subByEpicTaskList;
    }

    public void updateTask (Task newTask) {
        if (taskList.containsKey(newTask.getId())) {
            taskList.put(newTask.getId(), newTask);
        }
    }

    public void updateEpicTask (EpicTask newTask) {
        if (epicTaskList.containsKey(newTask.getId())) {
            EpicTask oldTask = epicTaskList.get(newTask.getId());
            if (oldTask.getSubTaskIds().size() != 0) {
                newTask.updateSubTaskIds(oldTask.getSubTaskIds());
                // как передать старый лист ид я не придумал)
            }
            if (!oldTask.getStatus().equals(newTask.getStatus())) {
                newTask.setStatus(oldTask.getStatus());
                // ИМХО чтобы умники не пытались руками поменять статус!
            }
            epicTaskList.put(oldTask.getId(), newTask);
        }
    }

    public void updateSubTask (SubTask newTask) {
        if (subTaskList.containsKey(newTask.getId())) {
            newTask.setEpicID(subTaskList.get(newTask.getId()).getEpicID());
            subTaskList.put(newTask.getId(), newTask);
            syncEpicTaskStatus(newTask.getEpicID());
        }
    }

    public ArrayList<Task> getAllTask () { return new ArrayList(taskList.values()); }

    public ArrayList<SubTask> getAllSubTask () {
        ArrayList<SubTask> listAllSubTask = new ArrayList<>();
        for (Integer taskID : subTaskList.keySet()) {
            listAllSubTask.add(subTaskList.get(taskID));
        }
        return listAllSubTask;
    }

    public ArrayList<EpicTask> getAllEpicTask () {
        ArrayList<EpicTask> listAAllEpicTask = new ArrayList<>();
        for (Integer taskID : epicTaskList.keySet()) {
            listAAllEpicTask.add(epicTaskList.get(taskID));
        }
        return listAAllEpicTask;
    }

    public void clearAll () {
        System.out.println("ВЫ уверены?");
        clearAllTask();
        clearAllEpicTask();
        clearAllSubTask();
    }

    public void clearAllTask () {
        taskList.clear();
    }

    public void clearAllEpicTask () {
        epicTaskList.clear();
    }

    public void clearAllSubTask () {
        subTaskList.clear();
    }

    public Task getTaskById (int id) {
        return taskList.get(id);
    }

    public EpicTask getEpicTaskById (int id) {
        return epicTaskList.get(id);
    }

    public SubTask getSubTaskById (int id) {
        return subTaskList.get(id);
    }

    public void deleteTaskById (int id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
        } else {
            System.out.println("Error 404: Object not found!");
        }
    }

    public void deleteSubTaskById (int id) {
        if (subTaskList.containsKey(id)) {
            SubTask oldSubTask = subTaskList.get(id);
            epicTaskList.get(oldSubTask.getEpicID()).removeIdsSubTask(id);
            subTaskList.remove(id);
            //remove (Object key)
            syncEpicTaskStatus(oldSubTask.getEpicID());
        } else {
            System.out.println("Error 404: Object not found!");
        }
    }

    public void deleteEpicTaskById (int id) {
        if (epicTaskList.containsKey(id)) {
            ArrayList<Integer> listIdsSubTask = epicTaskList.get(id).getSubTaskIds();
            for (Integer subId : listIdsSubTask) {
                subTaskList.remove(subId);
            }
            epicTaskList.remove(id);
        } else {
            System.out.println("Error 404: Object not found!");
        }
    }

    public HashMap<Integer, Task> getTaskList() {
        return taskList;
    }

    public HashMap<Integer, SubTask> getSubTaskList() {
        return subTaskList;
    }

    public HashMap<Integer, EpicTask> getEpicTaskList() {
        return epicTaskList;
    }
}