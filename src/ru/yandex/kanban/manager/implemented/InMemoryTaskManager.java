package ru.yandex.kanban.manager.implemented;

import ru.yandex.kanban.manager.interfaces.HistoryManager;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.manager.Managers;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager history;
    protected int currencyID = 1;
    protected final HashMap<Integer, Task> taskMap;
    protected final HashMap<Integer, SubTask> subTaskMap;
    protected final HashMap<Integer, EpicTask> epicTaskMap;
    protected final TreeSet<Task> listOfTasksSortedByTime;
    protected final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm | dd-MM-yy ");

    public InMemoryTaskManager() {
        this.history = Managers.getDefaultHistory();
        this.taskMap = new HashMap<>();
        this.epicTaskMap = new HashMap<>();
        this.subTaskMap = new HashMap<>();
        this.listOfTasksSortedByTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    @Override
    public ArrayList<Task> getAllListTask() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTaskMap.values());
    }

    @Override
    public ArrayList<EpicTask> getAllEpicTask() {
        return new ArrayList<>(epicTaskMap.values());
    }

    @Override
    public void clearAllTask() {
        taskMap.clear();
    }

    @Override
    public void clearAllEpicTask() {
        epicTaskMap.clear();
    }

    @Override
    public void clearAllSubTask() {
        subTaskMap.clear();
    }

    @Override
    public Task getTaskById(int id) {
        if (taskMap.containsKey(id)) {
            history.add(taskMap.get(id));
        }
        return taskMap.get(id);
    }

    @Override
    public EpicTask getEpicById(int id) {
        if (epicTaskMap.containsKey(id)) {
            history.add(epicTaskMap.get(id));
        }
        return epicTaskMap.get(id);
    }

    @Override
    public SubTask getSubById(int id) {
        if (subTaskMap.containsKey(id)) {
            history.add(subTaskMap.get(id));
        }
        return subTaskMap.get(id);
    }

    @Override
    public int addNewTask(Task task) {
        task.setId(currencyID++);
        validationOfTasksOverTime(task);
        taskMap.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int addNewTask(EpicTask epicTask) {
        epicTask.setId(currencyID++);
        validationOfTasksOverTime(epicTask);
        epicTaskMap.put(epicTask.getId(), epicTask);
        return epicTask.getId();
    }

    @Override
    public int addNewTask(SubTask subTask) throws RuntimeException {
        if (epicTaskMap.containsKey(subTask.getEpicID())) {
            throw new RuntimeException ("Такого эпика нет");
        }
            subTask.setId(currencyID++);
            epicTaskMap.get(subTask.getEpicID()).addSubTaskIds(subTask.getId());
            subTaskMap.put(subTask.getId(), subTask);
            syncEpicTaskStatus(subTask.getEpicID());
            validationOfTasksOverTime(subTask);
            return subTask.getId();
    }

    @Override
    public void updateTask(Task newTask) {
        if (taskMap.containsKey(newTask.getId())) {
            taskMap.put(newTask.getId(), newTask);
        }
    }

    @Override
    public void updateEpicTask(EpicTask newTask) {
        if (epicTaskMap.containsKey(newTask.getId())) {
            if (epicTaskMap.get(newTask.getId()).getStatus().equals(newTask.getStatus())) {
                newTask.setStatus(epicTaskMap.get(newTask.getId()).getStatus());
                // Protected to change the status manually
            }
            epicTaskMap.put(newTask.getId(), newTask);
        }
    }

    @Override
    public void updateSubTask(SubTask newTask) {
        if (subTaskMap.containsKey(newTask.getId())) {
            subTaskMap.put(newTask.getId(), newTask);
            syncEpicTaskStatus(newTask.getEpicID());
        }
    }

    @Override
    public void deleteTaskById(int id) {
        history.remove(id);
        taskMap.remove(id);
    }

    @Override
    public void deleteEpicTaskById(int id) {
        if (epicTaskMap.containsKey(id)) {
            for (Integer subId : epicTaskMap.get(id).getSubTaskIds()) {
                subTaskMap.remove(subId);
                history.remove(subId);
            }
            history.remove(id);
            epicTaskMap.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (subTaskMap.containsKey(id)) {
            int epicId = subTaskMap.get(id).getEpicID();
            epicTaskMap.get(epicId).getSubTaskIds().remove((Integer) id);
            subTaskMap.remove(id);
            history.remove(id);
            syncEpicTaskStatus(epicId);
        }
    }

    @Override
    public ArrayList<SubTask> getAllSubByEpicTask(int id) {
        ArrayList<SubTask> subByEpicTaskList = new ArrayList<>();
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

    protected void syncEpicTaskStatus(int idEpic) {
        int subNew = 0;
        int subDone = 0;

        if (epicTaskMap.containsKey(idEpic)) {
            EpicTask epicTask = epicTaskMap.get(idEpic);
            if (epicTask.getSubTaskIds().size() != 0) {
                for (Integer idSubs : epicTask.getSubTaskIds()) {
                    if (subTaskMap.get(idSubs).getStatus().equals(Status.NEW)) {
                        subNew++;
                    } else if (subTaskMap.get(idSubs).getStatus().equals(Status.DONE)) {
                        subDone++;
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

    private void StarAndEndTimeForEpicTask (EpicTask epicTask) {
        LocalDateTime endSubTask;
        LocalDateTime firstSubTask;
        Duration duration;

        for (Integer idSub : epicTask.getSubTaskIds()) {
            SubTask subTask = getSubtaskForMetodNotHistory(idSub);
            List<LocalDateTime> time;
            if(subTask.getStartTime() != null && subTask.getDuration() != null) {
                time = (List<LocalDateTime>) Comparator.comparing(SubTask::getStartTime);
            }
        }

    }

    private EpicTask getEpicForMetodNotHistory(int id) {
        return epicTaskMap.get(id);
    }

    private SubTask getSubtaskForMetodNotHistory(int id) {
        return subTaskMap.get(id);
    }

    private void validationOfTasksOverTime(Task task) throws RuntimeException {
        listOfTasksSortedByTime.add(task);
        LocalDateTime prev = LocalDateTime.MIN;
        for (Task priorityTask : listOfTasksSortedByTime) {
            if (priorityTask.getStartTime() != null) {
                if (prev.isAfter(priorityTask.getStartTime())) {
                    throw new RuntimeException("Задачи " + task.getName() + " и " + priorityTask.getName()
                            + " пересекаются по времени");
                }
                prev = priorityTask.getEndTime().orElse(priorityTask.getStartTime());
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return listOfTasksSortedByTime.stream().toList();
    }
}