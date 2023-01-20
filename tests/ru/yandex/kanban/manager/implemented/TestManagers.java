package ru.yandex.kanban.manager.implemented;

import org.junit.jupiter.api.*;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.exceptions.ManagerSaveException;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

abstract class TestManagers<T extends TaskManager> {

    abstract T createManager();

    public T manager;
    File file = new File("resources", "history.csv");


    @BeforeEach
    public void create() {
        manager = createManager();
    }

    @AfterEach
    public void afterEach() throws IOException {
        try {
            new FileWriter(file, false).close();
        } catch (IOException exception) {
            throw new IOException(exception.getMessage());
        }
    }

    @Test
    public void testPriorityTask_returnSortTask() {

        List<Task> taskPriority = new LinkedList<>();

        final EpicTask epicOne = new EpicTask(
                "EpicOne name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 20, 12, 25),
                15
        );

        final int idEpicOne = manager.addNewTask(epicOne);

        Task taskOne = new Task(
                "Task name",
                "Task des description",
                LocalDateTime.of(2021, 12, 19, 10, 25),
                15
        );

        manager.addNewTask(taskOne);


        Task taskSecond = new Task(
                "TaskSecond name",
                "Task des description",
                LocalDateTime.of(2021, 12, 19, 10, 40),
                15
        );

        manager.addNewTask(taskSecond);

        SubTask subOne = new SubTask(
                "SubOne name",
                "SubOne des description",
                LocalDateTime.of(2022, 12, 21, 20, 25),
                15,
                idEpicOne
        );

        manager.addNewTask(subOne);

        SubTask subTwo = new SubTask(
                "SubTwo name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 20, 9, 25),
                15,
                idEpicOne
        );

        manager.addNewTask(subTwo);

        SubTask subFour = new SubTask(
                "SubFour name",
                "SubThree des description",
                LocalDateTime.of(2022, 11, 19, 10, 30),
                15,
                idEpicOne
        );

        manager.addNewTask(subFour);

        SubTask subThree = new SubTask(
                "SubThree name",
                "SubThree des description",
                null,
                15,
                idEpicOne
        );

        manager.addNewTask(subThree);


        taskPriority.add(taskOne);
        taskPriority.add(taskSecond);
        taskPriority.add(epicOne);
        taskPriority.add(subFour);
        taskPriority.add(subTwo);
        taskPriority.add(subOne);
        taskPriority.add(subThree);
        Assertions.assertEquals(manager.getPrioritizedTasks(), taskPriority, "Приоритеты неверны");
    }

    @Test
    public void testTrowsTimeOverlay_returnTrows() {
        Task taskFirst = new Task(
                "TaskFirst name",
                "Task des description",
                LocalDateTime.of(2021, 12, 21, 10, 20),
                15
        );

        Task taskSecond = new Task(
                "TaskSecond name",
                "Task des description",
                LocalDateTime.of(2021, 12, 21, 10, 25),
                15
        );

        manager.addNewTask(taskFirst);
        Assertions.assertThrows(ManagerSaveException.class, () -> manager.addNewTask(taskSecond));
    }

    @Test
    public void testGetAllTaskList() {
        List<Task> listTest = new ArrayList<>();
        Assertions.assertEquals(listTest, manager.getAllListTask(), "В менеджере что-то есть 0_0");

        Task taskFirst = new Task(
                "TaskFirst name",
                "Task des description",
                LocalDateTime.of(2021, 12, 21, 10, 20),
                15
        );

        Task taskSecond = new Task(
                "TaskSecond name",
                "Task des description",
                LocalDateTime.of(2021, 12, 18, 10, 25),
                15
        );

        manager.addNewTask(taskFirst);
        manager.addNewTask(taskSecond);
        listTest.add(taskFirst);
        listTest.add(taskSecond);

        Assertions.assertEquals(listTest, manager.getAllListTask());
    }

    @Test
    public void testGetAllSubTask() {
        final List<Task> taskList = new ArrayList<>();
        final List<Task> subTaskList = new ArrayList<>();

        final EpicTask epicOne = new EpicTask(
                "EpicOne name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 20, 12, 25),
                15
        );

        final int idEpicOne = manager.addNewTask(epicOne);
        taskList.add(epicOne);

        final SubTask subTaskOne = new SubTask(
                "OneSub name",
                "SubOne des",
                LocalDateTime.of(2022, 12, 18, 13, 25),
                15,
                idEpicOne
        );

        manager.addNewTask(subTaskOne);
        taskList.add(subTaskOne);
        subTaskList.add(subTaskOne);

        Assertions.assertNotEquals(taskList, manager.getAllSubTask());
        Assertions.assertEquals(subTaskList, manager.getAllSubTask());
    }

