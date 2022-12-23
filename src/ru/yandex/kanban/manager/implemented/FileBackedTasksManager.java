package ru.yandex.kanban.manager.implemented;

import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.exceptions.ManagerSaveException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private void save() {
        try (Writer writer = new FileWriter("resources/history.csv", StandardCharsets.UTF_8, true)) {
            String firstStr = "id,type,name,status,description,epic";
            writer.write(firstStr + "\n");
            for (Task task : getTaskMap().values()) {
                writer.write(getTaskString(task));
                writer.write("\n");
            }
            for (Map.Entry<Integer, EpicTask> entry : getEpicTaskMap().entrySet()) {
                writer.write(getTaskString(entry.getValue()));
                writer.write("\n");
            }
            for (Map.Entry<Integer, SubTask> entry : getSubTaskMap().entrySet()) {
                writer.write(getTaskString(entry.getValue()));
                writer.write("\n");
            }
            writer.write("\n");
            //writer.write(toString(getHistoryManager()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private String getTaskString (Task task) {
        String[] arrString = {
                Integer.toString(task.getId()),
                task.getTypeTask().name(),
                task.getName(),
                task.getStatus().name(),
                task.getDescription()
        };
        String string = String.join(",", arrString);
        return string;
    }

    private String getTaskString (EpicTask task) {
        String[] arrString = {
                Integer.toString(task.getId()),
                task.getTypeTask().name(),
                task.getName(),
                task.getStatus().name(),
                task.getDescription()
        };
        String string = String.join(",", arrString);
        return string;
    }

    private String getTaskString (SubTask task) {
        String[] arrString = {
                Integer.toString(task.getId()),
                task.getTypeTask().name(),
                task.getName(),
                task.getStatus().name(),
                task.getDescription(),
                Integer.toString(task.getEpicID())
        };
        String string = String.join(",", arrString);
        return string;
    }

    @Override
    public int addNewTask (Task task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addNewTask (EpicTask task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addNewTask (SubTask task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

}

