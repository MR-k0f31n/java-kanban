package ru.yandex.kanban.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.kanban.data.enums.Status;
import ru.yandex.kanban.data.enums.TypeTask;

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
                Status.NEW
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
}