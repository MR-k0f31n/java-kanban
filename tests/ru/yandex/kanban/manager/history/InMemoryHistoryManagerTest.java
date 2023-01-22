package ru.yandex.kanban.manager.history;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.manager.interfaces.HistoryManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager;
    Task taskFirst;
    int idFirstTask;
    Task taskSecond;
    int idSecondTask;

    EpicTask epicTaskFirst;
    int idFirstEpicTask;

    SubTask subTaskFirst;
    int idFirstSubTask;

    @BeforeEach
    protected void createTasksAndHistory() {
        historyManager = new InMemoryHistoryManager();
        taskFirst = new Task(1, "Name Task ONe",
                "Des Task one",
                LocalDateTime.of(2022, 10, 25, 11, 20, 20),
                Status.DONE, 10);
        idFirstTask = taskFirst.getId();

        taskSecond = new Task(2, "Name Task Two",
                "Des Task Two",
                null, Status.IN_PROGRESS, 10);
        idSecondTask = taskSecond.getId();

        epicTaskFirst = new EpicTask(3,"Name EpicTask One",
                "Des EpicTask One",
                LocalDateTime.of(2022, 10, 20, 5, 20, 20),
                Status.NEW, 10);
        idFirstEpicTask = epicTaskFirst.getId();

        subTaskFirst = new SubTask(4,"Name Sub One",
                "Des Sub One",
                LocalDateTime.of(2022, 10, 26, 4, 20, 20),
                Status.NEW, 10,
                idFirstEpicTask);
        idFirstSubTask = subTaskFirst.getId();
    }

    @Test
    public void testAddHistory_correctLinkedList () {

        List<Integer> listExpected = new LinkedList<>();
        List<Integer> listActual = new ArrayList<>();

        listExpected.add(idFirstSubTask);
        listExpected.add(idSecondTask);
        listExpected.add(idFirstTask);
        listExpected.add(idFirstEpicTask);

        historyManager.add(subTaskFirst);
        historyManager.add(taskSecond);
        historyManager.add(taskFirst);
        historyManager.add(epicTaskFirst);

        for (Task task : historyManager.getHistory()) {
            listActual.add(task.getId());
        }
        Assertions.assertEquals(listExpected, listActual, "История не совпадает с задуманной, добавление");
    }

    @Test
    public void testAddDuplicateInHistory_correctLinkedList () {

        List<Integer> listExpected = new LinkedList<>();
        List<Integer> listActual = new ArrayList<>();

        listExpected.add(idFirstSubTask);
        listExpected.add(idFirstTask);
        listExpected.add(idSecondTask);
        listExpected.add(idFirstEpicTask);

        historyManager.add(subTaskFirst);
        historyManager.add(taskSecond);
        historyManager.add(taskFirst);
        historyManager.add(taskSecond);
        historyManager.add(epicTaskFirst);

        for (Task task : historyManager.getHistory()) {
            listActual.add(task.getId());
        }
        Assertions.assertEquals(listExpected, listActual, "История не совпадает с задуманной, добавление дубля");
    }

    @Test
    public void testDeleteFirstInHistory_correctLinkedList () {

        List<Integer> listExpected = new LinkedList<>();
        List<Integer> listActual = new ArrayList<>();

        listExpected.add(idFirstSubTask);
        listExpected.add(idFirstTask);
        listExpected.add(idFirstEpicTask);

        historyManager.add(subTaskFirst);
        historyManager.add(taskSecond);
        historyManager.add(taskFirst);
        historyManager.add(taskSecond);
        historyManager.add(epicTaskFirst);

        historyManager.remove(idSecondTask);

        for (Task task : historyManager.getHistory()) {
            listActual.add(task.getId());
        }
        Assertions.assertEquals(listExpected, listActual, "История не совпадает с задуманной, удалил первый");
    }

    @Test
    public void testDeleteCenterInHistory_correctLinkedList () {

        List<Integer> listExpected = new LinkedList<>();
        List<Integer> listActual = new ArrayList<>();


        listExpected.add(idFirstTask);
        listExpected.add(idSecondTask);
        listExpected.add(idFirstEpicTask);

        historyManager.add(subTaskFirst);
        historyManager.add(taskSecond);
        historyManager.add(taskFirst);
        historyManager.add(taskSecond);
        historyManager.add(epicTaskFirst);

        historyManager.remove(idFirstSubTask);

        for (Task task : historyManager.getHistory()) {
            listActual.add(task.getId());
        }
        Assertions.assertEquals(listExpected, listActual, "История не совпадает с задуманной, удалил середину");
    }

    @Test
    public void testDeleteLastInHistory_correctLinkedList () {

        List<Integer> listExpected = new LinkedList<>();
        List<Integer> listActual = new ArrayList<>();

        listExpected.add(idFirstSubTask);
        listExpected.add(idFirstTask);
        listExpected.add(idSecondTask);

        historyManager.add(subTaskFirst);
        historyManager.add(taskSecond);
        historyManager.add(taskFirst);
        historyManager.add(taskSecond);
        historyManager.add(epicTaskFirst);

        historyManager.remove(idFirstEpicTask);

        for (Task task : historyManager.getHistory()) {
            listActual.add(task.getId());
        }
        Assertions.assertEquals(listExpected, listActual, "История не совпадает с задуманной, удалил последний");
    }

}