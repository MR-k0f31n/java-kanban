package ru.yandex.kanban.manager.implemented;

public class InMemoryTaskManagerTest extends TestManagersTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }
}
