package ru.yandex.kanban.manager.implemented;

import org.junit.jupiter.api.*;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

abstract class TestManagersTest<T extends TaskManager> {

    public abstract T createManager();

    T manager;
    File file = new File("resources", "history.csv");
    Task taskFirst;
    int idFirstTask;
    Task taskSecond;
    int idSecondTask;

    EpicTask epicTaskFirst;
    int idFirstEpicTask;

    SubTask subTaskFirst;
    int idFirstSubTask;
    SubTask subTaskSecond;
    int idSecondSubTask;
    SubTask subTaskThree;
    int idThreeSubTask;

    @BeforeEach
    public void create() {
        manager = createManager();
    }


    protected void createTasks() {
        taskFirst = new Task("Name Task ONe",
                "Des Task one",
                LocalDateTime.of(2022, 10, 25, 11, 20, 20), 10);
        idFirstTask = manager.addNewTask(taskFirst);

        taskSecond = new Task("Name Task Two",
                "Des Task Two",
                null, 10);
        idSecondTask = manager.addNewTask(taskSecond);

        epicTaskFirst = new EpicTask("Name EpicTask One",
                "Des EpicTask One",
                LocalDateTime.of(2022, 10, 20, 5, 20, 20), 10);
        idFirstEpicTask = manager.addNewTask(epicTaskFirst);
    }

    protected void createSubTask() {
        subTaskFirst = new SubTask("Name Sub One",
                "Des Sub One",
                LocalDateTime.of(2022, 10, 26, 4, 20, 20), 10,
                idFirstEpicTask);
        idFirstSubTask = manager.addNewTask(subTaskFirst);


        subTaskSecond = new SubTask("Name Sub Two",
                "Des EpicTask Two",
                LocalDateTime.of(2022, 10, 29, 9, 20, 20), 10,
                idFirstEpicTask);
        idSecondSubTask = manager.addNewTask(subTaskSecond);

        subTaskThree = new SubTask("Name Sub Three",
                "Des Sub Three",
                LocalDateTime.of(2022, 10, 25, 10, 20, 20), 10,
                idFirstEpicTask);
        idThreeSubTask = manager.addNewTask(subTaskThree);
    }

    protected void updateSubTaskDone() {
        SubTask subTaskFirstDone = new SubTask(idFirstSubTask, "Name Sub One",
                "Des Sub One",
                LocalDateTime.of(2022, 10, 26, 4, 20, 20), Status.DONE, 10,
                idFirstEpicTask);
        manager.updateSubTask(subTaskFirstDone);


        SubTask subTaskSecondDone = new SubTask(idSecondSubTask, "Name Sub Two",
                "Des EpicTask Two",
                LocalDateTime.of(2022, 10, 29, 9, 20, 20), Status.DONE, 10,
                idFirstEpicTask);
        manager.updateSubTask(subTaskSecondDone);

        SubTask subTaskThreeDone = new SubTask(idThreeSubTask, "Name Sub Three",
                "Des Sub Three",
                LocalDateTime.of(2022, 10, 25, 10, 20, 20), Status.DONE, 10,
                idFirstEpicTask);
        manager.updateSubTask(subTaskThreeDone);
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
    public void testGetPriorityList_ReturnCorrectListPriority() {
        createTasks();
        createSubTask();
        List<Integer> listIdTaskOnPriorityExpected = new ArrayList<>();
        List<Integer> listIdActual = new ArrayList<>();

        listIdTaskOnPriorityExpected.add(idThreeSubTask);
        listIdTaskOnPriorityExpected.add(idFirstEpicTask);
        listIdTaskOnPriorityExpected.add(idFirstTask);
        listIdTaskOnPriorityExpected.add(idFirstSubTask);
        listIdTaskOnPriorityExpected.add(idSecondSubTask);
        listIdTaskOnPriorityExpected.add(idSecondTask);

        for (Task task : manager.getPrioritizedTasks()) {
            listIdActual.add(task.getId());
        }

        Assertions.assertEquals(listIdTaskOnPriorityExpected, listIdActual, "Приоритеты раставлены не верно!");
    }

    /*@Test
    public void calculateStatusEpic_CorrectCalculateStatusFromEpic() {
        createTasks();
        Assertions.assertEquals(Status.NEW, manager.getEpicById(idFirstEpicTask).getStatus(),
                "Список подзадач пусть, статус не новый");

        createSubTask();
        Assertions.assertEquals(Status.NEW, manager.getEpicById(idFirstEpicTask).getStatus(),
                "Список подзадач заполнился, статус не новый");

        SubTask subTaskFirstDone = new SubTask(idFirstSubTask, "Name Sub One",
                "Des Sub One",
                LocalDateTime.of(2022, 10, 26, 4, 20, 20), Status.DONE, 10,
                idFirstEpicTask);
        manager.updateSubTask(subTaskFirstDone);


        SubTask subTaskSecondDone = new SubTask(idSecondSubTask, "Name Sub Two",
                "Des EpicTask Two",
                LocalDateTime.of(2022, 10, 29, 9, 20, 20), Status.DONE, 10,
                idFirstEpicTask);
        manager.updateSubTask(subTaskSecondDone);

        SubTask subTaskThreeDone = new SubTask(idThreeSubTask, "Name Sub Three",
                "Des Sub Three",
                LocalDateTime.of(2022, 10, 25, 10, 20, 20), Status.DONE, 10,
                idFirstEpicTask);
        manager.updateSubTask(subTaskThreeDone);
        System.out.println(manager.getAllSubTask());
        System.out.println(manager.getAllEpicTask());
        Assertions.assertEquals(Status.DONE, manager.getEpicById(idFirstEpicTask).getStatus(),
                "Подзадачи DONE, Статус не обновился");
    }*/
}