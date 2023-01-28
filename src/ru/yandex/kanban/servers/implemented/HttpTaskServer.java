package ru.yandex.kanban.servers.implemented;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.manager.Managers;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;



public class HttpTaskServer {
    private final URI url = URI.create("http://localhost:8078");
    private HttpServer httpServer;
    private static final int PORT = 8078;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static TaskManager taskManager;
    private static Gson gson = new GsonBuilder().setPrettyPrinting()
            .serializeNulls().create();

    public void startServer() throws IOException, InterruptedException {
        taskManager = Managers.getDefault();
        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task/", new TaskHandler());
        httpServer.createContext("/tasks/epic/", new EpicHandler());
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler());
        httpServer.createContext("/tasks", new PrioritizedTasksHandler());
        httpServer.createContext("/tasks/history/", new HistoryHandler());
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    static class HistoryHandler implements HttpHandler {
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

    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI path = httpExchange.getRequestURI();
            String stringPath = path.toString();
            String method = httpExchange.getRequestMethod();
            try {
                switch (method) {
                    case GET:
                        if (stringPath.equals("/tasks/task/")) {
                            List<Task> tasksList = taskManager.getAllListTask();
                            String response = gson.toJson(tasksList);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else if (stringPath.startsWith("/tasks/task/?id=")) {
                            String[] id = stringPath.split("=");
                            Task task = taskManager.getTaskById(Integer.parseInt(id[1]));
                            String response = gson.toJson(task);
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
                        Task task = gson.fromJson(jsonObject, Task.class);
                        List<Task> taskList = taskManager.getAllListTask();
                        if (httpExchange.getRequestURI().toString().equals("/tasks/task/")) {
                            if (taskList.contains(task)) {
                                taskManager.updateTask(task);
                            } else {
                                taskManager.addNewTask(task);
                            }
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                        break;
                    case DELETE:
                        if ("/tasks/task/".equals(stringPath)) {
                            taskManager.clearAllTask();
                        } else {
                            if (stringPath.startsWith("/tasks/task/?id=")) {
                                String[] mass = stringPath.split("=");
                                taskManager.deleteTaskById(Integer.parseInt(mass[1]));
                            }
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        httpExchange.close();
                        break;
                    default:
                        System.out.println("Вызвали не GET у task");
                }
            } catch (Throwable e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    static class EpicHandler implements HttpHandler {
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

    static class SubtaskHandler implements HttpHandler {
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

    static class PrioritizedTasksHandler implements HttpHandler {
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
}