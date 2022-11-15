package ru.yandex.javadev;


import ru.yandex.javadev.data.EpicTask;
import ru.yandex.javadev.data.Status;
import ru.yandex.javadev.data.SubTask;
import ru.yandex.javadev.data.Task;
import ru.yandex.javadev.manager.manager;
import ru.yandex.javadev.manager.task.TaskManager;


public class Main {

    public static void main(String[] args) {
        System.out.println("Version on Sprint 3");
        TaskManager taskManager = manager.getDefault();

        Task task1 = new Task("Не забыть покормить кота Пикселя",
                "200 грамм корма на день ему достаточно");
        Task task2 = new Task("Проверить насколько потолстел Пиксель",
                "Если вес больше 300 грамм, заставить бегать в колесе");
        EpicTask epicTask1 = new EpicTask("Глобальный переезд",
                "Сьезжаем с этой студии начинаем новую жизнь");
        SubTask subTask1 = new SubTask("Собрать чумаданы", "Упаковать в коробки все необходимое",
                epicTask1.getId());
        SubTask subTask2 = new SubTask("Не забыть пикселя и байта",
                "Перед переездом их нужно покормить!", epicTask1.getId());

        EpicTask epicTask2 = new EpicTask("Поехать провериться к доктору",
                "На всякий случай пройти психиатора");
        SubTask subTask3 = new SubTask("Пройти терапевта", "Рассказать о недуге в левом глазу",
                epicTask2.getId());
        SubTask subTask4 = new SubTask("Напроситься на визит к психиатру",
                "Уточнить как у меня вообще может что-то получится в программировании", epicTask2.getId());
        SubTask subTask5 = new SubTask("Записаться к Акулисту",
                "От всех этих задач кажется 'Глаз замылился'", epicTask2.getId());

        int feedCat = taskManager.addNewTask(task1);
        int weighHamster = taskManager.addNewTask(task2);

        int moving = taskManager.addNewTask(epicTask1);
        int goToDoctor = taskManager.addNewTask(epicTask2);

        int packBag = taskManager.addNewTask(subTask1);
        int reminder = taskManager.addNewTask(subTask2);

        int listDoctor1 = taskManager.addNewTask(subTask3);
        int listDoctor2 = taskManager.addNewTask(subTask4);
        int listDoctor3 = taskManager.addNewTask(subTask5);

        System.out.println("Выводим спискок  тасков " + taskManager.getAllListTask());
        System.out.println("Выводим спискок  саб тасков " + taskManager.getAllSubTask());
        System.out.println("Выводим спискок  епик тасков " + taskManager.getAllEpicTask());

        Task taskNew = new Task(task1.getId(), "Не забыть покормить кота Пикселя",
                "200 грамм корма на день ему достаточно и байту дать корма", Status.IN_PROGRESS);
        taskManager.updateTask(taskNew);
        System.out.println("Обновили таску " + taskManager.getAllListTask());

        EpicTask newEpicTask = new EpicTask(epicTask1.getId(), epicTask1.getName(),
                "Сьезжаем с этой студии начинаем новую жизнь вместе со своим питомником", Status.IN_PROGRESS);
        taskManager.updateEpicTask(newEpicTask);
        System.out.println("Обновили эпик " + taskManager.getAllEpicTask());

        SubTask newSubTask = new SubTask(subTask4.getId(), subTask4.getName(), subTask4.getDescription(), Status.DONE);
        taskManager.updateSubTask(newSubTask);
        System.out.println("Обновили сабы " + taskManager.getAllEpicTask());
        System.out.println("Обновили сабы " + taskManager.getAllSubTask());
        SubTask newSubTask1 = new SubTask(subTask1.getId(), subTask1.getName(), subTask1.getDescription(),
                Status.IN_PROGRESS);
        taskManager.updateSubTask(newSubTask1);
        System.out.println("Обновили еще раз " + taskManager.getAllEpicTask());

        taskManager.deleteSubTaskById(subTask2.getId());
        taskManager.deleteSubTaskById(subTask4.getId());
        System.out.println("Удаляем сабы " + taskManager.getAllEpicTask());
        System.out.println("Удаляем сабы " + taskManager.getAllSubTask());

        taskManager.deleteEpicTaskById(epicTask1.getId());
        System.out.println("Удалили эпик " + taskManager.getAllEpicTask());
        System.out.println("Удалили эпик " + taskManager.getAllSubTask());
    }
}
