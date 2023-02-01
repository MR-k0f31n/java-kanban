package ru.yandex.kanban.manager.implemented;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.servers.implemented.HttpTaskServer;
import ru.yandex.kanban.servers.implemented.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {
    private static final KVServer kvServer;


    static {
        try {
            kvServer = new KVServer();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpTaskServer httpTS;

    private static Gson gson;
    private static HttpClient client;


    @BeforeAll
    static void setUp() throws IOException, InterruptedException {
        kvServer.start();
        httpTS = new HttpTaskServer();
        gson = new Gson();
        client = HttpClient.newHttpClient();
    }


    @AfterAll
    static void tearDown() {
        httpTS.stop();
        kvServer.stop();
    }


    @Test
    void addTasksToTaskServerOrUpdate() throws IOException, InterruptedException {


        Object Status;
        Task task1 = new Task("Name Task ONe",
                "Des Task one",
                LocalDateTime.of(2022, 10, 25, 11, 20, 20), 10);
        task1.setId(1);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        task1.setId(1);
        json = gson.toJson(task1);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(218, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task task = gson.fromJson(response.body(), Task.class);
        assertEquals("Task1", task.getName());

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode());

    }

    @Test
    void addOrUpdateSubtaskAndAddEpics() throws IOException, InterruptedException {
        EpicTask epic1 = new EpicTask("Name EpicTask One",
                "Des EpicTask One",
                LocalDateTime.of(2022, 10, 20, 5, 20, 20), 10);
        URI url = URI.create("http://localhost:8078/tasks/epic/");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        int epicId = 2;

        SubTask sub1_1 = new SubTask("Name Sub One",
                "Des Sub One",
                LocalDateTime.of(2022, 10, 10, 4, 20, 20), 10, epicId);
        url = URI.create("http://localhost:8078/tasks/subtask/");
        json = gson.toJson(sub1_1);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());


        url = URI.create("http://localhost:8078/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        SubTask task = gson.fromJson(response.body(), SubTask.class);
        assertEquals("SubTask1 - 1", task.getName());



        json = gson.toJson(task);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(218, response.statusCode());

        url = URI.create("http://localhost:8078/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        task = gson.fromJson(response.body(), SubTask.class);
        assertEquals("11", task.getName());

        url = URI.create("http://localhost:8078/tasks/subtask/?id=3");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response3.statusCode());

        url = URI.create("http://localhost:8078/tasks/epic/?id=2");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode());

        url = URI.create("http://localhost:8078/tasks/epic/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());
        JsonArray arrayTasks = JsonParser.parseString(response1.body()).getAsJsonArray();
        assertEquals(0, arrayTasks.size());


        url = URI.create("http://localhost:8078/tasks/subtask/");
        HttpRequest request4 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response4.statusCode());
        arrayTasks = JsonParser.parseString(response4.body()).getAsJsonArray();
        assertEquals(0, arrayTasks.size());

    }

    @Test
    void deleteAllTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8078/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());


        url = URI.create("http://localhost:8078/tasks/task/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(0, arrayTasks.size());

    }

    @Test
    void deleteAllSubsAndEpics() throws IOException, InterruptedException {
        EpicTask epic1 = new EpicTask("Name EpicTask One",
                "Des EpicTask One",
                LocalDateTime.of(2022, 10, 20, 5, 20, 20), 10);
        URI url = URI.create("http://localhost:8078/tasks/epic/");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        int epicId = 2;

        SubTask sub1_1 = new SubTask("Name Sub One",
                "Des Sub One",
                LocalDateTime.of(2022, 10, 10, 4, 20, 20), 10, epicId);
        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(sub1_1);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        url = URI.create("http://localhost:8078/tasks/epic/");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8078/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());


        url = URI.create("http://localhost:8078/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(0, arrayTasks.size());

        url = URI.create("http://localhost:8078/tasks/epic/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(0, arrayTasks.size());
    }
}
