package ru.yandex.kanban.manager.implemented;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.kanban.servers.implemented.KVServer;

import java.io.IOException;
class HttpTaskManagerTest extends TestManagersTest<HttpTaskManager> {

    KVServer server;

    @Override
    public HttpTaskManager createManager () throws IOException, InterruptedException {
        this.server = new KVServer();
        return new HttpTaskManager();
    }

    @BeforeEach
    public void start() {
        server.start();
    }

    @AfterEach
    public void stop () {
        server.stop();
    }
}