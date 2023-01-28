package ru.yandex.kanban.manager.implemented;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.servers.implemented.KVTaskClient;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient kvTaskClient;
    private Gson json = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public HttpTaskManager(File file) throws IOException, InterruptedException {
        super(file);
        this.kvTaskClient = new KVTaskClient("http://localhost:8078");
    }

    @Override
    public void save() {
            kvTaskClient.put("Task", json.toJson(super.getAllListTask()));
            kvTaskClient.put("Epic", json.toJson(super.getAllEpicTask()));
            kvTaskClient.put("Subtask", json.toJson(super.getAllSubTask()));
            kvTaskClient.put("History", json.toJson(super.getHistory()));
    }

    public void load() throws IOException, InterruptedException {
        List<Task> tasks = json.fromJson(kvTaskClient.load("Task"),
                new TypeToken<List<Task>>() {}.getType());
        List<EpicTask> epics = json.fromJson(kvTaskClient.load("Epic"),
                new TypeToken<List<EpicTask>>() {}.getType());
        List<SubTask> subTasks = json.fromJson(kvTaskClient.load("SubTask"),
                new TypeToken<List<SubTask>>() {}.getType());
        List<Task> history = json.fromJson(kvTaskClient.load("History"),
                new TypeToken<List<Task>>() {}.getType());

        for (Task task : tasks) {
            addNewTask(task);
        }
        for (EpicTask epic : epics) {
            addNewTask(epic);
        }
        for (SubTask subTask : subTasks) {
            addNewTask(subTask);
        }
        for (Task task : history) {
            int taskId = task.getId();
            if (tasks.contains(task)) {
                getTaskById(taskId);
            } else if (subTasks.contains(task)) {
                getSubById(taskId);
            } else {
                getEpicById(taskId);
            }
        }
    }
}
