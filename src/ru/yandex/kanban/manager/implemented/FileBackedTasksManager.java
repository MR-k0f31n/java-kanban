package ru.yandex.kanban.manager.implemented;

import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.enums.TypeTask;
import ru.yandex.kanban.exceptions.ManagerSaveException;
import ru.yandex.kanban.manager.util.Converter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static ru.yandex.kanban.manager.util.Util.getConverter;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Converter converter;
    private final Path path;
    private static final String HEAD = "id,type,name,status, dataTime, duration, description, epic ";

    private FileBackedTasksManager(File file) {
        super();
        this.path = file.toPath();
        this.converter = getConverter();
    }

    private void save() {
        try (Writer writer = new FileWriter(path.toString())) {
            writer.write(HEAD + System.lineSeparator());
            for (Task task : taskMap.values()) {
                writer.write(converter.convertToStringTask(task)+ System.lineSeparator());
            }
            for (EpicTask epicTask : epicTaskMap.values()) {
                writer.write(converter.convertToStringTask(epicTask) + System.lineSeparator());
            }
            for (SubTask subTask : subTaskMap.values()) {
                writer.write(converter.convertToStringTask(subTask) + System.lineSeparator());
            }
            writer.write(System.lineSeparator());
            writer.write(converter.toStringHistory(getHistory()) + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        if (file.exists()) {
            try {
                String fileToLine = Files.readString(file.toPath());
                String[] line = fileToLine.split(System.lineSeparator());
                List<Integer> historyList;
                int maxId = 0;

                for (int i = 1; i < line.length - 2; i++) {
                    String[] parts = line[i].split(",");
                    TypeTask type = TypeTask.valueOf(parts[1]);
                    int id = 0;

                    switch (type) {
                        case TASK -> {
                            Task task = Converter.taskFromString(parts);
                            fileBackedTasksManager.taskMap.put(task.getId(), task);
                            id = task.getId();
                        }
                        case EPIC_TASK -> {
                            EpicTask epicTask = Converter.EpicTaskFromString(parts);
                            fileBackedTasksManager.epicTaskMap.put(epicTask.getId(), epicTask);
                            id = epicTask.getId();
                        }
                        case SUB_TASK -> {
                            SubTask subTask = Converter.SubTaskFromString(parts);
                            fileBackedTasksManager.subTaskMap.put(subTask.getId(), subTask);
                            id = subTask.getId();
                        }
                    }
                    if (id > maxId) {
                        maxId = id;
                    }
                }
                if (isNumeric(line[line.length - 1])) {
                    historyList = Converter.historyFromString(line[line.length - 1]);
                    recoveryHistory(fileBackedTasksManager, historyList);
                }
                //synchEpicAndSubTask(fileBackedTasksManager);
                returnPriority(fileBackedTasksManager);
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

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void recoveryHistory(FileBackedTasksManager fileBackedTasksManager, List<Integer> historyList) {
        for (Integer id : historyList) {
            if (id != null) {
                if (fileBackedTasksManager.taskMap.containsKey(id)) {
                    fileBackedTasksManager.history.add(fileBackedTasksManager.taskMap.get(id));
                } else if (fileBackedTasksManager.epicTaskMap.containsKey(id)) {
                    fileBackedTasksManager.history.add(fileBackedTasksManager.epicTaskMap.get(id));
                } else if (fileBackedTasksManager.subTaskMap.containsKey(id)) {
                    fileBackedTasksManager.history.add(fileBackedTasksManager.subTaskMap.get(id));
                }
            }
        }
    }

    /*private static void synchEpicAndSubTask(FileBackedTasksManager fileBackedTasksManager) {
        for (SubTask subTask : fileBackedTasksManager.subTaskMap.values()) {
            if (subTask != null) {
                int idSub = subTask.getId();
                int idEpic = subTask.getEpicID();
                if (fileBackedTasksManager.epicTaskMap.containsKey(idEpic)) {
                    fileBackedTasksManager.epicTaskMap.get(idEpic).addSubTaskIds(idSub);
                    //fileBackedTasksManager.syncEpicTaskStatus(idEpic);
                }
            }
        }
    }*/

    private static void returnPriority(FileBackedTasksManager fileBackedTasksManager) {
        fileBackedTasksManager.listOfTasksSortedByTime.addAll(fileBackedTasksManager.getAllListTask());
        fileBackedTasksManager.listOfTasksSortedByTime.addAll(fileBackedTasksManager.getAllEpicTask());
        fileBackedTasksManager.listOfTasksSortedByTime.addAll(fileBackedTasksManager.getAllSubTask());
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

    @Override
    public void syncEpicTaskStatus(int idEpic) {
        super.syncEpicTaskStatus(idEpic);
        save();
    }

    @Override
    public void starAndEndTimeForEpicTask(EpicTask epicTask) {
        super.starAndEndTimeForEpicTask(epicTask);
        save();
    }
}