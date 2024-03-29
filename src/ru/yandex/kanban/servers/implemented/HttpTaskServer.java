package ru.yandex.kanban.servers.implemented;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.kanban.manager.Managers;
import ru.yandex.kanban.manager.interfaces.TaskManager;
import ru.yandex.kanban.servers.implemented.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8080;

    public HttpTaskServer() throws IOException, InterruptedException {
        this.httpServer = HttpServer.create();

        TaskManager taskManager = Managers.getDefault();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task/", new TaskHandler(taskManager));
        httpServer.createContext("/tasks/epic/", new EpicHandler(taskManager));
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler(taskManager));
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/tasks/history/", new HistoryHandler(taskManager));
    }

    public void start() {
        System.out.println("Запускаем HTTP сервер на порту " + PORT);
        System.out.println("---------------------------------");
        httpServer.start();
    }

    public void stop() {
        System.out.println("\nHTTP сервер остановлен");
        System.out.println("---------------------------------");
        httpServer.stop(0);
    }
}