package ru.yandex.kanban.manager.implemented;

import org.junit.jupiter.api.*;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.exceptions.ManagerSaveException;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

abstract class TestManagers <T extends TaskManager> {

    public abstract T createManager();
    private T manager;


    @BeforeEach
    public void create () {
        manager = createManager();
    }

    private void createTask () {

    }

    @Test
    public void testPriorityTask_returnSortTask () {

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

        final int idTask = manager.addNewTask(taskOne);


        Task taskSecond = new Task(
                "TaskSecond name",
                "Task des description",
                LocalDateTime.of(2021, 12, 19, 10, 40),
                15
        );

        final int idTaskSecond = manager.addNewTask(taskSecond);

        SubTask subOne = new SubTask(
                "SubOne name",
                "SubOne des description",
                LocalDateTime.of(2022, 12, 21, 20, 25),
                15,
                idEpicOne
        );

        final int idSubOne = manager.addNewTask(subOne);

        SubTask subTwo = new SubTask(
                "SubTwo name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 20, 9, 25),
                15,
                idEpicOne
        );

        final int idSubTwo = manager.addNewTask(subTwo);

        SubTask subFour = new SubTask(
                "SubFour name",
                "SubThree des description",
                LocalDateTime.of(2022, 11, 19,10,30),
                15,
                idEpicOne
        );

        final int idSubFour = manager.addNewTask(subFour);

        SubTask subThree = new SubTask(
                "SubThree name",
                "SubThree des description",
                null,
                15,
                idEpicOne
        );

        final int idSubThree = manager.addNewTask(subThree);


        taskPriority.add(taskOne);
        taskPriority.add(epicOne);
        taskPriority.add(subOne);
        taskPriority.add(subTwo);
        taskPriority.add(subThree);
        Assertions.assertEquals(manager.getPrioritizedTasks(), taskPriority, "Приоритеты неверны");
    }

    @Test
    public void testTrowsTimeOverlay_returnTrows () {
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
    public void testGetAllTaskList () {
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

        int idOne = manager.addNewTask(taskFirst);
        int idTwo = manager.addNewTask(taskSecond);
        listTest.add(taskFirst);
        listTest.add(taskSecond);

        Assertions.assertEquals(listTest, manager.getAllListTask());
    }

    @Test
    public void testGetAllSubTask () {
        List<Task> taskList = new ArrayList<>();
        final EpicTask epicOne = new EpicTask(
                "EpicOne name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 20, 12, 25),
                15
        );

        final int idEpicOne = manager.addNewTask(epicOne);
        taskList.add(epicOne);

        final  SubTask subTaskOne = new SubTask(
                "OneSub name",
                "SubOne des",
                LocalDateTime.of(2022,12,18,13,25),
                15,
                idEpicOne
        );

        final int idSub = manager.addNewTask(subTaskOne);
        taskList.add(subTaskOne);

        Assertions.assertNotEquals(taskList, manager.getAllSubTask());
    }
}