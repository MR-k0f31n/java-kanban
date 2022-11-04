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
        SubTask subTask1 = new SubTask("Собрать чумаданы", "Упаковать в коробки все необходимое", 3);
        SubTask subTask2 = new SubTask("Не забыть пикселя и байта",
                "Перед переездом их нужно покормить!", 3);

        EpicTask epicTask2 = new EpicTask("Поехать провериться к доктору",
                "На всякий случай пройти психиатора");
        SubTask subTask3 = new SubTask("Пройти терапевта", "Рассказать о недуге в левом глазу", 4);
        SubTask subTask4 = new SubTask("Напроситься на визит к психиатру",
                "Уточнить как у меня вообще может что-то получится в программировании", 4);
        SubTask subTask5 = new SubTask("Записаться к Акулисту",
                "От всех этих задач кажется 'Глаз замылился'", 4);

        manager.addNewTask(task1);
        manager.addNewTask(task2);

        manager.addNewEpicTask(epicTask1);
        manager.addNewEpicTask(epicTask2);

        manager.addNewSubTask(subTask1);
        manager.addNewSubTask(subTask2);

        manager.addNewSubTask(subTask3);
        manager.addNewSubTask(subTask4);
        manager.addNewSubTask(subTask5);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getSubTaskList());
        System.out.println(manager.getEpicTaskList());

        Task taskNew = new Task(task1.getId(), "Не забыть покормить кота Пикселя",
                "200 грамм корма на день ему достаточно и байту дать корма", Status.IN_PROGRESS);
        manager.updateTask(taskNew);
        System.out.println(manager.getTaskList());

        EpicTask newEpicTask = new EpicTask(epicTask1.getId(), epicTask1.getName(),
                "Сьезжаем с этой студии начинаем новую жизнь вместе со своим питомником", Status.IN_PROGRESS);
        manager.updateEpicTask(newEpicTask);
        System.out.println(manager.getEpicTaskList());

        SubTask newSubTask = new SubTask(subTask2.getId(), subTask2.getName(), subTask2.getDescription(), Status.DONE);
        manager.updateSubTask(newSubTask);
        System.out.println(manager.getEpicTaskList());
        System.out.println(manager.getSubTaskList());
        SubTask newSubTask1 = new SubTask(subTask1.getId(), subTask1.getName(), subTask1.getDescription(), Status.DONE);
        manager.updateSubTask(newSubTask1);
        System.out.println(manager.getEpicTaskList());

        manager.deleteSubTaskById(8);
        manager.deleteSubTaskById(6);
        System.out.println(manager.getEpicTaskList());

        manager.deleteEpicTaskById(3);
        System.out.println(manager.getEpicTaskList());
        System.out.println(manager.getSubTaskList());
    }
}
