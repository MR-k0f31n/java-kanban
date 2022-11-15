package ru.yandex.javadev.manager.task;

import ru.yandex.javadev.data.EpicTask;
import ru.yandex.javadev.data.Status;
import ru.yandex.javadev.data.SubTask;
import ru.yandex.javadev.data.Task;
import ru.yandex.javadev.manager.history.HistoryManager;
import ru.yandex.javadev.manager.manager;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    HistoryManager history = manager.getDefaultHistory();
    private static int currencyID = 1;

    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTaskList = new HashMap<>();

    public ArrayList<Task> getAllListTask () { return new ArrayList<>(taskList.values()); }

    public ArrayList<SubTask> getAllSubTask () { return new ArrayList<>(subTaskList.values()); }

    public ArrayList<EpicTask> getAllEpicTask () { return new ArrayList<>(epicTaskList.values()); }

    public void clearAllTask () {
        taskList.clear();
    }

    public void clearAllEpicTask () {
        epicTaskList.clear();
    }

    public void clearAllSubTask () {
        subTaskList.clear();
    }

    public Task getTaskById (Integer id) {
        history.add(taskList.get(id));
        return taskList.get(id);
    }

    public EpicTask getEpicById (Integer id) {
        history.add(epicTaskList.get(id));
        return epicTaskList.get(id);
    }

    public SubTask getSubById (Integer id) {
        history.add(subTaskList.get(id));
        return subTaskList.get(id);
    }

    public int addNewTask (Task task) {
        task.setId(currencyID++);
        taskList.put(task.getId(), task);
        return task.getId();
    }
   public int addNewTask (EpicTask epicTask) {
        epicTask.setId(currencyID++);
        epicTaskList.put(epicTask.getId(), epicTask);
        return epicTask.getId();
    }

   public int addNewTask (SubTask subTask) {
       if (epicTaskList.containsKey(subTask.getEpicID())) {
           subTask.setId(currencyID++);
           epicTaskList.get(subTask.getEpicID()).addSubTaskIds(subTask.getId());
           subTaskList.put(subTask.getId(), subTask);
           syncEpicTaskStatus(subTask.getEpicID());
           return subTask.getId();
       } else {
           return -1;
       }
   }

    public void updateTask (Task newTask) {
        if (taskList.containsKey(newTask.getId())) {
            taskList.put(newTask.getId(), newTask);
        }
    }

    public void updateEpicTask (EpicTask newTask) {
        if (epicTaskList.containsKey(newTask.getId())) {
            if (epicTaskList.get(newTask.getId()).getSubTaskIds().size() != 0) {
                newTask.setSubTaskIds(epicTaskList.get(newTask.getId()).getSubTaskIds());
            }
            if (epicTaskList.get(newTask.getId()).getStatus().equals(newTask.getStatus())) {
                newTask.setStatus(epicTaskList.get(newTask.getId()).getStatus());
                // Protected to change the status manually
            }
            epicTaskList.put(newTask.getId(), newTask);
        }
    }

    public void updateSubTask (SubTask newTask) {
        if (subTaskList.containsKey(newTask.getId())) {
            subTaskList.put(newTask.getId(), newTask);
            syncEpicTaskStatus(newTask.getEpicID());
        }
    }

    public void deleteTaskById (Integer id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
        }
    }

    public void deleteEpicTaskById (Integer id) {
        if (epicTaskList.containsKey(id)) {
            for (Integer subId : epicTaskList.get(id).getSubTaskIds()) {
                subTaskList.remove(subId);
            }
            epicTaskList.remove(id);
        }
    }

    public void deleteSubTaskById (Integer id) {
        if (subTaskList.containsKey(id)) {
            SubTask oldSubTask = subTaskList.get(id);
            epicTaskList.get(oldSubTask.getEpicID()).removeIdsSubTask(id);
            subTaskList.remove(id);
            syncEpicTaskStatus(oldSubTask.getEpicID());
        }
    }

    public ArrayList<SubTask> getAllSubByEpicTask (Integer id) {
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

    public void syncEpicTaskStatus(Integer idEpic) {
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


}