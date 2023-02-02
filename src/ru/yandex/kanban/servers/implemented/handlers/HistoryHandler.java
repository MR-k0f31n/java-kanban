package ru.yandex.kanban.servers.implemented.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.kanban.manager.interfaces.TaskManager;
import ru.yandex.kanban.servers.implemented.util.AdapterFromLocalData;
import ru.yandex.kanban.servers.implemented.util.AdapterFromDuration;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;

public class HistoryHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new AdapterFromLocalData())
            .registerTypeAdapter(Duration.class, new AdapterFromDuration())
            .create();
    private static final String GET = "GET";

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String stringPath = httpExchange.getRequestURI().getPath();
        String method = httpExchange.getRequestMethod();
        String response;

        switch (method) {
            case GET:
                System.out.println("Началась обработка метода GET запрос от клиента.");
                if (stringPath.equals("/tasks/history")) {
                    System.out.println("Началась обработка /tasks/history запроса от клиента.");
                    if (taskManager.getHistory().isEmpty()) {
                        response = "История получение задач пуста!";
                    } else {
                        response = gson.toJson(taskManager.getHistory());
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream outputStream = httpExchange.getResponseBody()) {
                        outputStream.write(response.getBytes());
                    }
                } else {
                    System.out.println("Неверный путь");
                    response = "Неверный путь";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream outputStream = httpExchange.getResponseBody()) {
                        outputStream.write(response.getBytes());
                    }
                }
                break;
            default:
                System.out.println("Метод не найден");
                response = "Во время выполнения запроса возникла ошибка.";
                httpExchange.sendResponseHeaders(404, 0);
                try (OutputStream outputStream = httpExchange.getResponseBody()) {
                    outputStream.write(response.getBytes());
                }
        }
    }
}