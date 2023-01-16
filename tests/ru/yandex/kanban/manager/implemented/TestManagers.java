package ru.yandex.kanban.manager.implemented;

import org.junit.jupiter.api.*;
import ru.yandex.kanban.data.EpicTask;
import ru.yandex.kanban.data.SubTask;
import ru.yandex.kanban.data.Task;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.time.LocalDateTime;
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
        final int idTask = manager.addNewTask(new Task("TaskONe name",
                "Description from TaskONe",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15
                ));
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
    public void testAddEpicAndSubTaskAndDelete () {
        final int idEpicOne = manager.addNewTask(new EpicTask(
                "EpicTaskOne name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15
        ));

        final EpicTask epicTaskOne = manager.getEpicById(idEpicOne);

        /**
         * Test Epic with empty list sub's ID
         * subtasks empty: Epic status - NEW
         * not NULL
         */

        Assertions.assertEquals(0, epicTaskOne.getSubTaskIds().size(), "Там что-то есть 0_0");
        Assertions.assertEquals(Status.NEW, epicTaskOne.getStatus(), "У эпика нет подзадач, почему не новый?");
        Assertions.assertNotNull(epicTaskOne.getSubTaskIds());

        final int idSubOneFromOneEpic = manager.addNewTask(new SubTask(
                "SubOneFromOneEpic name",
                "SubOneFromOneEpic des description",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15,
                idEpicOne

        ));

        final int idSubTwoFromOneEpic = manager.addNewTask(new SubTask(
                "SubTwoFromOneEpic name",
                "SubTwoFromOneEpic des description",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15,
                idEpicOne
        ));

        final int idSubThreeFromOneEpic = manager.addNewTask(new SubTask(
                "SubThreeFromOneEpic name",
                "SubThreeFromOneEpic des description",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15,
                idEpicOne
        ));

        final SubTask subTaskOne = manager.getSubById(idSubOneFromOneEpic);
        final SubTask subTaskTwo = manager.getSubById(idSubTwoFromOneEpic);
        final SubTask subTaskThree = manager.getSubById(idSubThreeFromOneEpic);
        List<Integer> listID = List.of(
                subTaskOne.getId(),
                subTaskTwo.getId(),
                subTaskThree.getId()
        );

        /**
         * Test the presence of epic task in subtask
         * Check status if subtask all NEW
         * List id Comparison in a subtask and Epic task
         */

        Assertions.assertEquals(idEpicOne, subTaskOne.getEpicID());
        Assertions.assertEquals(Status.NEW, epicTaskOne.getStatus());
        Assertions.assertEquals(listID, epicTaskOne.getSubTaskIds());

        manager.deleteSubTaskById(idSubOneFromOneEpic);
        manager.deleteSubTaskById(idSubThreeFromOneEpic);

        listID = List.of(subTaskTwo.getId());

        /**
         * Test delete subtask
         */

        Assertions.assertEquals(listID, epicTaskOne.getSubTaskIds());
    }

    @Test
    public void testUpdateStatusEpic () {
        final int idEpicOne = manager.addNewTask(new EpicTask(
                "EpicTaskOne name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15
        ));

        final EpicTask epicTaskOne = manager.getEpicById(idEpicOne);

        final int idSubTwoFromOneEpic = manager.addNewTask(new SubTask(
                "SubTwoFromOneEpic name",
                "SubTwoFromOneEpic des description",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15,
                idEpicOne
        ));

        final SubTask subTaskTwo = manager.getSubById(idSubTwoFromOneEpic);

        final int idSubOneFromFirstEpic = manager.addNewTask(new SubTask(
                "SubOneFromOneEpic name",
                "SubOneFromOneEpic des description",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15,
                idEpicOne
        ));

        final int idSubThreeFromOneEpic = manager.addNewTask(new SubTask(
                "SubThreeFromOneEpic name",
                "SubThreeFromOneEpic des description",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15,
                idEpicOne
        ));

        /**
         * Test from status DONE and NEW in all subtask
         */

        SubTask SubTaskOne = manager.getSubById(idSubOneFromFirstEpic);
        SubTaskOne.setStatus(Status.DONE);
        manager.updateSubTask(SubTaskOne);

        subTaskTwo.setStatus(Status.DONE);
        manager.updateSubTask(subTaskTwo);

        Assertions.assertEquals(Status.IN_PROGRESS, epicTaskOne.getStatus(), "Не все статусы сабов изменились");

        /**
         * Test all subtask DONE
         */

        SubTask SubTaskThree = manager.getSubById(idSubThreeFromOneEpic);
        SubTaskThree.setStatus(Status.DONE);
        manager.updateSubTask(SubTaskThree);

        Assertions.assertEquals(Status.DONE, epicTaskOne.getStatus());

        /**
         * Test all subtask IN_Progress
         */

        SubTaskOne.setStatus(Status.IN_PROGRESS);
        SubTaskThree.setStatus(Status.IN_PROGRESS);
        subTaskTwo.setStatus(Status.IN_PROGRESS);

    }

    @Test
    public void testPriorityTask () {
        final int idEpicOne = manager.addNewTask(new EpicTask(
                "EpicTaskOne name",
                "EpicTaskOne des description",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15
        ));

        final int idSubTwoFromOneEpic = manager.addNewTask(new SubTask(
                "SubTwoFromOneEpic name",
                "SubTwoFromOneEpic des description",
                LocalDateTime.of(2022, 12, 21, 15, 25),
                15,
                idEpicOne
        ));

        final int idSubOneFromFirstEpic = manager.addNewTask(new SubTask(
                "SubOneFromOneEpic name",
                "SubOneFromOneEpic des description",
                LocalDateTime.of(2022, 12, 21, 9, 25),
                15,
                idEpicOne
        ));

        final int idSubThreeFromFirstEpic = manager.addNewTask(new SubTask(
                "SubOneFromOneEpic name",
                "SubOneFromOneEpic des description",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15,
                idEpicOne
        ));

        System.out.println(manager.getEpicById(idEpicOne).getStartTime());
        System.out.println(manager.getEpicById(idEpicOne).getEndTime());
        System.out.println(manager.getEpicById(idEpicOne).getDuration());

    }
}