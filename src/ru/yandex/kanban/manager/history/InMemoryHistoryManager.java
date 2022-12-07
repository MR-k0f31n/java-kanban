package ru.yandex.kanban.manager.history;

import ru.yandex.kanban.data.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> idMap = new HashMap<>();
    private Node<Task> tail;
    private Node<Task> head;
    private int size = 0;

    public int size() {
        return this.size;
    }

    @Override
    public final void add(Task task) {
        int id = task.getId();
        if (size() > 10) {
            remove(id);
        }
        if (idMap.containsKey(id)) {
            remove(id);
        }
        idMap.put(id, linkLast(task));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (idMap.containsKey(id)) {
            removeNode(id);
        }
    }

    private List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            list.add(node.task);
            node = node.nextTask;
        }
        return list;
    }

    private Node linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.nextTask = newNode;
        }
        size++;
        return newNode;
    }

    private void removeNode(int id) {
        Node node = idMap.get(id);
        if (node != null) {
            final Node nextTask = node.nextTask;
            final Node prevTask = node.prevTask;
            if (prevTask == null) {
                head = node.nextTask;
            } else {
                prevTask.nextTask = nextTask;
            }
            if (nextTask == null) {
                tail = node.prevTask;
            } else {
                nextTask.prevTask = prevTask;
                node.nextTask = null;
            }
            idMap.remove(id);
            size--;
        }
    }

    private static class Node<Task> {
        private final Task task;
        private Node<Task> nextTask;
        private Node<Task> prevTask;

        private Node(Node<Task> prevTask, Task task, Node<Task> nextTask) {
            this.task = task;
            this.nextTask = nextTask;
            this.prevTask = prevTask;
        }
    }
}
