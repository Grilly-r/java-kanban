import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node first;
    private Node last;
    private Map<Integer, Node> nodeMap = new HashMap<>();

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        Task taskCopy = copyTask(task);
        remove(task.getId());
        linkLast(taskCopy);
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.get(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (last == null) {
            first = newNode;
            last = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        }
        nodeMap.put(task.getId(), newNode);
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        nodeMap.remove(node.task.getId());

        if (node.prev == null) {
            first = node.next;
        } else {
            node.prev.next = node.next;
        }

        if (node.next == null) {
            last = node.prev;
        } else {
            node.next.prev = node.prev;
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = first;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    private Task copyTask(Task task) {
        if (task instanceof Epic) {
            Epic epic = (Epic) task;
            Epic epicCopy = new Epic(epic.getId(), epic.getName(), epic.getDescription(), epic.getStatus());
            for (Integer subtaskId : epic.getSubtaskIds()) {
                epicCopy.addSubtaskId(subtaskId);
            }
            return epicCopy;
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return new Subtask(subtask.getId(), subtask.getName(), subtask.getDescription(), 
                             subtask.getStatus(), subtask.getEpicId());
        } else {
            return new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus());
        }
    }
}
