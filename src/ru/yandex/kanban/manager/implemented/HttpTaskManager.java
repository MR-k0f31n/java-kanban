package ru.yandex.kanban.manager.implemented;

import com.google.gson.*;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.servers.implemented.KVTaskClient;

import java.io.IOException;


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
        kvTaskClient.put("task", gson.toJson(taskMap.values()));
        kvTaskClient.put("epicTask", gson.toJson(epicTaskMap.values()));
        kvTaskClient.put("subTask", gson.toJson(subTaskMap.values()));
        kvTaskClient.put("history", gson.toJson(getHistory()));
    }

    public void load() {
        JsonElement loadTask = JsonParser.parseString(kvTaskClient.load("task"));
        JsonElement loadEpicTask = JsonParser.parseString(kvTaskClient.load("epicTask"));
        JsonElement loadSubTask = JsonParser.parseString(kvTaskClient.load("subTask"));
        JsonElement loadHistory = JsonParser.parseString(kvTaskClient.load("history"));

        if (!loadTask.isJsonNull()) {
            JsonArray loadTaskToArray = loadTask.getAsJsonArray();
            for (JsonElement jsonElement : loadTaskToArray) {
                Task task = gson.fromJson(jsonElement, Task.class);
                super.taskMap.put(task.getId(), task);
                super.listOfTasksSortedByTime.add(task);
            }
        }

        if (!loadEpicTask.isJsonNull()) {
            JsonArray loadEpicTaskToArray = loadEpicTask.getAsJsonArray();
            for (JsonElement jsonElement : loadEpicTaskToArray) {
                EpicTask epicTask = gson.fromJson(jsonElement, EpicTask.class);
                super.epicTaskMap.put(epicTask.getId(), epicTask);
                super.listOfTasksSortedByTime.add(epicTask);
            }
        }

        if (!loadSubTask.isJsonNull() && !loadEpicTask.isJsonNull()) {
            JsonArray loadSubTaskToArray = loadSubTask.getAsJsonArray();
            for (JsonElement jsonElement : loadSubTaskToArray) {
                SubTask subTask = gson.fromJson(jsonElement, SubTask.class);
                super.subTaskMap.put(subTask.getId(), subTask);
                super.listOfTasksSortedByTime.add(subTask);
            }
        }

        if (loadHistory != null) {
            JsonArray loadHistoryToArray = loadHistory.getAsJsonArray();
            for (JsonElement JsonElement : loadHistoryToArray) {
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
