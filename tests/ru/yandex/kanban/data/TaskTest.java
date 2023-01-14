package ru.yandex.kanban.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

class TaskTest {

    private Task task;

    @BeforeEach
    public void create () {
        task = new Task(
                1,
                "Задание было сложным",
                "Но мы с лопатой",
                Status.NEW
        );
    }

    @Test
    public void testConstructorFromAddTask () {
        final Task taskTest = new Task(
                "Create task1",
                "Description task1"
        );

        Assertions.assertEquals("Create task1", taskTest.getName());
        Assertions.assertEquals("Description task1", taskTest.getDescription());
    }

    @Test
    public void testConstructorWithAllField () {
        final Task taskTest = new Task(
                1,
                "Task with all field",
                "Test Description",
                Status.IN_PROGRESS
        );

        Assertions.assertEquals(1, taskTest.getId());
        Assertions.assertEquals("Task with all field", taskTest.getName());
        Assertions.assertEquals("Test Description", taskTest.getDescription());
        Assertions.assertEquals(Status.IN_PROGRESS, taskTest.getStatus());
    }

    @Test
    public void testSetAndGetFunctionFromTask () {
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
    public void testEqualsFromTask () {
        final Task taskTest = new Task(
                1,
                "Задание было сложным",
                "Но мы с лопатой",
                Status.NEW
        );

        Assertions.assertEquals(taskTest, task);
    }

    @Test
    public void testMethodToStringToTask () {
        final String taskToString = "Task{id=1, name='20', status='NEW', description='15'}";
        Assertions.assertEquals(taskToString, task.toString());
    }

    @Test
    public void itsTask () {
        Assertions.assertEquals(TypeTask.TASK, task.getTypeTask());
    }
}