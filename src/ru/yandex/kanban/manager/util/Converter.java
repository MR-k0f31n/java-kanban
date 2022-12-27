package ru.yandex.kanban.manager.util;

import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.enums.Status;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    public String convertToStringTask(Task task) {
        String[] arrString = {
                Integer.toString(task.getId()),
                task.getTypeTask().name(),
                task.getName(),
                task.getStatus().name(),
                task.getDescription()
        };
        return String.join(",", arrString);
    }


    public String convertToStringTask(EpicTask task) {
        String[] arrString = {
                Integer.toString(task.getId()),
                task.getTypeTask().name(),
                task.getName(),
                task.getStatus().name(),
                task.getDescription()
        };
        return String.join(",", arrString);
    }


    public String convertToStringTask(SubTask task) {
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

    public String toStringHistory(List<Task> list) {
        StringBuilder sb = new StringBuilder();
        String delimiter = "";
        for (Task task : list) {
            sb.append(delimiter);
            delimiter = ",";
            sb.append(task.getId());
        }
        return sb.toString();
    }


    public static Task taskFromString(String[] strArr) {
        return new Task(Integer.parseInt(strArr[0]), strArr[2], strArr[4], Status.valueOf(strArr[3]));
    }

    public static EpicTask EpicTaskFromString(String[] strArr) {
        return new EpicTask(Integer.parseInt(strArr[0]), strArr[2], strArr[4], Status.valueOf(strArr[3]));
    }

    public static SubTask SubTaskFromString(String[] strArr) {
        return new SubTask(Integer.parseInt(strArr[0]), strArr[2], strArr[4], Status.valueOf(strArr[3]),
                Integer.parseInt(strArr[5]));
    }

    public static List<Integer> historyFromString(String str) {
        List<Integer> list = new ArrayList<>();

        if (!str.isBlank()) {
            String[] listIds = str.split(",");

            for (String id : listIds) {
                list.add(Integer.parseInt(id));
            }
        }
        return list;
    }
}
