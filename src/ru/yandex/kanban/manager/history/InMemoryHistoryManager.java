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
        return size;
    }

    @Override
    public final void addHistory(Task task) {
        if (size() > 10) {
            removeNode(idMap.get(0));
        }
        addInLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getHistoryCustom();
    }

    @Override
    public void removeHistory(int id) {
        if (idMap.containsKey(id)) {
            Node node = idMap.get(id);
            removeNode(node);
        }
    }

    private List<Task> getHistoryCustom() {
        List<Task> showHistory = new ArrayList<>();
        Node node = head;

        while (node != null) {
            showHistory.add((Task) node.task);
            node = node.nextTask;
        }

        return showHistory;
    }

    private void addInLast(Task task) {
        if (idMap.containsKey(task.getId())) {
            removeNode(idMap.get(task.getId()));
        }

        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.nextTask = newNode;
        }
        idMap.put(task.getId(), newNode);
        size++;
    }

    private void removeNode(Node<Task> node) {
            final Node<Task> nextTask = node.nextTask;
            final Node<Task> prevTask = node.prevTask;
            if (prevTask == null) {
                head = nextTask;
            } else {
                prevTask.nextTask = nextTask;
            }

            if (nextTask == null) {
                tail = prevTask;
            } else {
                nextTask.prevTask = prevTask;
            }
            size--;
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