    @Test
    public void testGetAllEpicTaskList() {
        List<Task> taskListTest = new ArrayList<>();

        final EpicTask epicOne = new EpicTask(
                "EpicOne name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 20, 12, 25),
                15
        );

        manager.addNewTask(epicOne);
        taskListTest.add(epicOne);

        final EpicTask epicSecond = new EpicTask(
                "EpicSecond name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 11, 20, 12, 25),
                15
        );

        manager.addNewTask(epicSecond);
        taskListTest.add(epicSecond);

        Assertions.assertEquals(taskListTest, manager.getAllEpicTask());
    }

    @Test
    public void testClearMapTask_MapEmptyAndEmptyListPriority() {

        Task taskFirst = new Task(
                "TaskFirst name",
                "Task des description",
                LocalDateTime.of(2021, 12, 21, 10, 20),
                15
        );

        Task taskSecond = new Task(
                "TaskSecond name",
                "Task des description",
                LocalDateTime.of(2021, 12, 18, 10, 25),
                15
        );

        manager.addNewTask(taskFirst);
        manager.addNewTask(taskSecond);

        Assertions.assertEquals(2, manager.getPrioritizedTasks().size());
        Assertions.assertEquals(2, manager.getAllListTask().size());

        manager.clearAllTask();

        Assertions.assertEquals(0, manager.getAllListTask().size());
        Assertions.assertEquals(0, manager.getPrioritizedTasks().size());
    }

    @Test
    public void testClearMapSubTask_MapEmptyAndEmptyListPriority() {
        final EpicTask epicOne = new EpicTask(
                "EpicOne name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 20, 12, 25),
                15
        );

        final int idEpicOne = manager.addNewTask(epicOne);

        final SubTask subTaskOne = new SubTask(
                "OneSub name",
                "SubOne des",
                LocalDateTime.of(2022, 12, 18, 13, 25),
                15,
                idEpicOne
        );

        manager.addNewTask(subTaskOne);

        Assertions.assertEquals(2, manager.getPrioritizedTasks().size());
        Assertions.assertEquals(1, manager.getAllSubTask().size());
        Assertions.assertEquals(1, manager.getAllEpicTask().size());

        manager.clearAllSubTask();

        Assertions.assertEquals(1, manager.getPrioritizedTasks().size());
        Assertions.assertEquals(0, manager.getAllSubTask().size());
        Assertions.assertEquals(1, manager.getAllEpicTask().size());
    }

    @Test
    public void testClearMapEpicTask_MapEmptyAndEmptyListPriority() {
        final EpicTask epicOne = new EpicTask(
                "EpicOne name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 20, 12, 25),
                15
        );

        final int idEpicOne = manager.addNewTask(epicOne);

        final SubTask subTaskOne = new SubTask(
                "OneSub name",
                "SubOne des",
                LocalDateTime.of(2022, 12, 18, 13, 25),
                15,
                idEpicOne
        );

        manager.addNewTask(subTaskOne);

        Assertions.assertEquals(2, manager.getPrioritizedTasks().size());
        Assertions.assertEquals(1, manager.getAllSubTask().size());
        Assertions.assertEquals(1, manager.getAllEpicTask().size());

        manager.clearAllEpicTask();

        Assertions.assertEquals(0, manager.getPrioritizedTasks().size());
        Assertions.assertEquals(0, manager.getAllSubTask().size());
        Assertions.assertEquals(0, manager.getAllEpicTask().size());
    }

    @Test
    public void testAddNullTask() {
        Task taskOne = null;
        EpicTask epicTask = null;
        SubTask subTask = null;

        Assertions.assertThrows(ManagerSaveException.class, () -> manager.addNewTask(taskOne));
        Assertions.assertThrows(ManagerSaveException.class, () -> manager.addNewTask(epicTask));
        Assertions.assertThrows(ManagerSaveException.class, () -> manager.addNewTask(subTask));
    }

    @Test
    public void testExemptionNonEpicForSubTask() {
        final SubTask subTaskOne = new SubTask(
                "OneSub name",
                "SubOne des",
                LocalDateTime.of(2022, 12, 18, 13, 25),
                15,
                3
        );

        Assertions.assertThrows(ManagerSaveException.class, () -> manager.addNewTask(subTaskOne));
    }

    @Test
    public void testUpdateTask_equalsNewTask() {
        Task taskFirst = new Task(
                "TaskFirst name",
                "Task des description",
                LocalDateTime.of(2021, 12, 21, 10, 20),
                15
        );

        int idOne = manager.addNewTask(taskFirst);

        Task newTask = new Task(
                idOne,
                "New Task name",
                "Task des description",
                LocalDateTime.of(2021, 12, 18, 10, 25),
                Status.NEW,
                15
        );

        manager.updateTask(newTask);

        Assertions.assertEquals(newTask, manager.getTaskById(idOne), "Задача не обновилась!");

        Task newTaskNull = null;

        Assertions.assertThrows(ManagerSaveException.class, () -> manager.addNewTask(newTaskNull));
    }

