package ru.yandex.javadev;
import ru.yandex.javadev.task.EpicTask;
import ru.yandex.javadev.task.SubTask;
import ru.yandex.javadev.task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    protected int currencyID = 1;
    protected String[] status = {"New", "InProgress", "Done"};

    protected HashMap<Integer, Task> taskList = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    protected HashMap<Integer, EpicTask> epicTaskList = new HashMap<>();

    public void addNewTask (Task task) {
        task.setId(currencyID++);
        task.setStatus(status[0]);
        taskList.put(task.getId(), task);
    }

    public void addNewEpicTask (EpicTask epicTask) {
        epicTask.setId(currencyID++);
        epicTask.setStatus(status[0]);
        epicTaskList.put(epicTask.getId(), epicTask);
    }

    public void addNewSubTask (SubTask subTask){
        subTask.setId(currencyID++);
        subTask.setStatus(status[0]);
        subTask.setSubTask();
        subTaskList.put(subTask.getId(), subTask);
    }

    public void syncTaskID (EpicTask epicTask) {
        for (int epicID : epicTask.getSubTaskIDs()) {
            SubTask subTask = subTaskList.get(epicID);
        }
    }

    public ArrayList<String> getAllSubByEpicTask (int id) {
        ArrayList <String> subByEpicTaskList = new ArrayList<>();
        if (epicTaskList.containsKey(id)) {
            ArrayList<Integer> SubTaskIDs = epicTaskList.get(id).getSubTaskIDs();
            for (Integer idSub : SubTaskIDs) {
                if (idSub != null) {
                    subByEpicTaskList.add(epicTaskList.get(idSub).getName());
                }
            }
        } else {
            System.out.println("Error 404: Task not found");
        }
        return subByEpicTaskList;
    }
    public void updateStatusTask (int id) {
       if (taskList.containsKey(id)) {
           Task oldTask = taskList.get(id);
           switch (oldTask.getStatus()) {
               case "New":
                   oldTask.setStatus(status[1]);
                   break;
               case "InProgress":
                   oldTask.setStatus(status[2]);
                   break;
               default:
                   System.out.println("Task is done");
           }
           taskList.put(oldTask.getId(), oldTask);
       } else {
           System.out.println("Error 404: Task not found");
       }
    }

    public void updateStatusEpicTask (int id) {
        if (epicTaskList.containsKey(id)) {
            EpicTask oldEpicTask = epicTaskList.get(id);
            switch (oldEpicTask.getStatus()) {
                case "New":
                    oldEpicTask.setStatus(status[1]);
                    break;
                case "InProgress":
                    oldEpicTask.setStatus(status[2]);
                    break;
                default:
                    System.out.println("Task is done");
            }
            epicTaskList.put(oldEpicTask.getId(), oldEpicTask);
        } else {
            System.out.println("Error 404: Task not found");
        }
    }

    public void updateStatusSubTask (int id) {
        if (subTaskList.containsKey(id)) {
            SubTask oldSubTask = subTaskList.get(id);
            switch (oldSubTask.getStatus()) {
                case "New":
                    oldSubTask.setStatus(status[1]);
                    break;
                case "InProgress":
                    oldSubTask.setStatus(status[2]);
                    break;
                default:
                    System.out.println("Task is done");
            }
            subTaskList.put(oldSubTask.getId(), oldSubTask);
        } else {
            System.out.println("Error 404: Task not found");
        }
    }

    public void updateTask (Task newTask) {
        if (taskList.containsKey(newTask.getId())) {
            Task oldTask = taskList.get(newTask.getId());
            taskList.put(oldTask.getId(), newTask);
        }  else {
            System.out.println("Error 404: Task not found");
        }
    }

    public void updateEpicTask (EpicTask newTask) {
        if (epicTaskList.containsKey(newTask.getId())) {
            Task oldTask = epicTaskList.get(newTask.getId());
            epicTaskList.put(oldTask.getId(), newTask);
        }  else {
            System.out.println("Error 404: Task not found");
        }
    }

    public void updateSubTask (SubTask newTask) {
        if (subTaskList.containsKey(newTask.getId())) {
            Task oldTask = subTaskList.get(newTask.getId());
            subTaskList.put(oldTask.getId(), newTask);
        }  else {
            System.out.println("Error 404: Task not found");
        }
    }

    public ArrayList<String> getAllTask () {
        ArrayList<String> listAllTaskName = new ArrayList<>();
        for (Integer taskID : taskList.keySet()) {
            listAllTaskName.add(taskList.get(taskID).getName());
        }
        return listAllTaskName;
    }

    public ArrayList<String> getAllSubTask () {
        ArrayList<String> listAllSubTaskName = new ArrayList<>();
        for (Integer taskID : subTaskList.keySet()) {
            listAllSubTaskName.add(subTaskList.get(taskID).getName());
        }
        return listAllSubTaskName;
    }

    public ArrayList<String> getAllEpicTask () {
        ArrayList<String> listAAllEpicTaskName = new ArrayList<>();
        for (Integer taskID : epicTaskList.keySet()) {
            listAAllEpicTaskName.add(epicTaskList.get(taskID).getName());
        }
        return listAAllEpicTaskName;
    }

    public void clearAllTask () {
        taskList.clear();
        epicTaskList.clear();
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

    public void deleteSubTaskListById (int id) {
        if (subTaskList.containsKey(id)) {
            subTaskList.remove(id);
        } else {
            System.out.println("Error 404: Object not found!");
        }
    }

    public void deleteEpicTaskListById (int id) {
        if (epicTaskList.containsKey(id)) {
            epicTaskList.remove(id);
        } else {
            System.out.println("Error 404: Object not found!");
        }
    }
}