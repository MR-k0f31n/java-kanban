package java.kanban.manager.task;

import java.kanban.data.EpicTask;
import java.kanban.data.SubTask;
import java.kanban.data.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getAllListTask ();

    List<EpicTask> getAllEpicTask();

    List<SubTask> getAllSubTask();

    void clearAllTask();

    void clearAllEpicTask();

    void clearAllSubTask();

    Task getTaskById (int id);

    EpicTask getEpicById (int id);

    SubTask getSubById(int id);

    int addNewTask (Task task);

    int addNewTask (EpicTask epicTask);

    int addNewTask (SubTask subTask);

    void updateTask (Task newTask);

    void updateEpicTask (EpicTask newTask);

    void updateSubTask (SubTask newTask);

    void deleteTaskById (int id);

    void deleteEpicTaskById (int id);

    void deleteSubTaskById (int id);

    ArrayList<SubTask> getAllSubByEpicTask(int idEpic);

    void syncEpicTaskStatus (int idEpic);

    ArrayList<Task> getHistory();
}
