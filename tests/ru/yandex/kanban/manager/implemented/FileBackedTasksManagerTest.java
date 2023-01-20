package ru.yandex.kanban.manager.implemented;

import ru.yandex.kanban.manager.Managers;

public class FileBackedTasksManagerTest extends TestManagers<FileBackedTasksManager> {

    @Override
    FileBackedTasksManager createManager() {
        return (FileBackedTasksManager) Managers.getDefault();
    }
}
