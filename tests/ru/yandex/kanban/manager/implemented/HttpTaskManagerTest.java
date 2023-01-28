package ru.yandex.kanban.manager.implemented;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.kanban.servers.implemented.KVServer;

import java.io.File;
import java.io.IOException;
import java.net.URI;

class HttpTaskManagerTest extends TestManagersTest<HttpTaskManager> {
    KVServer kvServer;
    private static final File FILE = new File("resources", "history.csv");

    @Override
    public HttpTaskManager createManager() throws IOException, InterruptedException {
            return new HttpTaskManager(FILE);
    }

    @BeforeEach
    public void startServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void stopServer() {
        kvServer.stop();
    }
}