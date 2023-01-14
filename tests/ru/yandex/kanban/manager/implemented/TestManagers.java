package ru.yandex.kanban.manager.implemented;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.util.List;


abstract class TestManagers <T extends TaskManager> {

    public abstract T createManager();
    private T manager;

    @BeforeEach
    public void create () {
        manager = createManager();
    }

    @Test
    public void testAddTask () {
        final int idTask = manager.addNewTask(new Task("TaskONe name", "Description from TaskONe"));
        final Task taskTest = manager.getTaskById(idTask);

        Assertions.assertEquals(1, taskTest.getId());
        Assertions.assertEquals(Status.NEW, taskTest.getStatus());
        Assertions.assertEquals(TypeTask.TASK, taskTest.getTypeTask());

        final List<Task> tasks = manager.getAllListTask();

        Assertions.assertNotNull(tasks, "Задачи на возвращаются.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(taskTest, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void testAddEpicAndSubTask () {
        final int idEpicOne = manager.addNewTask(new EpicTask(
                "EpicTaskOne name",
                "EpicTaskOne des description"
        ));

        final int idEpicTwo = manager.addNewTask(new EpicTask(
                "EpicTaskTwo name",
                "EpicTaskTwo des description"
        ));

        final EpicTask epicTaskOne = manager.getEpicById(idEpicOne);
        final EpicTask epicTaskTwo = manager.getEpicById(idEpicOne);

        Assertions.assertEquals(0, epicTaskOne.getSubTaskIds().size(), "Там что-то есть 0_0");
        Assertions.assertEquals(Status.NEW, epicTaskOne.getStatus(), "У эпика нет подзадач, почему не новый?");

        final int idSubOneFromOneEpic = manager.addNewTask(new SubTask(
                "SubOneFromOneEpic name",
                "SubOneFromOneEpic des description",
                idEpicOne
        ));

        final int idSubTwoFromOneEpic = manager.addNewTask(new SubTask(
                "SubTwoFromOneEpic name",
                "SubTwoFromOneEpic des description",
                idEpicOne
        ));

        final int idSubOneFromTwoEpic = manager.addNewTask(new SubTask(
                "SubOneFromTwoEpic name",
                "SubOneFromTwoEpic des description",
                idEpicTwo
        ));

        final int idSubTwoFromTwoEpic = manager.addNewTask(new SubTask(
                "SubTwoFromTwoEpic name",
                "SubTwoFromTwoEpic des description",
                idEpicTwo
        ));

        final SubTask subTaskOne = manager.getSubById(idSubOneFromOneEpic);
        final SubTask subTaskTwo = manager.getSubById(idSubTwoFromOneEpic);
        final SubTask subTaskOneFromTwo = manager.getSubById(idSubOneFromTwoEpic);
        final SubTask subTaskTwoFromTwo = manager.getSubById(idSubTwoFromTwoEpic);

        Assertions.assertEquals(idEpicOne, subTaskOne.getEpicID());


    }
}