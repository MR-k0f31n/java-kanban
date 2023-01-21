package ru.yandex.kanban.manager.implemented;

import ru.yandex.kanban.manager.Managers;
import ru.yandex.kanban.manager.interfaces.TaskManager;

public class InMemoryTaskManagerTest extends TestManagers<TaskManager> {

    @Override
    TaskManager createManager() {
        return Managers.getInMemory();
    }
}
