package ru.yandex.kanban.manager.implemented;

import org.junit.jupiter.api.Test;
import ru.yandex.kanban.manager.Managers;
import ru.yandex.kanban.manager.interfaces.TaskManager;

import java.io.File;

public class FileBackedTasksManagerTest extends TestManagers<TaskManager> {

    @Override
    TaskManager createManager() {
        return Managers.getDefault();
    }
    @Test
    public void testLoadFromFile_expectedCorrectLoad () {
        TaskManager fileBackedTasksManager = Managers.getDefault();

        System.out.println(fileBackedTasksManager.getAllListTask().size());
        System.out.println(fileBackedTasksManager.getAllSubTask().size());
    }
}
