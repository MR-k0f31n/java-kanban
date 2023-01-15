package ru.yandex.kanban.manager.implemented;

import ru.yandex.kanban.manager.Managers;
import ru.yandex.kanban.manager.interfaces.TaskManager;

public class FileBackedTasksManagerTest extends TestManagers{

    @Override
    public TaskManager createManager() {
        return Managers.getDefault();
    }
}
