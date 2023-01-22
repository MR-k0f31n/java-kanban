package ru.yandex.kanban;

import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.manager.Managers;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        System.out.println("Start kanban from sprint 7");

        TaskManager fileBackedTasksManager = Managers.getDefault();

        EpicTask epicTaskFirst = new EpicTask("Name EpicTask One",
                "Des EpicTask One",
                LocalDateTime.of(2022, 10, 20, 5, 20, 20), 10);
        int idOne = fileBackedTasksManager.addNewTask(epicTaskFirst);


        SubTask subTaskFirst = new SubTask("Name Sub One",
                "Des Sub One",
                LocalDateTime.of(2022, 10, 26, 4, 20, 20), 10,
                idOne);
        int idSubTaskOne = fileBackedTasksManager.addNewTask(subTaskFirst);


        SubTask subTaskSecond = new SubTask("Name Sub Two",
                "Des EpicTask Two",
                LocalDateTime.of(2022, 10, 29, 9, 20, 20), 10,
                idOne);
        int idSubTaskTwo = fileBackedTasksManager.addNewTask(subTaskSecond);



        SubTask subTaskFirstDone = new SubTask(idSubTaskOne, "Name Sub One",
                "Des Sub One",
                LocalDateTime.of(2022, 10, 26, 4, 20, 20), Status.DONE, 10,
                idOne);
        fileBackedTasksManager.updateSubTask(subTaskFirstDone);


        SubTask subTaskSecondDone = new SubTask(idSubTaskTwo, "Name Sub Two",
                "Des EpicTask Two",
                LocalDateTime.of(2022, 10, 29, 9, 20, 20), Status.DONE, 10,
                idOne);
        fileBackedTasksManager.updateSubTask(subTaskSecondDone);

        System.out.println(fileBackedTasksManager.getAllEpicTask());
        System.out.println(fileBackedTasksManager.getAllSubTask());

    }
}
