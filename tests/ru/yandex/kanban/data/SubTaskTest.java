package ru.yandex.kanban.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.kanban.data.enums.Status;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest extends TaskTest {

    private SubTask subTask;

    @BeforeEach
    public void create () {
        subTask = new SubTask(
                1,
                "Первый саб",
                "Описание первого сабТаска",
                Status.NEW,
                3
        );
    }

    @Test
    public void testReturnIdEpic () {
        Assertions.assertEquals(3, subTask.getEpicID());
    }
}