package ru.yandex.kanban.servers.implemented.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.kanban.data.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

public static class PrioritizedTasksHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URI path = httpExchange.getRequestURI();
        String stringPath = path.toString();
        String method = httpExchange.getRequestMethod();
        try {
            switch (method) {
                case GET:
                    if (stringPath.equals("/tasks/")) {
                        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                        String response = gson.toJson(prioritizedTasks);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    } else
                        throw new RuntimeException("Неверный путь");
                    break;
                default:
                    throw new RuntimeException("Вызвали не GET у task");
            }
        } catch (Throwable e) {
            httpExchange.sendResponseHeaders(400, 0);
            httpExchange.close();
        }
    }
}