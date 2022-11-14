package ru.yandex.javadev.manager.task;

import ru.yandex.javadev.data.EpicTask;
import ru.yandex.javadev.data.Status;
import ru.yandex.javadev.data.SubTask;
import ru.yandex.javadev.data.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private static int currencyID = 1;

    private static HashMap<Integer, Task> taskList = new HashMap<>();
    private static HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private static HashMap<Integer, EpicTask> epicTaskList = new HashMap<>();


    public static void addNewTask (Task task) {
        task.setId(currencyID++);
        taskList.put(task.getId(), task);
    }


    public static void addNewTask (EpicTask epicTask) {
        epicTask.setId(currencyID++);
        epicTaskList.put(epicTask.getId(), epicTask);
    }

    public static void addNewTask (SubTask subTask, Integer idEpic){
        if (epicTaskList.containsKey(idEpic)) {
            subTask.setId(currencyID++);
            epicTaskList.get(idEpic).addSubTaskIds(subTask.getId());
            subTask.setEpicID(idEpic);
            subTaskList.put(subTask.getId(), subTask);
            syncEpicTaskStatus(subTask.getEpicID());
        } else {
            System.out.println("Error 404: Task not found");
        }
    }

    public static void syncEpicTaskStatus (Integer idEpic) {
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

    public static ArrayList<SubTask> getAllSubByEpicTask (int id) {
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

    public static void updateTask (Task newTask) {
        if (taskList.containsKey(newTask.getId())) {
            taskList.put(newTask.getId(), newTask);
        }
    }

    public static void updateEpicTask (EpicTask newTask) {
        if (epicTaskList.containsKey(newTask.getId())) {
            EpicTask oldTask = epicTaskList.get(newTask.getId());
            if (oldTask.getSubTaskIds().size() != 0) {
                newTask.updateSubTaskIds(epicTaskList.get(newTask.getId()).getSubTaskIds());
                // To translate a collection's
            }
            if (!oldTask.getStatus().equals(newTask.getStatus())) {
                newTask.setStatus(oldTask.getStatus());
                // Prohibition to change the status manually
            }
            epicTaskList.put(oldTask.getId(), newTask);
        }
    }

    public static void updateSubTask (SubTask newTask) {
        if (subTaskList.containsKey(newTask.getId())) {
            newTask.setEpicID(subTaskList.get(newTask.getId()).getEpicID());
            subTaskList.put(newTask.getId(), newTask);
            syncEpicTaskStatus(newTask.getEpicID());
        }
    }

    public ArrayList<Task> getAllTask () { return new ArrayList(taskList.values()); }

    public ArrayList<SubTask> getAllSubTask () { return new ArrayList<>(subTaskList.values()); }

    public ArrayList<EpicTask> getAllEpicTask () { return new ArrayList<>(epicTaskList.values()); }

    public static void clearAll () {
        System.out.println("ВЫ уверены?");
        clearAllTask();
        clearAllEpicTask();
        clearAllSubTask();
    }

    public static void clearAllTask () {
        taskList.clear();
    }

    public static void clearAllEpicTask () {
        epicTaskList.clear();
    }

    public static void clearAllSubTask () {
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

    public static void deleteTaskById (int id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
        } else {
            System.out.println("Error 404: Object not found!");
        }
    }

    public static void deleteSubTaskById (Integer id) {
        if (subTaskList.containsKey(id)) {
            SubTask oldSubTask = subTaskList.get(id);
            epicTaskList.get(oldSubTask.getEpicID()).removeIdsSubTask(id);
            // without this method there will be an exception null
            subTaskList.remove(id);
            // remove (Object key)
            /* на английском не силен, сюда что только не пробовал передавать все работает */
            syncEpicTaskStatus(oldSubTask.getEpicID());
        } else {
            System.out.println("Error 404: Object not found!");
        }
    }

    public static void deleteEpicTaskById (int id) {
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

    @Override
    public void addNewTask(Task task) {
        if (task != null) {
            task.setId(currencyID++);
            taskList.put(((Task) task).getId(), ((Task) task));
        }
        if ((task != null) && (task instanceof EpicTask)) {
            ((EpicTask) task).setId(currencyID++);
            epicTaskList.put(((EpicTask) task).getId(), ((EpicTask) task));
        }
        if ((task != null) && (task instanceof SubTask)) {
            if (epicTaskList.containsKey(((SubTask) task).getEpicID())) {
                ((SubTask) task).setId(currencyID++);
                epicTaskList.get(((SubTask) task).getEpicID()).addSubTaskIds(((SubTask) task).getId());
                ((SubTask) task).setEpicID(((SubTask) task).getEpicID());
                subTaskList.put(((SubTask) task).getId(), ((SubTask) task));
                syncEpicTaskStatus(((SubTask) task).getEpicID());
            } else {
                System.out.println("Error 404: Task not found");
            }
        }
    }
}