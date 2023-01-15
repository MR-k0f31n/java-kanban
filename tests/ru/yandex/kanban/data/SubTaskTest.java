package ru.yandex.kanban.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest extends TaskTest {

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

    @Override
    public void itsTask () {
        Assertions.assertEquals(TypeTask.SUB_TASK, subTask.getTypeTask());
    }

    @Override
    public void testSetAndGetFunctionFromTask () {
        subTask.setId(5);
        subTask.setName("Create task1");
        subTask.setDescription("Description task1");
        subTask.setStatus(Status.DONE);

        Assertions.assertEquals(5, subTask.getId());
        Assertions.assertEquals("Create task1", subTask.getName());
        Assertions.assertEquals("Description task1", subTask.getDescription());
        Assertions.assertEquals(Status.DONE, subTask.getStatus());
    }

    @Override
    public void testMethodToStringToTask () {
        final String taskToString = "SubTask{id=1, epicID=3, name='20', status='NEW', description='15', }";
        Assertions.assertEquals(taskToString, subTask.toString());
    }

    @Override
    public void testEqualsFromTask () {
        final SubTask taskTest = new SubTask(
                1,
                "Задание было сложным",
                "Но мы с лопатой",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                Status.NEW,
                15,
                3
        );

        Assertions.assertEquals(taskTest, subTask);
    }
}