    @Test
    public void testUpdateStatusEpic() {
        final EpicTask epicOne = new EpicTask(
                "EpicOne name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 31, 12, 25),
                15
        );

        int idEpic = manager.addNewTask(epicOne);

        Assertions.assertEquals(Status.NEW, manager.getEpicById(idEpic).getStatus(), "Статус не новый");

        final SubTask subTaskFirst = new SubTask(
                "FirstSub name",
                "SubOne des",
                LocalDateTime.of(2022, 12, 18, 13, 25),
                15,
                idEpic
        );

        final SubTask subTaskSecond = new SubTask(
                "SecondSub name",
                "SubOne des",
                LocalDateTime.of(2022, 12, 20, 13, 25),
                15,
                idEpic
        );

        final SubTask subTaskThree = new SubTask(
                "ThreeSub name",
                "SubOne des",
                LocalDateTime.of(2022, 12, 25, 13, 25),
                15,
                idEpic
        );

        manager.getSubById(subTaskFirst.getId());
        manager.getSubById(subTaskSecond.getId());
        manager.getSubById(subTaskThree.getId());

        Assertions.assertEquals(Status.NEW, manager.getEpicById(idEpic).getStatus(),
                "Добавили сабов, статус не новый");


        final EpicTask epicOneNewStatus = new EpicTask(
                idEpic,
                "EpicOne name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 31, 12, 25),
                Status.DONE,
                15
        );

        manager.updateEpicTask(epicOneNewStatus);

        Assertions.assertEquals(epicOne.getStatus(), manager.getEpicById(idEpic).getStatus(),
                "Пытаемсы обновить статус руками, статус обновился");
    }

    @Test
    public void testHistoryCreate_returnHistory() {
        Assertions.assertEquals(0, manager.getHistory().size(), "Там кто-то есть 0_0");

        Task taskSecond = new Task(
                "TaskSecond name",
                "Task des description",
                LocalDateTime.of(2021, 12, 21, 10, 20),
                15
        );

        Task taskFirst = new Task(
                "TaskFirst name",
                "Task des description",
                LocalDateTime.of(2021, 10, 21, 10, 20),
                15
        );

        Task taskThree = new Task(
                "TaskThree name",
                "Task des description",
                LocalDateTime.of(2021, 11, 21, 10, 20),
                15
        );

        int idOne = manager.addNewTask(taskFirst);
        int idTwo = manager.addNewTask(taskSecond);
        int idThree = manager.addNewTask(taskThree);

        manager.getTaskById(idThree);
        manager.getTaskById(idTwo);
        manager.getTaskById(idOne);

        List<Task> idTaskExpected = new LinkedList<>();
        idTaskExpected.add(taskThree);
        idTaskExpected.add(taskSecond);
        idTaskExpected.add(taskFirst);

        Assertions.assertEquals(idTaskExpected, manager.getHistory(), "История просмотра задач не верна.");

        manager.getTaskById(idThree);
        idTaskExpected.add(taskThree);

        Assertions.assertNotEquals(idTaskExpected, manager.getHistory(), "В истории закрались дубли!");
    }

    @Test
    public void testDeleteHistoryAnBack_returnHistory() {
        Assertions.assertEquals(0, manager.getHistory().size(), "Там кто-то есть 0_0");

        Task taskSecond = new Task(
                "TaskSecond name",
                "Task des description",
                LocalDateTime.of(2021, 12, 21, 10, 20),
                15
        );

        Task taskFirst = new Task(
                "TaskFirst name",
                "Task des description",
                LocalDateTime.of(2021, 10, 21, 10, 20),
                15
        );

        Task taskThree = new Task(
                "TaskThree name",
                "Task des description",
                LocalDateTime.of(2021, 11, 21, 10, 20),
                15
        );

        int idOne = manager.addNewTask(taskFirst);
        int idTwo = manager.addNewTask(taskSecond);
        int idThree = manager.addNewTask(taskThree);

        manager.getTaskById(idThree);
        manager.getTaskById(idTwo);
        manager.getTaskById(idOne);

        List<Task> idTaskExpected = new LinkedList<>();

        manager.deleteTaskById(idOne);
        idTaskExpected.add(taskThree);
        idTaskExpected.add(taskSecond);

        Assertions.assertEquals(idTaskExpected, manager.getHistory(), "С конца списка удаление не верное");
    }
}