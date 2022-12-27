package ru.yandex.kanban.manager;

import ru.yandex.kanban.manager.implemented.FileBackedTasksManager;
import ru.yandex.kanban.manager.implemented.InMemoryTaskManager;
import ru.yandex.kanban.manager.interfaces.HistoryManager;
import ru.yandex.kanban.manager.history.InMemoryHistoryManager;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getInMemory() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        //Спасибо!!!!! Я еще даже не джун, я пелёночник) ресурсов мало знаю.
        return FileBackedTasksManager.loadFromFile(new File("resources", "history.csv"));
        /*не знаю так или нет, вызвать конструктор на пряму вообще не получатся идея жалиться говорит дай паблик
        я уже готов выкинуть ноутбук уволится с курса и уйти в монастырь - правда женский
        логика такая либо файл есть либо нет но тогда его нужно нарисовать другого не дано
        надеюсь мыслю верно*/
    }

}
