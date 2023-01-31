package ru.yandex.kanban.servers.implemented.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.kanban.data.EpicTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public static class EpicHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URI path = httpExchange.getRequestURI();
        String stringPath = path.toString();
        String method = httpExchange.getRequestMethod();
        try {
            switch (method) {
                case GET:
                    if (stringPath.equals("/tasks/epic/")) {
                        List<EpicTask> epicsTaskList = taskManager.getAllEpicTask();
                        String response = gson.toJson(epicsTaskList);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    } else if (stringPath.startsWith("/tasks/epic/?id=")) {
                        String[] id = stringPath.split("=");
                        EpicTask epic = taskManager.getEpicById(Integer.parseInt(id[1]));
                        String response = gson.toJson(epic);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    } else
                        System.out.println("Неверный путь");
                    break;
                case POST:
                    InputStream inputStream = httpExchange.getRequestBody();
                    String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(jsonString);
                    if (!jsonElement.isJsonObject()) {
                        System.out.println("Это не jsonObject");
                    }
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    EpicTask epic = gson.fromJson(jsonObject, EpicTask.class);
                    List<EpicTask> epicTaskList = taskManager.getAllEpicTask();
                    if (httpExchange.getRequestURI().toString().equals("/tasks/task/")) {
                        if (epicTaskList.contains(epic)) {
                            taskManager.updateTask(epic);
                        } else {
                            taskManager.addNewTask(epic);
                        }
                    }
                    httpExchange.sendResponseHeaders(201, 0);
                    httpExchange.close();
                    break;
                case DELETE:
                    if ("/tasks/epic/".equals(stringPath)) {
                        taskManager.clearAllEpicTask();
                    } else {
                        if (stringPath.startsWith("/tasks/epic/?id=")) {
                            String[] mass = stringPath.split("=");
                            taskManager.deleteEpicTaskById(Integer.parseInt(mass[1]));
                        }
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    httpExchange.close();
                    break;
                default:
                    System.out.println("Вызвали не GET у epic");
            }
        } catch (Throwable e) {
            httpExchange.sendResponseHeaders(400, 0);
            httpExchange.close();
        }
    }
}