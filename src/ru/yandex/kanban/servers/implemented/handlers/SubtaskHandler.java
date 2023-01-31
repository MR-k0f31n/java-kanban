package ru.yandex.kanban.servers.implemented.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.kanban.data.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public static class SubtaskHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URI path = httpExchange.getRequestURI();
        String stringPath = path.toString();
        String method = httpExchange.getRequestMethod();
        try {
            switch (method) {
                case GET:
                    if (stringPath.equals("/tasks/subtask/")) {
                        List<SubTask> subTaskList = taskManager.getAllSubTask();
                        String response = gson.toJson(subTaskList);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    } else if (stringPath.startsWith("/tasks/subtask/?id=")) {
                        String[] id = stringPath.split("=");
                        SubTask subTask = taskManager.getSubById(Integer.parseInt(id[1]));
                        String response = gson.toJson(subTask);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    } else if (stringPath.startsWith("/tasks/subtask/epic?id=")) {
                        String[] id = stringPath.split("=");
                        List<SubTask> listSubTasks = taskManager.getAllSubByEpicTask(Integer.parseInt(id[1]));
                        String response = gson.toJson(listSubTasks);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    } else
                        throw new RuntimeException("Неверный путь");
                    break;
                case POST:
                    InputStream inputStream = httpExchange.getRequestBody();
                    String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(jsonString);
                    if (!jsonElement.isJsonObject()) {
                        throw new RuntimeException("Это не jsonObject");
                    }
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    SubTask subTask = gson.fromJson(jsonObject, SubTask.class);
                    List<SubTask> subTaskList = taskManager.getAllSubTask();
                    if (httpExchange.getRequestURI().toString().equals("/tasks/subtask/")) {
                        if (subTaskList.contains(subTask)) {
                            taskManager.updateSubTask(subTask);
                        } else {
                            taskManager.addNewTask(subTask);
                        }
                    }
                    httpExchange.sendResponseHeaders(201, 0);
                    httpExchange.close();
                    break;
                case DELETE:
                    if ("/tasks/subtask/".equals(stringPath)) {
                        taskManager.clearAllSubTask();
                    } else {
                        if (stringPath.startsWith("/tasks/subtask/?id=")) {
                            String[] mass = stringPath.split("=");
                            taskManager.deleteSubTaskById(Integer.parseInt(mass[1]));
                        }
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    httpExchange.close();
                    break;
                default:
                    throw new RuntimeException("Вызвали не GET у epic");
            }
        } catch (Throwable e) {
            httpExchange.sendResponseHeaders(400, 0);
            httpExchange.close();
        }
    }
}