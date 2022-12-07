package ru.yandex.kanban;

import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.Status;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.manager.Managers;
import ru.yandex.kanban.manager.task.TaskManager;

public class Main {

    public static void main(String[] args) {

        System.out.println("Version on Sprint 4");
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Не забыть покормить кота Пикселя",
                "200 грамм корма на день ему достаточно");

        int feedCat = taskManager.addNewTask(task1);

        Task task2 = new Task("Проверить насколько потолстел Пиксель",
                "Если вес больше 300 грамм, заставить бегать в колесе");

        int weighHamster = taskManager.addNewTask(task2);

        EpicTask epicTask1 = new EpicTask("Глобальный переезд",
                "Сьезжаем с этой студии начинаем новую жизнь");

        int moving = taskManager.addNewTask(epicTask1);

        SubTask subTask1 = new SubTask("Собрать чумаданы", "Упаковать в коробки все необходимое",
                moving);

        int packBag = taskManager.addNewTask(subTask1);

        SubTask subTask2 = new SubTask("Не забыть пикселя и байта",
                "Перед переездом их нужно покормить!", moving);

        int reminder = taskManager.addNewTask(subTask2);

        EpicTask epicTask2 = new EpicTask("Поехать провериться к доктору",
                "На всякий случай пройти психиатора");

        int goToDoctor = taskManager.addNewTask(epicTask2);

        SubTask subTask3 = new SubTask("Пройти терапевта", "Рассказать о недуге в левом глазу",
                goToDoctor);

        int listDoctor1 = taskManager.addNewTask(subTask3);

        SubTask subTask4 = new SubTask("Напроситься на визит к психиатру",
                "Уточнить как у меня вообще может что-то получится в программировании", goToDoctor);

        int listDoctor2 = taskManager.addNewTask(subTask4);

        SubTask subTask5 = new SubTask("Записаться к Акулисту",
                "От всех этих задач кажется 'Глаз замылился'", goToDoctor);

        int listDoctor3 = taskManager.addNewTask(subTask5);

        System.out.println("Выводим спискок  тасков " + taskManager.getAllListTask());
        System.out.println("Выводим спискок  епик тасков " + taskManager.getAllEpicTask());
        System.out.println("Выводим спискок  саб тасков " + taskManager.getAllSubTask());

        Task taskNew = taskManager.getTaskById(feedCat);
        taskNew.setName("Не забыть покормить кота Пикселя");
        taskNew.setDescription("200 грамм корма на день ему достаточно и байту дать корма");
        taskNew.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(taskNew);

        System.out.println("Обновили таску " + taskManager.getAllListTask());

        taskManager.deleteTaskById(weighHamster);

        EpicTask newEpicTask = taskManager.getEpicById(moving);
        newEpicTask.setDescription("Сьезжаем с этой студии начинаем новую жизнь вместе со своим питомником");
        taskManager.updateEpicTask(newEpicTask);

        System.out.println("Обновили эпик " + taskManager.getAllEpicTask());
        System.out.println("Обновили эпик " + taskManager.getAllSubTask());

        SubTask newSubTask = taskManager.getSubById(listDoctor2);
        newSubTask.setStatus(Status.DONE);
        taskManager.updateSubTask(newSubTask);

        EpicTask newEpicTask1 = taskManager.getEpicById(moving);
        newEpicTask1.setDescription("Сьезжаем с этой студии начинаем новую жизнь вместе со своим питомником");
        taskManager.updateEpicTask(newEpicTask1);

        System.out.println("History" + taskManager.getHistory());

        System.out.println("Обновили сабы " + taskManager.getAllEpicTask());
        System.out.println("Обновили сабы " + taskManager.getAllSubTask());


        SubTask newSubTask1 = taskManager.getSubById(packBag);
        newEpicTask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(newSubTask1);

        SubTask newSubTask2 = taskManager.getSubById(listDoctor3);
        newSubTask2.setStatus(Status.DONE);
        taskManager.updateSubTask(newSubTask2);

        SubTask newSubTask3 = taskManager.getSubById(listDoctor1);
        newSubTask3.setStatus(Status.DONE);
        taskManager.updateSubTask(newSubTask3);


        System.out.println("Обновили еще раз " + taskManager.getAllEpicTask());
        System.out.println("History" + taskManager.getHistory());

        taskManager.deleteSubTaskById(reminder);
        taskManager.deleteSubTaskById(listDoctor2);

        System.out.println("Удаляем сабы " + taskManager.getAllEpicTask());
        System.out.println("Удаляем сабы " + taskManager.getAllSubTask());
        System.out.println("History" + taskManager.getHistory());

        taskManager.deleteEpicTaskById(moving);

        //test history
        taskManager.getEpicById(goToDoctor);
        taskManager.getTaskById(feedCat);
        taskManager.deleteEpicTaskById(goToDoctor);


        System.out.println("Удалили эпик " + taskManager.getAllEpicTask());
        System.out.println("Удалили эпик " + taskManager.getAllSubTask());
        System.out.println("History" + taskManager.getHistory());
    }
}
