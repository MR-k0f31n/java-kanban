package ru.yandex.kanban.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {

    private EpicTask epicTask;
    @BeforeEach
    public void create () {
        epicTask = new EpicTask(
                1,
                "Задание оказалось еще сложнее",
                "Лопата уже хрустела",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                Status.NEW,
                15
        );
    }

    @Test
    public void itsEpicTask () {
        Assertions.assertEquals(TypeTask.EPIC_TASK, epicTask.getTypeTask());
    }

    @Test
    public void testNullInListSubs () {
        Assertions.assertNotNull(epicTask.getSubTaskIds());
    }

    @Test
    public void testAddList () {
        final List subList = List.of(5, 15, 25, 35);

        epicTask.addSubTaskIds(5);
        epicTask.addSubTaskIds(15);
        epicTask.addSubTaskIds(25);
        epicTask.addSubTaskIds(35);

        Assertions.assertEquals(subList, epicTask.getSubTaskIds());
    }

    @Test
    public void testOverrideMethodToStringToTask () {
        final String taskToString =
                "EpicTask{subTaskIDs=[], id=1, name='29', status='NEW', description='19'," +
                        " startTime=2022-12-21T10:25, duration=PT15M}";
        Assertions.assertEquals(taskToString, epicTask.toString());
    }

    @Test
    public void testConstructorFromAddTaskEqualsTaskAndTestTask () {
        final EpicTask taskTest = new EpicTask(
                "Create task1",
                "Description task1",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15
        );

        Assertions.assertEquals("Create task1", taskTest.getName());
        Assertions.assertEquals("Description task1", taskTest.getDescription());
    }

    @Test
    public void testSetAndGetFunctionFromTaskReturnAllField () {
        epicTask.setId(5);
        epicTask.setName("Create task1");
        epicTask.setDescription("Description task1");

        Assertions.assertEquals(5, epicTask.getId());
        Assertions.assertEquals("Create task1", epicTask.getName());
        Assertions.assertEquals("Description task1", epicTask.getDescription());
    }

    @Test
    public void testOverrideEqualsFromTaskEqualsTask () {
        final EpicTask taskTest = new EpicTask(
                1,
                "Задание оказалось еще сложнее",
                "Лопата уже хрустела",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                Status.NEW,
                15
        );

        Assertions.assertEquals(taskTest, epicTask);
    }

    @Test
    public void itsTaskReturnTask () {
        Assertions.assertEquals(TypeTask.EPIC_TASK, epicTask.getTypeTask());
    }

    @Test
    public void testAllTimeMethodeCorrectCalculateTime () {
        Assertions.assertEquals(epicTask.getStartTime(),
                LocalDateTime.of(2022, 12, 21, 10, 25));
        Assertions.assertEquals(epicTask.getDuration(), Duration.ofMinutes(15));
        Assertions.assertNull(epicTask.getEndTime());

        epicTask.setStartTime(null);
        epicTask.setDuration(null);
        Assertions.assertNull(epicTask.getStartTime());
        Assertions.assertNull(epicTask.getDuration());


    }

    @Test
    public void testSetEndTime () {
        epicTask.setEndTime(LocalDateTime.of(2022, 12, 21, 10, 25));
        Assertions.assertEquals(epicTask.getEndTime(), LocalDateTime.of(2022, 12, 21, 10, 25));
    }
}