package ru.yandex.kanban.data.exceptions;

public class ManagerSaveException extends RuntimeException{
    public ManagerSaveException(String message) {
        super(message);
    }
}