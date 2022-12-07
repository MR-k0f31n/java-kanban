package ru.yandex.kanban.manager.history;

import ru.yandex.kanban.data.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> historyList;

    public InMemoryHistoryManager() {
        this.historyList = new CustomLinkedList<>();
    }

    @Override
    public final void addHistory(Task task) {
        if (historyList.size() > 10) {
            historyList.removeNode(historyList.idMap.get(0));
        }
        historyList.addInLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getHistory();
    }

    @Override
    public void removeHistory(int id) {
        historyList.removeNode(historyList.idMap.get(id));
    }

    private static class CustomLinkedList<T extends Task> {

        private final Map<Integer, Node<Task>> idMap = new HashMap<>();
        private Node<Task> tail;
        private Node<Task> head;
        private int size = 0;

        public int size() {
            return size;
        }

        private List<Task> getHistory() {
            List<Task> showHistory = new ArrayList<>();
            for (Node tasks : idMap.values()) {
                if (tasks.nextTask != null) {
                    showHistory.add((Task) tasks.task);
                }
            }
            return showHistory;
        }

        private void addInLast (Task task) {
            if (idMap.containsKey(task.getId())) {
                removeNode(idMap.get(task.getId()));
            }

            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if(oldTail == null) {
                head = newNode;
            } else {
                oldTail.nextTask = newNode;
            }
            idMap.put(task.getId(), newNode);
            size++;
        }

        private void removeNode(Node<Task> node) {
            if (idMap.containsKey(node.task.getId()) && idMap.get(node.task.getId()) != null) {
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
}
