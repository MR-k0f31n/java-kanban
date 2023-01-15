package ru.yandex.kanban.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest extends TaskTest{

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

    @Override
    public void testMethodToStringToTask () {
        final String taskToString = "EpicTask{subTaskIDs=[] ,id=1, name='20', status='NEW', description='15'}";
        Assertions.assertEquals(taskToString, epicTask.toString());
    }

    @Override
    public void testSetAndGetFunctionFromTask () {
        epicTask.setId(5);
        epicTask.setName("Create task1");
        epicTask.setDescription("Description task1");

        Assertions.assertEquals(5, epicTask.getId());
        Assertions.assertEquals("Create task1", epicTask.getName());
        Assertions.assertEquals("Description task1", epicTask.getDescription());
    }

    @Override
    public void testEqualsFromTask () {
        final EpicTask taskTest = new EpicTask(
                1,
                "Задание было сложным",
                "Но мы с лопатой",
                LocalDateTime.of(2022, 12, 21, 10, 25),
                Status.NEW,
                15
        );

        Assertions.assertEquals(taskTest, epicTask);
    }

    @Override
    public void itsTask () {
        Assertions.assertEquals(TypeTask.EPIC_TASK, epicTask.getTypeTask());
    }
}