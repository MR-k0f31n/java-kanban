package ru.yandex.kanban.manager.util;

import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    public String convertToStringTask(Task task) {
        String[] arrString;
        if (task.getStartTime() != null) {
            arrString = new String[]{
                    Integer.toString(task.getId()),
                    task.getTypeTask().name(),
                    task.getName(),
                    task.getStatus().name(),
                    task.getStartTime().toString(),
                    String.valueOf(task.getDuration().toMinutes()),
                    task.getDescription()
            };
        } else {
            arrString = new String[]{
                    Integer.toString(task.getId()),
                    task.getTypeTask().name(),
                    task.getName(),
                    task.getStatus().name(),
                    "null",
                    String.valueOf(task.getDuration().toMinutes()),
                    task.getDescription()
            };
        }
        return String.join(",", arrString);
    }


    public String convertToStringTask(EpicTask task) {
        String[] arrString;
        if (task.getStartTime() != null) {
            arrString = new String[]{
                    Integer.toString(task.getId()),
                    task.getTypeTask().name(),
                    task.getName(),
                    task.getStatus().name(),
                    task.getStartTime().toString(),
                    String.valueOf(task.getDuration().toMinutes()),
                    task.getDescription()
            };
        } else {
            arrString = new String[]{
                    Integer.toString(task.getId()),
                    task.getTypeTask().name(),
                    task.getName(),
                    task.getStatus().name(),
                    "null",
                    String.valueOf(task.getDuration().toMinutes()),
                    task.getDescription()
            };
        }
        return String.join(",", arrString);
    }


    public String convertToStringTask(SubTask task) {
        String[] arrString;
        if (task.getStartTime() != null) {
            arrString = new String[]{
                    Integer.toString(task.getId()),
                    task.getTypeTask().name(),
                    task.getName(),
                    task.getStatus().name(),
                    task.getStartTime().toString(),
                    String.valueOf(task.getDuration().toMinutes()),
                    task.getDescription(),
                    Integer.toString(task.getEpicID())
            };
        } else {
            arrString = new String[]{
                    Integer.toString(task.getId()),
                    task.getTypeTask().name(),
                    task.getName(),
                    task.getStatus().name(),
                    "null",
                    String.valueOf(task.getDuration().toMinutes()),
                    task.getDescription(),
                    Integer.toString(task.getEpicID())
            };
        }
        return String.join(",", arrString);
    }

    public String toStringHistory(List<Task> list) {
        if (list.isEmpty()) {
            return " ";
        } else {
            StringBuilder sb = new StringBuilder();
            String delimiter = "";
            for (Task task : list) {
                sb.append(delimiter);
                delimiter = ",";
                sb.append(task.getId());
            }
            return sb.toString();
        }
    }

    public static Task taskFromString(String[] strArr) {
        if (!strArr[4].equals("null")) {
            return new Task(
                    Integer.parseInt(strArr[0]),
                    strArr[2],
                    strArr[6],
                    LocalDateTime.parse(strArr[4]),
                    Status.valueOf(strArr[3]),
                    Integer.parseInt(strArr[5])
            );
        } else {
            return new Task(
                    Integer.parseInt(strArr[0]),
                    strArr[2],
                    strArr[6],
                    null,
                    Status.valueOf(strArr[3]),
                    Integer.parseInt(strArr[5])
            );
        }
        // file  0id, 1type, 2name, 3status, 4dataTime, 5duration, 6description, 7epic
        // constructor id, name, description, dataTime, status, duration, from Sub EpicID
    }

    public static EpicTask EpicTaskFromString(String[] strArr) {
        if (!strArr[4].equals("null")) {
            return new EpicTask(
                    Integer.parseInt(strArr[0]),
                    strArr[2],
                    strArr[6],
                    LocalDateTime.parse(strArr[4]),
                    Status.valueOf(strArr[3]),
                    Integer.parseInt(strArr[5])
            );
        } else {
            return new EpicTask(
                    Integer.parseInt(strArr[0]),
                    strArr[2],
                    strArr[6],
                    null,
                    Status.valueOf(strArr[3]),
                    Integer.parseInt(strArr[5])
            );
        }
    }

    public static SubTask SubTaskFromString(String[] strArr) {
        if (!strArr[4].equals("null")) {
            return new SubTask(
                    Integer.parseInt(strArr[0]),
                    strArr[2],
                    strArr[6],
                    LocalDateTime.parse(strArr[4]),
                    Status.valueOf(strArr[3]),
                    Integer.parseInt(strArr[5]),
                    Integer.parseInt(strArr[7])
            );
        } else {
            return new SubTask(
                    Integer.parseInt(strArr[0]),
                    strArr[2],
                    strArr[6],
                    null,
                    Status.valueOf(strArr[3]),
                    Integer.parseInt(strArr[5]),
                    Integer.parseInt(strArr[7])
            );
        }
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
