package ru.yandex.kanban.servers.implemented.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.manager.interfaces.TaskManager;
import ru.yandex.kanban.servers.implemented.util.AdapterFromLocalData;
import ru.yandex.kanban.servers.implemented.util.DurationAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public class EpicHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new AdapterFromLocalData())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
            /*new GsonBuilder().setPrettyPrinting()
            .serializeNulls().create()*/;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String stringPath = httpExchange.getRequestURI().getPath();
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case GET:
                System.out.println("Началась обработка метода GET запрос от клиента.");
                if (stringPath.equals("/tasks/epic/")) {
                    System.out.println("Началась обработка /tasks/epic запроса от клиента.");
                    String response = gson.toJson(taskManager.getAllEpicTask());
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream outputStream = httpExchange.getResponseBody()) {
                        outputStream.write(response.getBytes());
                    }
                } else if (stringPath.startsWith("/tasks/epic/?id=")) {
                    System.out.println("Началась обработка /tasks/epic by ID запроса от клиента.");
                    String[] id = stringPath.split("=");
                    EpicTask epic = taskManager.getEpicById(Integer.parseInt(id[1]));
                    String response = gson.toJson(epic);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream outputStream = httpExchange.getResponseBody()) {
                        outputStream.write(response.getBytes());
                    }
                } else {
                    System.out.println("Неверный путь");
                    String response = "Неверный путь";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream outputStream = httpExchange.getResponseBody()) {
                        outputStream.write(response.getBytes());
                    }
                }
                httpExchange.close();
                break;
            case POST:
                InputStream inputStream = httpExchange.getRequestBody();
                String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                EpicTask epic = gson.fromJson(jsonString, EpicTask.class);
                List<EpicTask> epicTaskList = taskManager.getAllEpicTask();
                if (epic != null) {
                    String response;
                    if (epicTaskList.contains(epic)) {
                        taskManager.updateTask(epic);
                        response = "Такая задача существует и была обновленна";
                        httpExchange.sendResponseHeaders(208, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    } else {
                        taskManager.addNewTask(epic);
                        response = "Задача была успешно добавлена";
                        httpExchange.sendResponseHeaders(201, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    }
                }
                httpExchange.close();
                break;
            case DELETE:
                if ("/tasks/epic/".equals(stringPath)) {
                    taskManager.clearAllEpicTask();
                    String response = "Все задачи и подзадачи были удалены";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream outputStream = httpExchange.getResponseBody()) {
                        outputStream.write(response.getBytes());
                    }
                } else {
                    if (stringPath.startsWith("/tasks/epic/?id=")) {
                        String[] mass = stringPath.split("=");
                        taskManager.deleteEpicTaskById(Integer.parseInt(mass[1]));
                        String response = "Задача ID " + mass[1] + " и связаные подзадачи были удалены!";
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    }
                }
                httpExchange.sendResponseHeaders(200, 0);
                httpExchange.close();
                break;
            default:
                System.out.println("Метод не найден");
                String response = "Во время выполнения запроса возникла ошибка.";
                httpExchange.sendResponseHeaders(404, 0);
                try (OutputStream outputStream = httpExchange.getResponseBody()) {
                    outputStream.write(response.getBytes());
                }
        }
    }
}