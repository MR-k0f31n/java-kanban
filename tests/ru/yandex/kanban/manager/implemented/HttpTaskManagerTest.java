package ru.yandex.kanban.manager.implemented;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.kanban.servers.implemented.HttpTaskServer;
import ru.yandex.kanban.servers.implemented.KVServer;

import java.io.IOException;
class HttpTaskManagerTest extends TestManagersTest<HttpTaskManager> {

    private KVServer server;
    private HttpTaskServer httpTaskServer;

    @Override
    public HttpTaskManager createManager () throws IOException, InterruptedException {

        this.httpTaskServer = new HttpTaskServer();
        return new HttpTaskManager();
    }

    @BeforeAll
    public static void startServer() throws IOException {
        new KVServer().start();
    }

    @BeforeEach
    public void start() {
        httpTaskServer.start();
    }

    @AfterEach
    public void stop () {
        server.stop();
        httpTaskServer.stop();
    }
}