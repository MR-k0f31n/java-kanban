package ru.yandex.kanban.manager.task;

import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.Status;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.manager.history.HistoryManager;
import ru.yandex.kanban.manager.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager history;
    private int currencyID = 1;
    private final HashMap<Integer, Task> taskMap;
    private final HashMap<Integer, SubTask> subTaskMap;
    private final HashMap<Integer, EpicTask> epicTaskMap;

    public InMemoryTaskManager () {
        this.history = Managers.getDefaultHistory();
        this.taskMap = new HashMap<>();
        this.epicTaskMap = new HashMap<>();
        this.subTaskMap = new HashMap<>();
    }

    @Override
    public ArrayList<Task> getAllListTask () { return new ArrayList<>(taskMap.values()); }

    @Override
    public ArrayList<SubTask> getAllSubTask () { return new ArrayList<>(subTaskMap.values()); }

    @Override
    public ArrayList<EpicTask> getAllEpicTask () { return new ArrayList<>(epicTaskMap.values()); }

    @Override
    public void clearAllTask () {
        taskMap.clear();
    }

    @Override
    public void clearAllEpicTask () {
        epicTaskMap.clear();
    }

    @Override
    public void clearAllSubTask () {
        subTaskMap.clear();
    }

    @Override
    public Task getTaskById (int id) {
        if (taskMap.containsKey(id)) {
            history.addHistory(taskMap.get(id));
        }
        return taskMap.get(id);
    }

    @Override
    public EpicTask getEpicById (int id) {
        if (epicTaskMap.containsKey(id)) {
            history.addHistory(epicTaskMap.get(id));
        }
        return epicTaskMap.get(id);
    }

    @Override
    public SubTask getSubById (int id) {
        if (subTaskMap.containsKey(id)) {
            history.addHistory(subTaskMap.get(id));
        }
        return subTaskMap.get(id);
    }

    @Override
    public int addNewTask (Task task) {
        task.setId(currencyID++);
        taskMap.put(task.getId(), task);
        return task.getId();
    }

    @Override
   public int addNewTask (EpicTask epicTask) {
        epicTask.setId(currencyID++);
        epicTaskMap.put(epicTask.getId(), epicTask);
        return epicTask.getId();
    }

    @Override
   public int addNewTask (SubTask subTask) {
       if (epicTaskMap.containsKey(subTask.getEpicID())) {
           subTask.setId(currencyID++);
           epicTaskMap.get(subTask.getEpicID()).addSubTaskIds(subTask.getId());
           subTaskMap.put(subTask.getId(), subTask);
           syncEpicTaskStatus(subTask.getEpicID());
           return subTask.getId();
       } else {
           return -1;
       }
   }

    @Override
    public void updateTask (Task newTask) {
        if (taskMap.containsKey(newTask.getId())) {
            taskMap.put(newTask.getId(), newTask);
        }
    }

    @Override
    public void updateEpicTask (EpicTask newTask) {
        if (epicTaskMap.containsKey(newTask.getId())) {
            if (epicTaskMap.get(newTask.getId()).getStatus().equals(newTask.getStatus())) {
                newTask.setStatus(epicTaskMap.get(newTask.getId()).getStatus());
                // Protected to change the status manually
            }
            epicTaskMap.put(newTask.getId(), newTask);
        }
    }

    @Override
    public void updateSubTask (SubTask newTask) {
        if (subTaskMap.containsKey(newTask.getId())) {
            subTaskMap.put(newTask.getId(), newTask);
            syncEpicTaskStatus(newTask.getEpicID());
        }
    }

    @Override
    public void deleteTaskById (int id) {
        taskMap.remove(id);
    }

    @Override
    public void deleteEpicTaskById (int id) {
        if (epicTaskMap.containsKey(id)) {
            for (Integer subId : epicTaskMap.get(id).getSubTaskIds()) {
                subTaskMap.remove(subId);
            }
            epicTaskMap.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById (int id) {
        if (subTaskMap.containsKey(id)) {
            int epicId = subTaskMap.get(id).getEpicID();
            epicTaskMap.get(epicId).getSubTaskIds().remove((Integer) id);
            subTaskMap.remove(id);
            syncEpicTaskStatus(epicId);
        }
    }

    @Override
    public ArrayList<SubTask> getAllSubByEpicTask (int id) {
        ArrayList <SubTask> subByEpicTaskList = new ArrayList<>();
        if (epicTaskMap.containsKey(id)) {
            List<Integer> subTaskIds = epicTaskMap.get(id).getSubTaskIds();
            for (Integer idSub : subTaskIds) {
                if (idSub != null) {
                    subByEpicTaskList.add(subTaskMap.get(idSub));
                }
            }
        }
        return subByEpicTaskList;
    }

    @Override
    public void syncEpicTaskStatus(int idEpic) {
        int subNew = 0;
        int subDone = 0;

        if (epicTaskMap.containsKey(idEpic)) {
            EpicTask epicTask = epicTaskMap.get(idEpic);
            if (epicTask.getSubTaskIds().size() != 0) {
                for (Integer idSubs : epicTask.getSubTaskIds()) {
                    if (subTaskMap.get(idSubs).getStatus().equals(Status.NEW)) {
                        subNew ++;
                    } else if (subTaskMap.get(idSubs).getStatus().equals(Status.DONE)) {
                        subDone ++;
                    }
                }
                if (epicTask.getSubTaskIds().size() == subNew) {
                    epicTask.setStatus(Status.NEW);
                    epicTaskMap.put(epicTask.getId(), epicTask);
                } else if (epicTask.getSubTaskIds().size() == subDone) {
                    epicTask.setStatus(Status.DONE);
                    epicTaskMap.put(epicTask.getId(), epicTask);
                } else {
                    epicTask.setStatus(Status.IN_PROGRESS);
                    epicTaskMap.put(epicTask.getId(), epicTask);
                }
            } else {
                epicTask.setStatus(Status.NEW);
                epicTaskMap.put(epicTask.getId(), epicTask);
            }
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getHistory();
    }

}