package ru.yandex.kanban.servers.implemented;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.kanban.manager.Managers;
import ru.yandex.kanban.manager.interfaces.TaskManager;
import ru.yandex.kanban.servers.implemented.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;



public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8078;

    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException, InterruptedException {
        this.httpServer = HttpServer.create();

        this.taskManager = Managers.getDefault();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task/", new TaskHandler(taskManager));
        httpServer.createContext("/tasks/epic/", new EpicHandler(taskManager));
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler(taskManager));
        httpServer.createContext("/tasks", new PrioritizedTasksHandler(taskManager));
        httpServer.createContext("/tasks/history/", new HistoryHandler(taskManager));

        System.out.println("Запускаем HTTP сервер на порту " + PORT);
        System.out.println("---------------------------------");
        httpServer.start();
    }

    public void stop() {
        System.out.println("HTTP сервер остановлен");
        System.out.println("---------------------------------");
        httpServer.stop(0);
    }
}