package ru.yandex.kanban.manager.implemented;

import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.enums.TypeTask;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.exceptions.ManagerSaveException;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private final static String PATH = ("resources/history.csv");
    private static final String HEAD = "id,type,name,status,description,epic";

    public FileBackedTasksManager() {
        super();
    }


    private void save() {
        try (Writer writer = new FileWriter(PATH, StandardCharsets.UTF_8, false)) {
            writer.write(HEAD + "\n");
            for (Task task : getTaskMap().values()) {
                writer.write(getTaskToString(task));
                writer.write("\n");
            }
            for (Map.Entry<Integer, EpicTask> entry : getEpicTaskMap().entrySet()) {
                writer.write(getTaskToString(entry.getValue()));
                writer.write("\n");
            }
            for (Map.Entry<Integer, SubTask> entry : getSubTaskMap().entrySet()) {
                writer.write(getTaskToString(entry.getValue()));
                writer.write("\n");
            }
            writer.write("\n");
            writer.write(toStringHistory());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private String getTaskToString(Task task) {
        String[] arrString = {
                Integer.toString(task.getId()),
                task.getTypeTask().name(),
                task.getName(),
                task.getStatus().name(),
                task.getDescription()
        };
        return String.join(",", arrString);
    }

    private String getTaskToString(EpicTask task) {
        String[] arrString = {
                Integer.toString(task.getId()),
                task.getTypeTask().name(),
                task.getName(),
                task.getStatus().name(),
                task.getDescription()
        };
        return String.join(",", arrString);
    }

    private String getTaskToString(SubTask task) {
        String[] arrString = {
                Integer.toString(task.getId()),
                task.getTypeTask().name(),
                task.getName(),
                task.getStatus().name(),
                task.getDescription(),
                Integer.toString(task.getEpicID())
        };
        return String.join(",", arrString);
    }

    private String toStringHistory() {
        List<Task> list = getHistory();
        StringBuilder sb = new StringBuilder();
        String delimiter = "";
        for (Task task : list) {
            sb.append(delimiter);
            delimiter = ",";
            sb.append(task.getId());
        }
        return sb.toString();
    }

    public static FileBackedTasksManager loadFromFile() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try {
            String file = Files.readString(Path.of(PATH));
            String[] line = file.split("\r?\n");
            List<Integer> historyList = historyFromString(line[line.length-1]);
            int maxId = 0;

            for (int i = 1; i < line.length - 2; i++) {
                String[] parts = line[i].split(",");
                TypeTask type = TypeTask.valueOf(parts[1]);
                int id = Integer.parseInt(parts[0]);

                switch (type) {
                    case TASK -> {
                        Task task = taskFromString(parts);
                        fileBackedTasksManager.taskMap.put(id, task);
                    }
                    case EPIC_TASK -> {
                        EpicTask epicTask = EpicTaskFromString(parts);
                        fileBackedTasksManager.epicTaskMap.put(id, epicTask);
                    }
                    case SUB_TASK -> {
                        SubTask subTask = SubTaskFromString(parts);
                        fileBackedTasksManager.subTaskMap.put(id, subTask);
                    }
                }
                if (id > maxId) {
                    maxId = id;
                }
            }
            synchEpicAndSubTask(fileBackedTasksManager);
            fileBackedTasksManager.currencyID = maxId++;

            for (Integer id : historyList) {
                if (id != null) {
                    if (fileBackedTasksManager.taskMap.containsKey(id)) {
                        fileBackedTasksManager.getTaskById(id);
                    } else if (fileBackedTasksManager.epicTaskMap.containsKey(id)) {
                        fileBackedTasksManager.getEpicById(id);
                    } else if (fileBackedTasksManager.subTaskMap.containsKey(id)) {
                        fileBackedTasksManager.getSubById(id);
                    }
                }
            }
        } catch (IOException exc) {
            throw new ManagerSaveException("Невозможно прочитать файл.");
        }
        return fileBackedTasksManager;
    }

    private static void synchEpicAndSubTask (FileBackedTasksManager fileBackedTasksManager) {
        for (SubTask task : fileBackedTasksManager.subTaskMap.values()) {
            if (task != null) {
                int idSub = task.getId();
                int idEpic = task.getEpicID();
                if(fileBackedTasksManager.epicTaskMap.containsKey(idEpic)) {
                    fileBackedTasksManager.epicTaskMap.get(idEpic).addSubTaskIds(idSub);
                    fileBackedTasksManager.syncEpicTaskStatus(idEpic);
                }
            }
        }

    }

    private static Task taskFromString(String[] strArr) {
        return new Task(Integer.parseInt(strArr[0]), strArr[2], strArr[4], Status.valueOf(strArr[3]));
    }

    private static EpicTask EpicTaskFromString(String[] strArr) {
        return new EpicTask(Integer.parseInt(strArr[0]), strArr[2], strArr[4], Status.valueOf(strArr[3]));
    }

    private static SubTask SubTaskFromString(String[] strArr) {
        return new SubTask(Integer.parseInt(strArr[0]), strArr[2], strArr[4], Status.valueOf(strArr[3]),
                Integer.parseInt(strArr[5]));
    }

    private static List<Integer> historyFromString(String str) {
        List<Integer> list = new ArrayList<>();

        if (!str.isBlank()) {
            String[] listIds = str.split(",");

            for (String id : listIds) {
                list.add(Integer.parseInt(id));
            }
        }
        return list;
    }

    @Override
    public int addNewTask(Task task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addNewTask(EpicTask task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addNewTask(SubTask task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicTaskById(int id) {
        super.deleteEpicTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    public static void main(String[] args) {
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile();

        System.out.println("Все добыто и затестил из текущего мейн, потом просто читаю файл отсюда (ツ)");
        System.out.println("Можно проверить переподрубить вместо memory файловый образец," +
                "пока тестил запись так и сделал");
        System.out.println("newManager: все таски - " + newManager.getAllListTask());
        System.out.println("newManager: все эпики - " + newManager.getAllEpicTask());
        System.out.println("newManager: все сабы - " + newManager.getAllSubTask());
        System.out.println("newManager: Текущий ID: " + newManager.currencyID);
        System.out.println("newManager: история - " + newManager.getHistory());
    }
}