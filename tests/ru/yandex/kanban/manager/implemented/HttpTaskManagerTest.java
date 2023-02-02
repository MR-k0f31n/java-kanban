package ru.yandex.kanban.manager.implemented;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import ru.yandex.kanban.servers.implemented.HttpTaskServer;
import ru.yandex.kanban.servers.implemented.KVServer;
import ru.yandex.kanban.servers.implemented.KVTaskClient;

import java.io.IOException;

class HttpTaskManagerTest extends TestManagersTest<HttpTaskManager> {

    private static KVServer server;
    private HttpTaskServer httpTaskServer;
    private KVTaskClient kvTaskClient;
    Gson gson = new Gson();

    @Override
    public HttpTaskManager createManager () throws IOException, InterruptedException {
        this.httpTaskServer = new HttpTaskServer();
        this.kvTaskClient = new KVTaskClient();
        return new HttpTaskManager();
    }

    @BeforeAll
    public static void startServer() throws IOException {
        server = new KVServer();
        server.start();
    }

    @BeforeEach
    public void start() {
        httpTaskServer.start();
    }

    @AfterEach
    public void stop () throws IOException {
        server.stop();
        httpTaskServer.stop();
    }

    /*@Test
    public void LoadFromServer_expectedCorrectLoad () {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest.BodyPublisher body;
        HttpRequest request;

        Task taskOne = new Task("Name Task ONe",
                "Des Task one",
                LocalDateTime.of(2022,10,25,11,20,20),10);
        String jsonTaskOne = gson.toJson(taskOne);
        body = HttpRequest.BodyPublishers.ofString(jsonTaskOne);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();

        Task taskSecond = new Task("Name Task Two",
                "Des Task Two",
                null,10);
        String jsonTaskTwo = gson.toJson(taskSecond);
        body = HttpRequest.BodyPublishers.ofString(jsonTaskTwo);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();

        EpicTask epicTaskFirst = new EpicTask("Name EpicTask One",
                "Des EpicTask One",
                LocalDateTime.of(2022,10,20,5,20,20),10);
        String jsonEpicOne = gson.toJson(epicTaskFirst);
        url = URI.create("http://localhost:8080/tasks/epic/");
        body = HttpRequest.BodyPublishers.ofString(jsonEpicOne);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();


    }


    @Test
    public void loadFromFile_HistoryFromFile_expectedLoadHistoryCorrect () throws IOException {
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

        assertEquals(idBeforeLoadInHistory, idAfterLoadInHistory, "История загрузилась неудачно!");
    }*/
}