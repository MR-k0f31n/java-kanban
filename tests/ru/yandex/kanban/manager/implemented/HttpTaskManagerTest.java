package ru.yandex.kanban.manager.implemented;

import org.junit.jupiter.api.*;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.servers.implemented.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;


class HttpTaskManagerTest extends TestManagersTest<HttpTaskManager> {

    private static KVServer kvServer;

    @Override
    public HttpTaskManager createManager() throws IOException, InterruptedException {
        return new HttpTaskManager();
    }

    @BeforeAll
    public static void startServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterAll
    public static void stop() {
        kvServer.stop();
    }

    @Test
    public void loadFromKVServer_CorrectLoad() throws IOException, InterruptedException {
        HttpTaskManager taskManager = new HttpTaskManager();

        Task taskOne = new Task("Name Task ONe",
                "Des Task one",
                LocalDateTime.of(2022, 10, 25, 11, 20, 20), 10);
        taskManager.addNewTask(taskOne);

        Task taskSecond = new Task("Name Task Two",
                "Des Task Two",
                null, 10);
        taskManager.addNewTask(taskSecond);

        EpicTask epicTaskFirst = new EpicTask("Name EpicTask One",
                "Des EpicTask One",
                LocalDateTime.of(2022, 10, 20, 5, 20, 20), 10);
        taskManager.addNewTask(epicTaskFirst);

        int sizeMapTakBeforeLoad = taskManager.getAllListTask().size();
        int sizeMapEpicBeforeLoad = taskManager.getAllEpicTask().size();

        System.out.println("Epics " + sizeMapEpicBeforeLoad + " Tasks " + sizeMapTakBeforeLoad);

        HttpTaskManager taskManagerLoad = new HttpTaskManager();

        int sizeMapTakAfterLoad = taskManagerLoad.getAllListTask().size();
        int sizeMapEpicAfterLoad = taskManagerLoad.getAllEpicTask().size();

        Assertions.assertEquals(sizeMapTakBeforeLoad, sizeMapTakAfterLoad, "Загрузка Задач прошла неудачно!");
        Assertions.assertEquals(sizeMapEpicBeforeLoad, sizeMapEpicAfterLoad, "Загрузка Эпиков прошла неудачно!");
    }
}