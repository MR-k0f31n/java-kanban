package ru.yandex.kanban.manager.util;

import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.enums.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm | dd-MM-yy ");

    public String convertToStringTask(Task task) {
        String[] arrString = {
                Integer.toString(task.getId()),
                task.getTypeTask().name(),
                task.getName(),
                task.getStatus().name(),
                task.getStartTime().toString(),
                String.valueOf(task.getDuration().toMinutes()),
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
                task.getStartTime().toString(),
                String.valueOf(task.getDuration().toMinutes()),
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
                task.getStartTime().toString(),
                String.valueOf(task.getDuration().toMinutes()),
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
        return new Task(
                Integer.parseInt(strArr[0]),
                strArr[2],
                strArr[6],
                LocalDateTime.parse(strArr[4], FORMATTER),
                Status.valueOf(strArr[3]),
                Integer.parseInt(strArr[5])
        );
        // file  0id, 1type, 2name, 3status, 4dataTime, 5duration, 6description, 7epic
        // constructor id, name, description, dataTime, status, duration, from Sub EpicID
    }

    public static EpicTask EpicTaskFromString(String[] strArr) {
        return new EpicTask(
                Integer.parseInt(strArr[0]),
                strArr[2],
                strArr[6],
                LocalDateTime.parse(strArr[4], FORMATTER),
                Status.valueOf(strArr[3]),
                Integer.parseInt(strArr[5])
        );
    }

    public static SubTask SubTaskFromString(String[] strArr) {
        return new SubTask(
                Integer.parseInt(strArr[0]),
                strArr[2],
                strArr[6],
                LocalDateTime.parse(strArr[4], FORMATTER),
                Status.valueOf(strArr[3]),
                Integer.parseInt(strArr[5]),
                Integer.parseInt(strArr[7])
        );
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
