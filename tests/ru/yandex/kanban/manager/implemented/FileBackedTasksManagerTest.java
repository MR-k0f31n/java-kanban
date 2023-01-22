package ru.yandex.kanban.manager.implemented;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

public class FileBackedTasksManagerTest extends TestManagersTest<FileBackedTasksManager> {

    private static final File FILE = new File("resources", "history.csv");
    private static final File FILE_TEST_FROM_LOAD = new File("resources", "testFromLoad.csv");
    @Override
    FileBackedTasksManager createManager() {
        return FileBackedTasksManager.loadFromFile(FILE);
    }


    public static void clean() throws IOException {
        try {
            new FileWriter(FILE_TEST_FROM_LOAD, false).close();
        } catch (IOException exception) {
            throw new IOException(exception.getMessage());
        }
    }
    @Test
    public void testLoadFromFile_expectedCorrectLoad () throws IOException {
        FileBackedTasksManager fileBackedTasksManagerCreate = FileBackedTasksManager.loadFromFile(FILE_TEST_FROM_LOAD);
        Task taskOne = new Task("Name Task ONe",
                "Des Task one",
                LocalDateTime.of(2022,10,25,11,20,20),10);
        int idFTaskOne = fileBackedTasksManagerCreate.addNewTask(taskOne);

        Task taskSecond = new Task("Name Task Two",
                "Des Task Two",
                null,10);
        int idSecondTask = fileBackedTasksManagerCreate.addNewTask(taskSecond);

        EpicTask epicTaskFirst = new EpicTask("Name EpicTask One",
                "Des EpicTask One",
                LocalDateTime.of(2022,10,20,5,20,20),10);
        int idFirstEpicTask = fileBackedTasksManagerCreate.addNewTask(epicTaskFirst);

        int sizeMapTakBeforeLoad = fileBackedTasksManagerCreate.getAllListTask().size();
        int sizeMapEpicBeforeLoad = fileBackedTasksManagerCreate.getAllEpicTask().size();

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(FILE_TEST_FROM_LOAD);

        int sizeMapTakAfterLoad = fileBackedTasksManager.getAllListTask().size();
        int sizeMapEpicAfterLoad = fileBackedTasksManager.getAllEpicTask().size();

        Assertions.assertEquals(sizeMapTakBeforeLoad, sizeMapTakAfterLoad, "Загрузка Задач прошла неудачно!");
        Assertions.assertEquals(sizeMapEpicBeforeLoad, sizeMapEpicAfterLoad, "Загрузка Эпиков прошла неудачно!");

        clean();
    }


    @Test
    public void testLoadHistoryFromFile_expectedLoadHistoryCorrect () throws IOException {
        FileBackedTasksManager fileBackedTasksManagerCreate = FileBackedTasksManager.loadFromFile(FILE_TEST_FROM_LOAD);
        Task taskOne = new Task("Name Task ONe",
                "Des Task one",
                LocalDateTime.of(2022,10,25,11,20,20),10);
        int idFTaskOne = fileBackedTasksManagerCreate.addNewTask(taskOne);


        Task taskSecond = new Task("Name Task Two",
                "Des Task Two",
                null,10);
        int idSecondTask = fileBackedTasksManagerCreate.addNewTask(taskSecond);

        EpicTask epicTaskFirst = new EpicTask("Name EpicTask One",
                "Des EpicTask One",
                LocalDateTime.of(2022,10,20,5,20,20),10);
        int idFirstEpicTask = fileBackedTasksManagerCreate.addNewTask(epicTaskFirst);

        fileBackedTasksManagerCreate.getTaskById(idFTaskOne);
        fileBackedTasksManagerCreate.getTaskById(idSecondTask);
        fileBackedTasksManagerCreate.getEpicById(idFirstEpicTask);

        List<Integer> idBeforeLoadInHistory = new ArrayList<>();
        for (Task task : fileBackedTasksManagerCreate.getHistory()) {
            idBeforeLoadInHistory.add(task.getId());
        }

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(FILE_TEST_FROM_LOAD);

        List<Integer> idAfterLoadInHistory = new ArrayList<>();
        for (Task task : fileBackedTasksManager.getHistory()) {
            idAfterLoadInHistory.add(task.getId());
        }

        Assertions.assertEquals(idBeforeLoadInHistory, idAfterLoadInHistory, "История загрузилась неудачно!");

        clean();
    }
}
