package ru.yandex.javadev;

import ru.yandex.javadev.task.EpicTask;
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
        SubTask subTask2 = new SubTask("Не забыть пикселя и байта", "Перед переездом их нужно покормить!");

        manager.addNewTask(task1);
        manager.addNewTask(task2);

        manager.addNewEpicTask(epicTask1);

        manager.addNewSubTask(subTask1);
        manager.addNewSubTask(subTask2);

        System.out.println(manager.taskList);
        System.out.println(manager.subTaskList);
        System.out.println(manager.epicTaskList);
    }
}
