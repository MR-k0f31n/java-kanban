package ru.yandex.kanban.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {

    private Task task;

    @BeforeEach
    public void create () {
        task = new Task(
                1,
                "Задание было сложным",
                "Но мы с лопатой",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                Status.NEW,
                15
        );
    }

    @Test
    public void testConstructorFromAddTaskEqualsTaskAndTestTask () {
        final Task taskTest = new Task(
                "Create task1",
                "Description task1",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                15
        );

        Assertions.assertEquals("Create task1", taskTest.getName());
        Assertions.assertEquals("Description task1", taskTest.getDescription());
    }

    @Test
    public void testConstructorWithAllFieldEqualsTAskAndTestTask () {
        final Task taskTest = new Task(
                1,
                "Task with all field",
                "Test Description",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                Status.IN_PROGRESS,
                15
        );

        Assertions.assertEquals(1, taskTest.getId());
        Assertions.assertEquals("Task with all field", taskTest.getName());
        Assertions.assertEquals("Test Description", taskTest.getDescription());
        Assertions.assertEquals(Status.IN_PROGRESS, taskTest.getStatus());
    }

    @Test
    public void testSetAndGetFunctionFromTaskReturnAllField () {
        task.setId(5);
        task.setName("Create task1");
        task.setDescription("Description task1");
        task.setStatus(Status.DONE);

        Assertions.assertEquals(5, task.getId());
        Assertions.assertEquals("Create task1", task.getName());
        Assertions.assertEquals("Description task1", task.getDescription());
        Assertions.assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    public void testOverrideEqualsFromTaskEqualsTask () {
        final Task taskTest = new Task(
                1,
                "Задание было сложным",
                "Но мы с лопатой",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                Status.NEW,
                15
        );

        Assertions.assertEquals(taskTest, task);
    }

    @Test
    public void testOverrideMethodToStringToTask () {
        final String taskToString = "Task{id=1, name='20', status='NEW', description='15', startTime=2022-12-21T10:25, duration=PT15M}";
        Assertions.assertEquals(taskToString, task.toString());
    }

    @Test
    public void itsTaskReturnTask () {
        Assertions.assertEquals(TypeTask.TASK, task.getTypeTask());
    }

    @Test
    public void testAllTimeMethodeCorrectCalculateTime () {
        Assertions.assertEquals(task.getStartTime(),
                LocalDateTime.of(2022, 12, 21, 10, 25));
        Assertions.assertEquals(task.getDuration(), Duration.ofMinutes(15));
        Assertions.assertEquals(task.getEndTime(),
                LocalDateTime.of(2022, 12, 21, 10, 25).plus(Duration.ofMinutes(15)));

        task.setStartTime(null);
        task.setDuration(null);
        Assertions.assertNull(task.getStartTime());
        Assertions.assertNull(task.getDuration());
        Assertions.assertNull(task.getEndTime());
    }
}