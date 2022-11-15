package ru.yandex.javadev.manager.task;

import ru.yandex.javadev.data.EpicTask;
import ru.yandex.javadev.data.SubTask;
import ru.yandex.javadev.data.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getAllListTask ();

    List<EpicTask> getAllEpicTask();

    List<SubTask> getAllSubTask();

    void clearAllTask();

    void clearAllEpicTask();

    void clearAllSubTask();

    Task getTaskById (Integer id);

    EpicTask getEpicById (Integer id);

    SubTask getSubById(Integer id);

    int addNewTask (Task task);

    int addNewTask (EpicTask epicTask);

    int addNewTask (SubTask subTask);

    void updateTask (Task newTask);

    void updateEpicTask (EpicTask newTask);

    void updateSubTask (SubTask newTask);

    void deleteTaskById (Integer id);

    void deleteEpicTaskById (Integer id);

    void deleteSubTaskById (Integer id);

    Object getAllSubByEpicTask(Integer idEpic);

    void syncEpicTaskStatus (Integer idEpic);
}
