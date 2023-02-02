package ru.yandex.kanban.servers.implemented.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.manager.interfaces.TaskManager;
import ru.yandex.kanban.servers.implemented.util.AdapterFromLocalData;
import ru.yandex.kanban.servers.implemented.util.AdapterFromDuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class SubtaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new AdapterFromLocalData())
            .registerTypeAdapter(Duration.class, new AdapterFromDuration())
            .create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String stringPath = httpExchange.getRequestURI().getPath();
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case GET:
                System.out.println("Началась обработка метода GET запрос от клиента.");

                if (stringPath.equals("/tasks/subtask/")) {
                    System.out.println("Началась обработка /tasks/subtask запроса от клиента.");
                    String response;
                    if (taskManager.getAllSubTask().isEmpty()) {
                        response = "Список подзадач пуст";
                    } else {
                        response = gson.toJson(taskManager.getAllSubTask());
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream outputStream = httpExchange.getResponseBody()) {
                        outputStream.write(response.getBytes());
                    }
                } else if (stringPath.startsWith("/tasks/subtask/?id=")) {
                    System.out.println("Началась обработка /tasks/subtask by ID запроса от клиента.");
                    String[] id = stringPath.split("=");

                    String response = gson.toJson(taskManager.getSubById(Integer.parseInt(id[1])));
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream outputStream = httpExchange.getResponseBody()) {
                        outputStream.write(response.getBytes());
                    }
                } else if (stringPath.startsWith("/tasks/subtask/epic?id=")) {
                    System.out.println("Началась обработка /tasks/subtask by epicID запроса от клиента.");
                    String[] id = stringPath.split("=");

                    String response = gson.toJson(taskManager.getAllSubByEpicTask(Integer.parseInt(id[1])));
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
                System.out.println("Началась обработка метода POST запрос от клиента.");
                InputStream inputStream = httpExchange.getRequestBody();
                String jsonString = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

                SubTask subTask = gson.fromJson(jsonString, SubTask.class);
                List<SubTask> subTaskList = taskManager.getAllSubTask();
                if (subTask != null) {
                    if (subTaskList.contains(subTask)) {
                        taskManager.updateSubTask(subTask);
                        String response = "Такая задача существует и была обновленна";
                        httpExchange.sendResponseHeaders(208, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    } else {
                        taskManager.addNewTask(subTask);
                        String response = "Задача была успешно добавлена";
                        httpExchange.sendResponseHeaders(201, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    }
                }
                httpExchange.close();
                break;
            case DELETE:
                System.out.println("Началась обработка метода DELETE запрос от клиента.");
                if ("/tasks/subtask/".equals(stringPath)) {
                    taskManager.clearAllSubTask();
                    String response = "Все подзадачи были удалены";
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream outputStream = httpExchange.getResponseBody()) {
                        outputStream.write(response.getBytes());
                    }
                } else {
                    if (stringPath.startsWith("/tasks/subtask/?id=")) {
                        String[] mass = stringPath.split("=");
                        taskManager.deleteSubTaskById(Integer.parseInt(mass[1]));
                        String response = "Подзадача была удалена ID " + mass[1];
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    }
                }
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