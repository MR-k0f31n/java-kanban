package ru.yandex.javadev.manager.task;

public interface TaskManager {

    Object getAllListTask ();

    void deleteAllTask ();

    void getTaskId (int id);

    void addNewTask (Object object);

    void updateTask (Object object);

    void deleteFromId (int id);

    Object getListSubsFromEpic(int idEpic);

    void syncEpicTaskStatus (int idEpic);
}
