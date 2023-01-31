package ru.yandex.kanban.manager.implemented;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.kanban.manager.Managers;
import ru.yandex.kanban.servers.implemented.HttpTaskServer;
import ru.yandex.kanban.servers.implemented.KVServer;

import java.io.IOException;
import java.net.http.HttpClient;

class HttpTaskManagerTest extends TestManagersTest<HttpTaskManager> {
    private static final KVServer kvServer;
    private static HttpTaskServer httpTS;

    private static Gson gson;
    private static HttpClient client;

    static {
        try {
            kvServer = new KVServer();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HttpTaskManager createManager() throws IOException, InterruptedException {
            return new HttpTaskManager();
    }

    @BeforeEach
    public void startServer() throws IOException, InterruptedException {
        kvServer.start();
        httpTS = new HttpTaskServer();
        gson = new Gson();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void stopServer() {
        kvServer.stop();
    }
}