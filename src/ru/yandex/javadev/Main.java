package ru.yandex.javadev;

import ru.yandex.javadev.task.EpicTask;
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

    }
}
