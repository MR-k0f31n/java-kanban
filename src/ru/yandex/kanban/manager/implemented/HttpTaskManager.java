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

    public HttpTaskManager(File file) throws IOException, InterruptedException {
        super(file);
        this.kvTaskClient = new KVTaskClient("http://localhost:8078");
    }

    @Override
    public void save() {
            kvTaskClient.put("Task", gson.toJson(super.getAllListTask()));
            kvTaskClient.put("Epic", gson.toJson(super.getAllEpicTask()));
            kvTaskClient.put("Subtask", gson.toJson(super.getAllSubTask()));
            kvTaskClient.put("History", gson.toJson(super.getHistory()));
    }

    public void load() throws IOException, InterruptedException {

    }
}
