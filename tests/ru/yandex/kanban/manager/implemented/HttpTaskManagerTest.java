package ru.yandex.kanban.manager.implemented;

import org.junit.jupiter.api.*;
import ru.yandex.kanban.servers.implemented.HttpTaskServer;
import ru.yandex.kanban.servers.implemented.KVServer;

import java.io.IOException;


class HttpTaskManagerTest extends TestManagersTest<HttpTaskManager> {

    private static KVServer kvServer;
    private HttpTaskServer httpTaskServer;

    @Override
    public HttpTaskManager createManager() throws IOException, InterruptedException {
        this.httpTaskServer = new HttpTaskServer();
        return new HttpTaskManager();
    }

    @BeforeAll
    public static void startServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @BeforeEach
    public void start() {
        httpTaskServer.start();
    }

    @AfterEach
    public void stop() {
        kvServer.stop();
        httpTaskServer.stop();
    }
}