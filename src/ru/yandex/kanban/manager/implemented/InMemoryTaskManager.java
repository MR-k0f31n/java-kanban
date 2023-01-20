package ru.yandex.kanban.manager.implemented;

import ru.yandex.kanban.data.enums.TypeTask;
import ru.yandex.kanban.exceptions.ManagerSaveException;
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
    public int addNewTask(Task task) throws ManagerSaveException {
        if (task == null) {
            throw new ManagerSaveException("Задача пуста!");
        }
        task.setId(currencyID++);
        taskMap.put(task.getId(), task);
        validationOfTasksOverTime(task);
        return task.getId();
    }

    @Override
    public int addNewTask(EpicTask epicTask) throws ManagerSaveException {
        if (epicTask == null) {
            throw new ManagerSaveException("Задача пуста!");
        }
        epicTask.setId(currencyID++);
        epicTaskMap.put(epicTask.getId(), epicTask);
        validationOfTasksOverTime(epicTask);
        return epicTask.getId();
    }

    @Override
    public int addNewTask(SubTask subTask) throws ManagerSaveException {
        if (subTask == null) {
            throw new ManagerSaveException("Задача пуста!");
        }
        if (!epicTaskMap.containsKey(subTask.getEpicID())) {
            throw new ManagerSaveException("Такого эпика нет");
        }
        subTask.setId(currencyID++);
        epicTaskMap.get(subTask.getEpicID()).addSubTaskIds(subTask.getId());
        subTaskMap.put(subTask.getId(), subTask);
        syncEpicTaskStatus(subTask.getEpicID());
        validationOfTasksOverTime(subTask);
        return subTask.getId();
    }

    @Override
    public void updateTask(Task newTask) throws ManagerSaveException {
        if (newTask == null) {
            throw new ManagerSaveException("Задача пуста!");
        }
        if (taskMap.containsKey(newTask.getId())) {
            taskMap.put(newTask.getId(), newTask);
        }
    }

    @Override
    public void updateEpicTask(EpicTask newTask) {
        if (newTask == null) {
            throw new ManagerSaveException("Задача пуста!");
        }
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
        if (newTask == null) {
            throw new ManagerSaveException("Задача пуста!");
        }
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
        StarAndEndTimeForEpicTask(epicTaskMap.get(idEpic));
    }

    private void StarAndEndTimeForEpicTask(EpicTask epicTask) {
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
                        if (endSubTask == null) {
                            endSubTask = subTaskTime;
                        }
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
            if (duration != null) {
                epicTask.setEndTime(endSubTask.plus(duration));
            }
        }
    }

    private void validationOfTasksOverTime(Task newTask) throws ManagerSaveException {
        listOfTasksSortedByTime.add(newTask);
        boolean isCrossing = false;
        if (newTask.getStartTime() != null && newTask.getEndTime() != null) {
            LocalDateTime newTaskStartTime = newTask.getStartTime();
            LocalDateTime newTaskEndTime = newTask.getEndTime();
            for (Task task : listOfTasksSortedByTime) {
                LocalDateTime prevStartTime = task.getStartTime();
                LocalDateTime prevEndTime = task.getEndTime();
                if (prevStartTime != null & prevEndTime != null) {
                    if ((newTaskStartTime.isAfter(prevStartTime) && newTaskEndTime.isBefore(prevEndTime))
                            || (newTaskStartTime.isBefore(prevStartTime) && newTaskEndTime.isAfter(prevEndTime))
                            || (newTaskStartTime.isBefore(prevEndTime) && (newTaskEndTime.isAfter(prevEndTime)))) {
                        isCrossing = true;
                    }
                }
            }
            if (isCrossing) {
                throw new ManagerSaveException("Есть пересеченеие по времени старта задач!");
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