package ru.yandex.kanban.manager.implemented;

import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.enums.TypeTask;
import ru.yandex.kanban.exceptions.ManagerSaveException;
import ru.yandex.kanban.manager.Managers;
import ru.yandex.kanban.manager.interfaces.TaskManager;
import ru.yandex.kanban.manager.util.Converter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;

import static ru.yandex.kanban.manager.util.Util.getConverter;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Converter converter;
    private final Path path;
    private static final String HEAD = "id,type,name,status,description,epic";

    private FileBackedTasksManager(File file) {
        super();
        this.path = file.toPath();
        this.converter = getConverter();
    }

    private void save() {
        try (Writer writer = new FileWriter(path.toString(), StandardCharsets.UTF_8, false)) {
            writer.write(HEAD + "\n");
            for (Task task : taskMap.values()) {
                writer.write(converter.convertToStringTask(task));
                writer.write("\n");
            }
            for (EpicTask epicTask : epicTaskMap.values()) {
                writer.write(converter.convertToStringTask(epicTask));
                writer.write("\n");
            }
            for (SubTask subTask : subTaskMap.values()) {
                writer.write(converter.convertToStringTask(subTask));
                writer.write("\n");
            }
            writer.write("\n");
            writer.write(converter.toStringHistory(getHistory()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        if (file.exists()) {
            try {
                String fileToLine = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                String[] line = fileToLine.split("\r?\n");
                List<Integer> historyList = Converter.historyFromString(line[line.length - 1]);
                int maxId = 0;

                for (int i = 1; i < line.length - 2; i++) {
                    String[] parts = line[i].split(",");
                    TypeTask type = TypeTask.valueOf(parts[1]);
                    int id = Integer.parseInt(parts[0]);

                    switch (type) {
                        case TASK -> {
                            Task task = Converter.taskFromString(parts);
                            fileBackedTasksManager.taskMap.put(id, task);
                        }
                        case EPIC_TASK -> {
                            EpicTask epicTask = Converter.EpicTaskFromString(parts);
                            fileBackedTasksManager.epicTaskMap.put(id, epicTask);
                        }
                        case SUB_TASK -> {
                            SubTask subTask = Converter.SubTaskFromString(parts);
                            fileBackedTasksManager.subTaskMap.put(id, subTask);
                        }
                    }
                    if (id > maxId) {
                        maxId = id;
                    }
                }
                synchEpicAndSubTask(fileBackedTasksManager);
                recoveryHistory(fileBackedTasksManager, historyList);
                fileBackedTasksManager.currencyID = maxId + 1;


            } catch (IOException exc) {
                throw new ManagerSaveException("Невозможно прочитать файл.");
            }
        } else {
            System.out.println("Файл не обнаружен созадние...");
            try {
                Files.createFile(file.toPath());
            } catch (IOException exc) {
                System.out.println(exc.getMessage());
            }
        }
        return fileBackedTasksManager;
    }

    private static void recoveryHistory(FileBackedTasksManager fileBackedTasksManager, List<Integer> historyList) {
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
    }

    private static void synchEpicAndSubTask(FileBackedTasksManager fileBackedTasksManager) {
        for (SubTask subTask : fileBackedTasksManager.subTaskMap.values()) {
            if (subTask != null) {
                int idSub = subTask.getId();
                int idEpic = subTask.getEpicID();
                if (fileBackedTasksManager.epicTaskMap.containsKey(idEpic)) {
                    fileBackedTasksManager.epicTaskMap.get(idEpic).addSubTaskIds(idSub);
                    fileBackedTasksManager.syncEpicTaskStatus(idEpic);
                }
            }
        }
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

    @Override
    public Task getTaskById(int id) {
        super.getTaskById(id);
        save();
        return taskMap.get(id);
    }

    @Override
    public EpicTask getEpicById(int id) {
        super.getEpicById(id);
        save();
        return epicTaskMap.get(id);
    }

    @Override
    public SubTask getSubById(int id) {
        super.getSubById(id);
        save();
        return subTaskMap.get(id);
    }

    @Override
    public void clearAllTask() {
        super.clearAllTask();
        save();
    }

    @Override
    public void clearAllEpicTask() {
        super.clearAllEpicTask();
        save();
    }

    @Override
    public void clearAllSubTask() {
        super.clearAllSubTask();
        save();
    }

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Если я опять не так сделал", "Лучше пристрелите меня как собаку");

        int taskNum1 = manager.addNewTask(task1);

        Task task2 = new Task("Александр", "Мне кажется вы переоцениваете старика =D ");

        int taskNum2 = manager.addNewTask(task2);

        EpicTask epicTask1 = new EpicTask("Epic1", "descrEpic1");

        int epicTaskNum1 = manager.addNewTask(epicTask1);

        EpicTask epicTask2 = new EpicTask("Epic2", "descrEpic2");

        int epicTaskNum2 = manager.addNewTask(epicTask2);

        SubTask subTask1 = new SubTask("sub1", "descrSub1", epicTaskNum1);

        int subTaskNum1 = manager.addNewTask(subTask1);

        SubTask subTask2 = new SubTask("sub2", "descrSub2", epicTaskNum2);

        int subTaskNum2 = manager.addNewTask(subTask2);

        SubTask subTask3 = new SubTask("sub3", "descrSub3", epicTaskNum2);

        int subTaskNum3 = manager.addNewTask(subTask3);

        manager.getSubById(subTaskNum3);
        manager.getEpicById(epicTaskNum2);
        manager.getTaskById(taskNum1);
        manager.getSubById(subTaskNum2);
        manager.getSubById(subTaskNum1);
        manager.getEpicById(epicTaskNum1);

        System.out.println("Таски: " + manager.getAllListTask());
        System.out.println("Эпики: " + manager.getAllEpicTask());
        System.out.println("Сабы: " + manager.getAllSubTask());
        System.out.println("История: " + manager.getHistory());

        manager.deleteEpicTaskById(epicTaskNum1); // testing delete from save file - OK.

        System.out.println("Таски: " + manager.getAllListTask());
        System.out.println("Эпики: " + manager.getAllEpicTask());
        System.out.println("Сабы: " + manager.getAllSubTask());
        System.out.println("История: " + manager.getHistory());

        TaskManager newManager = Managers.getDefault();

        System.out.println("newManager: все таски - " + newManager.getAllListTask());
        System.out.println("newManager: все эпики - " + newManager.getAllEpicTask());
        System.out.println("newManager: все сабы - " + newManager.getAllSubTask());
        System.out.println("newManager: история - " + newManager.getHistory());
    }
}