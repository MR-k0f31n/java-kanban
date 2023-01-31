package ru.yandex.kanban.servers.implemented.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class TaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder().setPrettyPrinting()
            .serializeNulls().create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    private TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String stringPath = httpExchange.getRequestURI().getPath();
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case GET:
                if (stringPath.equals("/tasks/")) {
                    System.out.println("Началась обработка /tasks запроса от клиента.");
                    getPrioritizedTasks(httpExchange);
                } else if (stringPath.startsWith("/tasks/task/")) {
                    System.out.println("Началась обработка /tasks/task запроса от клиента.");
                    getAllTask(httpExchange);
                } else if (stringPath.startsWith("/tasks/task/?id=")) {
                    System.out.println("Началась обработка /tasks/task by ID запроса от клиента.");
                    String[] id = stringPath.split("=");
                    int idTask = Integer.parseInt(id[1]);
                    getTaskById(httpExchange, idTask);
                } else {
                    System.out.println("Неверный путь");
                }
                break;
            case POST:
                System.out.println("Началась обработка /tasks/task запроса от клиента.");
                String response;
                InputStream inputStream = httpExchange.getRequestBody();
                String jsonString = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

                Task task = gson.fromJson(jsonString, Task.class);

                List<Task> taskList = taskManager.getAllListTask();
                if (task != null) {
                    if (taskList.contains(task)) {
                        taskManager.updateTask(task);
                        response = "Такая задача существует и была обновленна";
                        httpExchange.sendResponseHeaders(208, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(response.getBytes());
                        }
                    } else {
                        taskManager.addNewTask(task);
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
                System.out.println("Метод не найден");
                response = "Во время выполнения запроса возникла ошибка.";
                httpExchange.sendResponseHeaders(404, 0);
                try (OutputStream outputStream = httpExchange.getResponseBody()) {
                    outputStream.write(response.getBytes());
                }
        }
    }

    private void getPrioritizedTasks (HttpExchange httpExchange) throws IOException {
        String response;
        if (taskManager.getPrioritizedTasks().isEmpty()) {
            response = "На данный момент список всех задач пуст";
            httpExchange.sendResponseHeaders(200, 0);
        } else {
            response = gson.toJson(taskManager.getPrioritizedTasks());
            httpExchange.sendResponseHeaders(200, 0);
        }
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        }
    }

    private void getAllTask (HttpExchange httpExchange) throws IOException {
        String response;
        if (taskManager.getAllListTask().isEmpty()) {
            response = "На данный момент список задач пуст";
            httpExchange.sendResponseHeaders(200, 0);
        } else {
            response = gson.toJson(taskManager.getAllListTask());
            httpExchange.sendResponseHeaders(200, 0);
        }
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        }
    }

    private void getTaskById (HttpExchange httpExchange, int id) throws IOException {
        String response = gson.toJson(taskManager.getTaskById(id));
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        }
    }
}