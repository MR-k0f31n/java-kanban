package ru.yandex.kanban.manager.implemented;

import ru.yandex.kanban.data.enums.TypeTask;
import ru.yandex.kanban.manager.interfaces.HistoryManager;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.manager.Managers;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HistoryManager history;
    protected int currencyID = 1;
    protected final HashMap<Integer, Task> taskMap;
    protected final HashMap<Integer, SubTask> subTaskMap;
    protected final HashMap<Integer, EpicTask> epicTaskMap;
    protected final TreeSet<Task> listOfTasksSortedByTime;

    public InMemoryTaskManager() {
        this.history = Managers.getDefaultHistory();
        this.taskMap = new HashMap<>();
        this.epicTaskMap = new HashMap<>();
        this.subTaskMap = new HashMap<>();
        this.listOfTasksSortedByTime = new TreeSet<>(Comparator.comparing(Task::getStartTime,
                        Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparingInt(Task::getId));
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
        List<Task> list = new ArrayList<>(listOfTasksSortedByTime);
        for (Task task : list) {
            boolean isTask = task.getTypeTask().equals(TypeTask.TASK);

            if (isTask) {
                listOfTasksSortedByTime.remove(task);
                history.remove(task.getId());
            }
        }
        taskMap.clear();
    }

    @Override
    public void clearAllEpicTask() {
        List<Task> list = new ArrayList<>(listOfTasksSortedByTime);

        for (Task epicTask : list) {
            if (epicTask instanceof EpicTask) {
                List<Integer> subsId = ((EpicTask) epicTask).getSubTaskIds();
                for (Integer idSub : subsId) {
                    listOfTasksSortedByTime.remove(subTaskMap.get(idSub));
                    history.remove(idSub);
                    subTaskMap.remove(idSub);
                }
                listOfTasksSortedByTime.remove(epicTask);
                history.remove(epicTask.getId());
            }
        }
        epicTaskMap.clear();
    }

    @Override
    public void clearAllSubTask() {
        List<Task> list = new ArrayList<>(listOfTasksSortedByTime);

        for (Task subTask : list) {
            if (subTask instanceof SubTask) {
                listOfTasksSortedByTime.remove(subTask);
                history.remove(subTask.getId());
            }
        }
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
        if (validationOfTasksOverTime(task)){
            taskMap.put(task.getId(), task);
            return task.getId();
        } else {
            throw new IllegalArgumentException("Есть пересечение по времени задач! Задача не была добавлена!");
        }
    }

    @Override
    public int addNewTask(EpicTask epicTask) {
        epicTask.setId(currencyID++);
        if (validationOfTasksOverTime(epicTask)) {
            epicTaskMap.put(epicTask.getId(), epicTask);
            return epicTask.getId();
        } else {
            throw new IllegalArgumentException("Есть пересечение по времени задач! Задача не была добавлена!");
        }
    }

    @Override
    public int addNewTask(SubTask subTask) {
        if (!epicTaskMap.containsKey(subTask.getEpicID())) {
            throw new IllegalArgumentException("Такого эпика нет! Задача не была добавлена!");
        }
        subTask.setId(currencyID++);
        if (validationOfTasksOverTime(subTask)) {
            epicTaskMap.get(subTask.getEpicID()).addSubTaskIds(subTask.getId());
            subTaskMap.put(subTask.getId(), subTask);
            syncEpicTaskStatus(subTask.getEpicID());
            return subTask.getId();
        } else {
            throw new RuntimeException("Есть пересечение по времени задач! Задача не была добавлена!");
        }
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
            if (!epicTaskMap.get(newTask.getId()).getStatus().equals(newTask.getStatus())) {
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
        if (taskMap.containsKey(id)) {
            history.remove(id);
            listOfTasksSortedByTime.remove(taskMap.get(id));
            taskMap.remove(id);
        }
    }

    @Override
    public void deleteEpicTaskById(int id) {
        if (epicTaskMap.containsKey(id)) {
            for (Integer subId : epicTaskMap.get(id).getSubTaskIds()) {
                subTaskMap.remove(subId);
                listOfTasksSortedByTime.remove(subTaskMap.get(subId));
                history.remove(subId);
            }
            history.remove(id);
            listOfTasksSortedByTime.remove(epicTaskMap.get(id));
            epicTaskMap.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (subTaskMap.containsKey(id)) {
            int epicId = subTaskMap.get(id).getEpicID();
            epicTaskMap.get(epicId).getSubTaskIds().remove((Integer) id);
            history.remove(id);
            listOfTasksSortedByTime.remove(subTaskMap.get(id));
            subTaskMap.remove(id);
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
                    Status statusSubTask = subTaskMap.get(idSubs).getStatus();
                    if (statusSubTask.equals(Status.NEW)) {
                        subNew++;
                    } else if (statusSubTask.equals(Status.DONE)) {
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
        starAndEndTimeForEpicTask(epicTaskMap.get(idEpic));
    }

    protected void starAndEndTimeForEpicTask(EpicTask epicTask) {
        LocalDateTime endSubTask = null;
        LocalDateTime firstSubTask = null;
        Duration duration;
        int durationInMinutes = 0;

        if (epicTask.getSubTaskIds().size() != 0) {
            LocalDateTime subTaskTime;
            for (int idSub : epicTask.getSubTaskIds()) {
                SubTask subTask = subTaskMap.get(idSub);
                if (subTask.getDuration() != null) {
                    durationInMinutes += subTask.getDuration().toMinutes();
                }
                if (subTask.getStartTime() != null) {
                    subTaskTime = subTask.getStartTime();
                    if (firstSubTask == null) {
                        firstSubTask = subTaskTime;
                        endSubTask = subTaskTime;
                    }
                    if (subTaskTime.isBefore(firstSubTask)) {
                        firstSubTask = subTaskTime;
                    }
                    if (subTaskTime.isAfter(endSubTask)) {
                        endSubTask = subTaskTime;
                    }
                }
            }
            duration = Duration.ofMinutes(durationInMinutes);

            epicTask.setDuration(duration);
            if (firstSubTask != null) {
                epicTask.setStartTime(firstSubTask);
            }
            if (duration != null && endSubTask != null) {
                epicTask.setEndTime(endSubTask.plus(duration));
            }
        }
    }

    protected boolean validationOfTasksOverTime(Task newTask) {
        LocalDateTime prevStartTime = LocalDateTime.MIN;
        LocalDateTime prevEndTime = LocalDateTime.MIN;

        listOfTasksSortedByTime.add(newTask);
        boolean isNotCrossing = true;

        LocalDateTime nextTaskStartTime;
        LocalDateTime nextTaskEndTime;

        TypeTask prevType;
        String prevNameTask;
        for (Task task : listOfTasksSortedByTime) {
            prevType = task.getTypeTask();
            prevNameTask = task.getName();
            if ((task.getStartTime() != null) && (task.getEndTime() != null)) {
                nextTaskStartTime = task.getStartTime();
                nextTaskEndTime = task.getEndTime();

                if ((nextTaskStartTime.isBefore(prevStartTime) && nextTaskEndTime.isAfter(prevStartTime))
                        || (nextTaskStartTime.isAfter(prevStartTime) && nextTaskEndTime.isBefore(prevEndTime))
                        || (nextTaskStartTime.isBefore(prevEndTime) && nextTaskEndTime.isAfter(prevEndTime))) {
                    if (!prevType.equals(TypeTask.EPIC_TASK) && !task.getTypeTask().equals(TypeTask.SUB_TASK)){
                        System.out.println(prevNameTask + " задача пересекается во времени с " + task.getName());
                        isNotCrossing = false;
                    }
                }

                prevStartTime = nextTaskStartTime;
                prevEndTime = nextTaskEndTime;
            }
        }
        return isNotCrossing;
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