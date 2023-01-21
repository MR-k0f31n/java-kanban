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

    protected T manager;
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

    @BeforeEach
    public void createTasks () {
        taskFirst = new Task("Name Task ONe",
                "Des Task one",
                LocalDateTime.of(2022,10,25,11,20,20),10);
        idFirstTask = manager.addNewTask(taskFirst);

        taskSecond = new Task("Name Task Two",
                "Des Task Two",
                null,10);
        idSecondTask = manager.addNewTask(taskSecond);

        epicTaskFirst = new EpicTask("Name EpicTask Two",
                "Des EpicTask Two",
                LocalDateTime.of(2022,10,20,5,20,20),10);
        idFirstEpicTask = manager.addNewTask(epicTaskFirst);

        subTaskFirst = new SubTask("Name Sub One",
                "Des Sub One",
                LocalDateTime.of(2022,10,26,4,20,20),10,
                idFirstEpicTask);
        idFirstSubTask = manager.addNewTask(subTaskFirst);


        subTaskSecond = new SubTask("Name Sub Two",
                "Des EpicTask Two",
                LocalDateTime.of(2022,10,29,9,20,20),10,
                idFirstEpicTask);
        idSecondSubTask = manager.addNewTask(subTaskSecond);

        subTaskThree = new SubTask("Name Sub Three",
                "Des Sub Three",
                LocalDateTime.of(2022,10,25,10,20,20),10,
                idFirstEpicTask);
        idThreeSubTask = manager.addNewTask(subTaskThree);

        manager.getTaskById(idSecondTask);
        manager.getTaskById(idFirstEpicTask);
        manager.getSubById(idFirstSubTask);
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
    public void testGetPriorityList_ReturnCorrectListPriority () {
        List<Integer> listIdTaskOnPriorityExpected = new LinkedList<>();
        List<Integer> listIdActual = new LinkedList<>();

        listIdTaskOnPriorityExpected.add(idThreeSubTask);
        listIdTaskOnPriorityExpected.add(idFirstEpicTask);
        listIdTaskOnPriorityExpected.add(idFirstTask);
        listIdTaskOnPriorityExpected.add(idFirstSubTask);
        listIdTaskOnPriorityExpected.add(idSecondSubTask);
        listIdTaskOnPriorityExpected.add(idSecondTask);


    }
}