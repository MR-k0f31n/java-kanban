package ru.yandex.javadev;


import ru.yandex.javadev.data.EpicTask;
import ru.yandex.javadev.data.SubTask;
import ru.yandex.javadev.data.Task;


public class Main {

    public static void main(String[] args) {
        System.out.println("Version on Sprint 3");


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

        /*manager.addNewTask(task1);
        manager.addNewTask(task2);

        manager.addNewEpicTask(epicTask1);
        manager.addNewEpicTask(epicTask2);

        manager.addNewSubTask(subTask1, epicTask1.getId());
        manager.addNewSubTask(subTask2, epicTask1.getId());

        manager.addNewSubTask(subTask3, epicTask2.getId());
        manager.addNewSubTask(subTask4, epicTask2.getId());
        manager.addNewSubTask(subTask5, epicTask2.getId());

        System.out.println("Выводим спискок  тасков " + manager.getAllTask());
        System.out.println("Выводим спискок  саб тасков " + manager.getAllSubTask());
        System.out.println("Выводим спискок  епик тасков " + manager.getAllEpicTask());

        Task taskNew = new Task(task1.getId(), "Не забыть покормить кота Пикселя",
                "200 грамм корма на день ему достаточно и байту дать корма", Status.IN_PROGRESS);
        manager.updateTask(taskNew);
        System.out.println("Обновили таску " + manager.getAllTask());

        EpicTask newEpicTask = new EpicTask(epicTask1.getId(), epicTask1.getName(),
                "Сьезжаем с этой студии начинаем новую жизнь вместе со своим питомником", Status.IN_PROGRESS);
        manager.updateEpicTask(newEpicTask);
        System.out.println("Обновили эпик " + manager.getAllEpicTask());

        SubTask newSubTask = new SubTask(subTask4.getId(), subTask4.getName(), subTask4.getDescription(), Status.DONE);
        manager.updateSubTask(newSubTask);
        System.out.println("Обновили сабы " + manager.getAllEpicTask());
        System.out.println("Обновили сабы " + manager.getAllSubTask());
        SubTask newSubTask1 = new SubTask(subTask1.getId(), subTask1.getName(), subTask1.getDescription(),
                Status.IN_PROGRESS);
        manager.updateSubTask(newSubTask1);
        System.out.println("Обновили еще раз " + manager.getAllEpicTask());

        manager.deleteSubTaskById(subTask2.getId());
        manager.deleteSubTaskById(subTask4.getId());
        System.out.println("Удаляем сабы " + manager.getAllEpicTask());
        System.out.println("Удаляем сабы " + manager.getAllSubTask());

        manager.deleteEpicTaskById(epicTask1.getId());
        System.out.println("Удалили эпик " + manager.getAllEpicTask());
        System.out.println("Удалили эпик " + manager.getAllSubTask());*/
    }
}
