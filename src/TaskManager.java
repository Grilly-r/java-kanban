import java.util.List;

public interface TaskManager {
    List<Task> getTasks();
    void deleteAllTasks();
    Task getTask(int id);
    Task createTask(Task task);
    Task updateTask(Task task);
    void deleteTask(int id);

    List<Epic> getEpics();
    void deleteAllEpics();
    Epic getEpic(int id);
    Epic createEpic(Epic epic);
    Epic updateEpic(Epic epic);
    void deleteEpic(int id);

    List<Subtask> getSubtasks();
    void deleteAllSubtasks();
    Subtask getSubtask(int id);
    Subtask createSubtask(Subtask subtask);
    Subtask updateSubtask(Subtask subtask);
    void deleteSubtask(int id);

    List<Subtask> getEpicSubtasks(int epicId);
    
    List<Task> getHistory();
}
