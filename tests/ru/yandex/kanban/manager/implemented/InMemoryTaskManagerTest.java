package ru.yandex.kanban.manager.implemented;

public class InMemoryTaskManagerTest extends TestManagersTest<InMemoryTaskManager> {

    @Override
    InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }
}
