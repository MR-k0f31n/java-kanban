package ru.yandex.kanban.manager.implemented;

import com.google.gson.*;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.servers.implemented.KVTaskClient;
import ru.yandex.kanban.servers.implemented.util.AdapterFromLocalData;
import ru.yandex.kanban.servers.implemented.util.AdapterFromDuration;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskManager extends FileBackedTasksManager {
    protected final KVTaskClient kvTaskClient;
    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new AdapterFromLocalData())
            .registerTypeAdapter(Duration.class, new AdapterFromDuration())
            .create();

    public HttpTaskManager() throws IOException, InterruptedException {
        super();
        this.kvTaskClient = new KVTaskClient();
        load();
    }

    public String getApiToken () {
        return kvTaskClient.getApiToken();
    }

    public void  setApiToken (String apiToken) {
        kvTaskClient.setApiToken(apiToken);
    }

    @Override
    public void save() {
        kvTaskClient.put("task", gson.toJson(getAllListTask()));
        kvTaskClient.put("epicTask", gson.toJson(getAllEpicTask()));
        kvTaskClient.put("subTask", gson.toJson(getAllSubTask()));
        kvTaskClient.put("history", gson.toJson(getHistory()));
    }

    public void load() {
        int maxID = 0;
        String loadTaskString = kvTaskClient.load("task");
        String loadEpicTaskString = kvTaskClient.load("epicTask");
        String loadSubTaskString = kvTaskClient.load("subTask");
        String loadHistoryString = kvTaskClient.load("history");

        if (loadTaskString != null && !loadTaskString.isBlank()) {
            JsonElement loadTask = JsonParser.parseString(loadTaskString);

            if (!loadTask.isJsonNull()) {
                JsonArray loadTaskToArray = loadTask.getAsJsonArray();
                for (JsonElement jsonElement : loadTaskToArray) {
                    Task task = gson.fromJson(jsonElement, Task.class);
                    super.taskMap.put(task.getId(), task);
                    super.listOfTasksSortedByTime.add(task);
                    int idTask = task.getId();
                    if (idTask > maxID) {
                        maxID = idTask;
                    }
                }
            }
        }

        if (loadEpicTaskString != null && !loadEpicTaskString.isBlank()) {
            JsonElement loadEpicTask = JsonParser.parseString(loadEpicTaskString);

            if (!loadEpicTask.isJsonNull()) {
                JsonArray loadEpicTaskToArray = loadEpicTask.getAsJsonArray();
                for (JsonElement jsonElement : loadEpicTaskToArray) {
                    EpicTask epicTask = gson.fromJson(jsonElement, EpicTask.class);
                    super.epicTaskMap.put(epicTask.getId(), epicTask);
                    super.listOfTasksSortedByTime.add(epicTask);
                    int idTask = epicTask.getId();
                    if (idTask > maxID) {
                        maxID = idTask;
                    }
                }
            }
        }

        if (loadSubTaskString != null && !loadSubTaskString.isBlank()) {
            JsonElement loadSubTask = JsonParser.parseString(loadSubTaskString);
            if (!loadSubTask.isJsonNull()) {
                JsonArray loadSubTaskToArray = loadSubTask.getAsJsonArray();
                for (JsonElement jsonElement : loadSubTaskToArray) {
                    SubTask subTask = gson.fromJson(jsonElement, SubTask.class);
                    super.subTaskMap.put(subTask.getId(), subTask);
                    super.listOfTasksSortedByTime.add(subTask);
                    int idTask = subTask.getId();
                    if (idTask > maxID) {
                        maxID = idTask;
                    }
                }
            }
        }

        if (loadHistoryString != null && !loadHistoryString.isBlank()) {
            JsonElement loadHistory = JsonParser.parseString(loadHistoryString);
            if (loadHistory != null) {
                JsonArray loadHistoryToArray = loadHistory.getAsJsonArray();
                for (JsonElement JsonElement : loadHistoryToArray) {
                    Task task = gson.fromJson(JsonElement, Task.class);
                    history.add(task);
                }
            }
        }

        currencyID = maxID + 1;
    }
}
