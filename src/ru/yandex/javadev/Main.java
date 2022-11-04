package ru.yandex.javadev;

import ru.yandex.javadev.task.EpicTask;
import ru.yandex.javadev.task.Status;
import ru.yandex.javadev.task.SubTask;
import ru.yandex.javadev.task.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Version on Sprint 3");
        Manager manager = new Manager();
        Task task1 = new Task("Не забыть покормить кота Пикселя",
                "200 грамм корма на день ему достаточно");
        Task task2 = new Task("Проверить насколько потолстел Пиксель",
                "Если вес больше 300 грамм, заставить бегать в колесе");
        EpicTask epicTask1 = new EpicTask("Глобальный переезд",
                "Сьезжаем с этой студии начинаем новую жизнь");
        SubTask subTask1 = new SubTask("Собрать чумаданы", "Упаковать в коробки все необходимое");
        SubTask subTask2 = new SubTask("Не забыть пикселя и байта",
                "Перед переездом их нужно покормить!");

        EpicTask epicTask2 = new EpicTask("Поехать провериться к доктору",
                "На всякий случай пройти психиатора");
        SubTask subTask3 = new SubTask("Пройти терапевта", "Рассказать о недуге в левом глазу");
        SubTask subTask4 = new SubTask("Напроситься на визит к психиатру",
                "Уточнить как у меня вообще может что-то получится в программировании");
        SubTask subTask5 = new SubTask("Записаться к Акулисту",
                "От всех этих задач кажется 'Глаз замылился'");

        manager.addNewTask(task1);
        manager.addNewTask(task2);

        manager.addNewEpicTask(epicTask1);
        manager.addNewEpicTask(epicTask2);

        manager.addNewSubTask(subTask1, epicTask1.getId());
        manager.addNewSubTask(subTask2, epicTask1.getId());

        manager.addNewSubTask(subTask3, epicTask2.getId());
        manager.addNewSubTask(subTask4, epicTask2.getId());
        manager.addNewSubTask(subTask5, epicTask2.getId());

        System.out.println(manager.taskList);
        System.out.println(manager.subTaskList);
        System.out.println(manager.epicTaskList);

        Task taskNew = new Task(task1.getId(), "Не забыть покормить кота Пикселя",
                "200 грамм корма на день ему достаточно и байту дать корма", Status.InProgress);
        manager.updateTask(taskNew);
        System.out.println(manager.taskList);

        EpicTask newEpicTask = new EpicTask(epicTask1.getId(), epicTask1.getName(),
                "Сьезжаем с этой студии начинаем новую жизнь вместе со своим питомником", Status.InProgress);
        manager.updateEpicTask(newEpicTask);
        System.out.println(manager.epicTaskList);

        SubTask newSubTask = new SubTask(subTask2.getId(), subTask2.getName(), subTask2.getDescription(), Status.Done);
        manager.updateSubTask(newSubTask);
        System.out.println(manager.epicTaskList);
        System.out.println(manager.subTaskList);
        SubTask newSubTask1 = new SubTask(subTask1.getId(), subTask1.getName(), subTask1.getDescription(), Status.Done);
        manager.updateSubTask(newSubTask1);
        System.out.println(manager.epicTaskList);

        manager.deleteSubTaskListById(8);
        //manager.deleteSubTaskListById(6);
        System.out.println(manager.epicTaskList);

        manager.deleteEpicTaskListById(3);
        System.out.println(manager.epicTaskList);
        System.out.println(manager.subTaskList);
    }
}
