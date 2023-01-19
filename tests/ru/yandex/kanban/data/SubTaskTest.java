package ru.yandex.kanban.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    private SubTask subTask;

    @BeforeEach
    public void create () {
        subTask = new SubTask(
                1,
                "Первый саб",
                "Описание первого сабТаска",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                Status.NEW,
                15,
                3
        );
    }

    @Test
    public void testReturnIdEpic () {
        Assertions.assertEquals(3, subTask.getEpicID());
    }

    @Test
    public void itsTaskReturnTask () {
        Assertions.assertEquals(TypeTask.SUB_TASK, subTask.getTypeTask());
    }

    @Test
    public void testSetAndGetFunctionFromTaskReturnAllField () {
        subTask.setId(5);
        subTask.setName("Create task1");
        subTask.setDescription("Description task1");
        subTask.setStatus(Status.DONE);

        Assertions.assertEquals(5, subTask.getId());
        Assertions.assertEquals("Create task1", subTask.getName());
        Assertions.assertEquals("Description task1", subTask.getDescription());
        Assertions.assertEquals(Status.DONE, subTask.getStatus());
    }

    @Test
    public void testOverrideMethodToStringToTask () {
        final String taskToString = "SubTask{epicID=3, id=1, name='10', status='NEW', description='25', " +
                "startTime=2022-12-21T10:25, duration=PT15M}";
        Assertions.assertEquals(taskToString, subTask.toString());
    }

    @Test
    public void testOverrideEqualsFromTaskEqualsTask () {
        final SubTask taskTest = new SubTask(
                1,
                "Задание было сложным",
                "Но мы с лопатой",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                Status.NEW,
                15,
                3
        );

        Assertions.assertNotEquals(taskTest, subTask);
    }

    @Test
    public void testConstructorFromAddTaskEqualsTaskAndTestTask () {
        final SubTask taskTest = new SubTask(
                "Первый саб",
                "Описание первого сабТаска",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15,
                3
        );

        Assertions.assertEquals("Первый саб", taskTest.getName());
        Assertions.assertEquals("Описание первого сабТаска", taskTest.getDescription());
    }

    @Test
    public void testAllTimeMethodeCorrectCalculateTime () {
        Assertions.assertEquals(subTask.getStartTime(),
                LocalDateTime.of(2022, 12, 21, 10, 25));
        Assertions.assertEquals(subTask.getDuration(), Duration.ofMinutes(15));
        Assertions.assertEquals(subTask.getEndTime(),
                LocalDateTime.of(2022, 12, 21, 10, 25).plus(Duration.ofMinutes(15)));

        subTask.setStartTime(null);
        subTask.setDuration(null);
        Assertions.assertNull(subTask.getStartTime());
        Assertions.assertNull(subTask.getDuration());
        Assertions.assertNull(subTask.getEndTime());
    }
}