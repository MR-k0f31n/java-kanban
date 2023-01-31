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
    private final KVTaskClient kvTaskClient;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public HttpTaskManager() throws IOException, InterruptedException {
        super();
        this.kvTaskClient = new KVTaskClient("http://localhost:8078");
        load();
    }

    @Override
    public void save() {
        kvTaskClient.put("Task", gson.toJson(super.getAllListTask()));
        kvTaskClient.put("Epic", gson.toJson(super.getAllEpicTask()));
        kvTaskClient.put("Subtask", gson.toJson(super.getAllSubTask()));
        kvTaskClient.put("History", gson.toJson(super.getHistory()));
    }

    public void load() {
        JsonArray loadTask = kvTaskClient.load("Task");
        JsonArray loadEpicTask = kvTaskClient.load("Epic");
        JsonArray loadSubTask = kvTaskClient.load("Subtask");
        JsonArray loadHistory = kvTaskClient.load("History");

        if (loadTask != null) {
            for (JsonElement jsonElement : loadTask) {
                Task task = gson.fromJson(jsonElement, Task.class);
                super.taskMap.put(task.getId(), task);
                super.listOfTasksSortedByTime.add(task);
            }
        }

        if (loadEpicTask != null) {
            for (JsonElement jsonElement : loadEpicTask) {
                EpicTask epicTask = gson.fromJson(jsonElement, EpicTask.class);
                super.epicTaskMap.put(epicTask.getId(), epicTask);
                super.listOfTasksSortedByTime.add(epicTask);
            }
        }

        if (loadSubTask != null && loadEpicTask != null) {
            for (JsonElement jsonElement : loadSubTask) {
                SubTask subTask = gson.fromJson(jsonElement, SubTask.class);
                super.subTaskMap.put(subTask.getId(), subTask);
                super.listOfTasksSortedByTime.add(subTask);
            }
        }

        if (loadHistory != null) {
            for (JsonElement JsonElement : loadHistory) {
                Integer idTask = JsonElement.getAsInt();
                if (taskMap.containsKey(idTask)) {
                    history.add(taskMap.get(idTask));
                } else if (subTaskMap.containsKey(idTask)) {
                    history.add(subTaskMap.get(idTask));
                } else if (epicTaskMap.containsKey(idTask)) {
                    history.add(epicTaskMap.get(idTask));
                }
            }
        }
    }
}
