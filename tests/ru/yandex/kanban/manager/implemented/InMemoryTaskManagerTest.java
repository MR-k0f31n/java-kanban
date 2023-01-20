package ru.yandex.kanban.manager.implemented;

import ru.yandex.kanban.manager.Managers;

public class InMemoryTaskManagerTest extends TestManagers<InMemoryTaskManager> {

    @Override
    InMemoryTaskManager createManager() {
        return (InMemoryTaskManager) Managers.getInMemory();
    }
}
