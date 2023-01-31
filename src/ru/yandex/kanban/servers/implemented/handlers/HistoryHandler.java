package ru.yandex.kanban.servers.implemented.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.kanban.data.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

public static class HistoryHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URI path = httpExchange.getRequestURI();
        String stringPath = path.toString();
        String method = httpExchange.getRequestMethod();
        try {
            switch (method) {
                case GET:
                    if (stringPath.equals("/tasks/history")) {
                        List<Task> history = taskManager.getHistory();
                        String response = gson.toJson(history);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    } else
                        System.out.println("Неверный путь");
                    break;
                default:
                    System.out.println("Вызвали не GET у history");
            }
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(400, 0);
            httpExchange.close();
        }
    }
}