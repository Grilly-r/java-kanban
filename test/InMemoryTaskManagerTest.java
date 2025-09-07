import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

public class InMemoryTaskManagerTest {
    
    private TaskManager manager;
    
    @BeforeEach
    public void setUp() {
        manager = Managers.getDefault();
    }
    
    @Test
    public void testAddDifferentTypesOfTasksAndFindById() {
        Task task = manager.createTask(new Task("Обычная задача", "Описание задачи"));
        Epic epic = manager.createEpic(new Epic("Эпик", "Описание эпика"));
        Subtask subtask = manager.createSubtask(new Subtask("Подзадача", "Описание подзадачи", epic.getId()));
        
        Assertions.assertEquals(task, manager.getTask(task.getId()), "Задача должна быть найдена по ID");
        Assertions.assertEquals(epic, manager.getEpic(epic.getId()), "Эпик должен быть найден по ID");
        Assertions.assertEquals(subtask, manager.getSubtask(subtask.getId()), "Подзадача должна быть найдена по ID");
    }
    
    @Test
    public void testTasksWithSetIdAndGeneratedIdDoNotConflict() {
        Task generatedTask = manager.createTask(new Task("Сгенерированная задача", "Описание"));
        
        Task setTask = new Task(999, "Задача с заданным ID", "Описание", TaskStatus.NEW);
        manager.createTask(setTask);
        
        Task anotherGeneratedTask = manager.createTask(new Task("Еще одна сгенерированная", "Описание"));
        
        Assertions.assertNotEquals(generatedTask.getId(), setTask.getId(), "ID не должны совпадать");
        Assertions.assertNotEquals(generatedTask.getId(), anotherGeneratedTask.getId(), "ID не должны совпадать");
        Assertions.assertNotEquals(setTask.getId(), anotherGeneratedTask.getId(), "ID не должны совпадать");
        
        Assertions.assertEquals(generatedTask, manager.getTask(generatedTask.getId()));
        Assertions.assertEquals(setTask, manager.getTask(setTask.getId()));
        Assertions.assertEquals(anotherGeneratedTask, manager.getTask(anotherGeneratedTask.getId()));
    }
    
    @Test
    public void testTaskImmutabilityWhenAddedToManager() {
        String originalName = "Исходное название";
        String originalDescription = "Исходное описание";
        TaskStatus originalStatus = TaskStatus.NEW;
        
        Task originalTask = new Task(originalName, originalDescription);
        originalTask.setStatus(originalStatus);
        
        Task addedTask = manager.createTask(originalTask);
        
        Task retrievedTask = manager.getTask(addedTask.getId());
        
        Assertions.assertEquals(originalName, retrievedTask.getName());
        Assertions.assertEquals(originalDescription, retrievedTask.getDescription());
        Assertions.assertEquals(originalStatus, retrievedTask.getStatus());
        
        originalTask.setName("Измененное название");
        originalTask.setDescription("Измененное описание");
        originalTask.setStatus(TaskStatus.DONE);
        
        Task taskAfterChange = manager.getTask(addedTask.getId());
        Assertions.assertEquals(originalName, taskAfterChange.getName());
        Assertions.assertEquals(originalDescription, taskAfterChange.getDescription());
        Assertions.assertEquals(originalStatus, taskAfterChange.getStatus());
    }
    
    @Test
    public void testTaskIsUnchangedWhenAddedToManager() {
        String originalName = "Исходное название";
        String originalDescription = "Исходное описание";
        TaskStatus originalStatus = TaskStatus.NEW;
        
        Task originalTask = new Task(originalName, originalDescription);
        originalTask.setStatus(originalStatus);
        
        Task addedTask = manager.createTask(originalTask);
        
        Task retrievedTask = manager.getTask(addedTask.getId());
        
        Assertions.assertEquals(originalName, retrievedTask.getName());
        Assertions.assertEquals(originalDescription, retrievedTask.getDescription());
        Assertions.assertEquals(originalStatus, retrievedTask.getStatus());
    }
    
    @Test
    public void testSubtaskCannotBeItsOwnEpic() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Описание"));
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Subtask subtask = new Subtask("Подзадача", "Описание", epic.getId());
            subtask.setId(epic.getId());
            subtask.setEpicId(epic.getId());
            manager.createSubtask(subtask);
        });
    }
    
    @Test
    public void testHistoryPreservesTaskState() {
        Task task = manager.createTask(new Task("Исходная задача", "Исходное описание"));
        task.setStatus(TaskStatus.NEW);
        
        manager.getTask(task.getId());
        
        task.setName("Измененное название");
        task.setDescription("Измененное описание");
        task.setStatus(TaskStatus.DONE);
        manager.updateTask(task);
        
        java.util.List<Task> history = manager.getHistory();
        Assertions.assertEquals(1, history.size());
        
        Task taskFromHistory = history.get(0);
        Assertions.assertEquals("Исходная задача", taskFromHistory.getName());
        Assertions.assertEquals("Исходное описание", taskFromHistory.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, taskFromHistory.getStatus());
    }
    
    @Test
    public void testTaskIsRemovedFromHistoryAfterDeletion() {
        Task task1 = manager.createTask(new Task("Задача 1", "Описание 1"));
        Task task2 = manager.createTask(new Task("Задача 2", "Описание 2"));
        
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        
        java.util.List<Task> historyBefore = manager.getHistory();
        Assertions.assertEquals(2, historyBefore.size(), "В истории должно быть 2 задачи");
        
        manager.deleteTask(task1.getId());
        
        java.util.List<Task> historyAfter = manager.getHistory();
        Assertions.assertEquals(1, historyAfter.size(), "После удаления в истории должна остаться 1 задача");
        Assertions.assertEquals(task2.getId(), historyAfter.get(0).getId(), "В истории должна остаться только задача 2");
    }
    
    @Test
    public void testEpicAndSubtasksRemovedFromHistoryAfterEpicDeletion() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Описание эпика"));
        Subtask subtask1 = manager.createSubtask(new Subtask("Подзадача 1", "Описание 1", epic.getId()));
        Subtask subtask2 = manager.createSubtask(new Subtask("Подзадача 2", "Описание 2", epic.getId()));
        
        manager.getEpic(epic.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        
        java.util.List<Task> historyBefore = manager.getHistory();
        Assertions.assertEquals(3, historyBefore.size(), "В истории должно быть 3 элемента");
        
        manager.deleteEpic(epic.getId());
        
        java.util.List<Task> historyAfter = manager.getHistory();
        Assertions.assertEquals(0, historyAfter.size(), "После удаления эпика история должна быть пустой");
    }
    
    @Test
    public void testSubtaskRemovedFromHistoryAfterDeletion() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Описание эпика"));
        Subtask subtask1 = manager.createSubtask(new Subtask("Подзадача 1", "Описание 1", epic.getId()));
        Subtask subtask2 = manager.createSubtask(new Subtask("Подзадача 2", "Описание 2", epic.getId()));
        
        manager.getEpic(epic.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        
        java.util.List<Task> historyBefore = manager.getHistory();
        Assertions.assertEquals(3, historyBefore.size(), "В истории должно быть 3 элемента");
        
        manager.deleteSubtask(subtask1.getId());
        
        java.util.List<Task> historyAfter = manager.getHistory();
        Assertions.assertEquals(2, historyAfter.size(), "После удаления подзадачи должно остаться 2 элемента");
        
        boolean subtask1Found = false;
        for (Task task : historyAfter) {
            if (task.getId() == subtask1.getId()) {
                subtask1Found = true;
                break;
            }
        }
        Assertions.assertFalse(subtask1Found, "Удаленная подзадача не должна оставаться в истории");
    }
    
    @Test
    public void testDuplicateViewsInHistory() {
        Task task1 = manager.createTask(new Task("Задача 1", "Описание 1"));
        Task task2 = manager.createTask(new Task("Задача 2", "Описание 2"));
        
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task1.getId());
        
        java.util.List<Task> history = manager.getHistory();
        Assertions.assertEquals(2, history.size(), "В истории должно быть 2 уникальные задачи");
        
        Assertions.assertEquals(task2.getId(), history.get(0).getId(), "Первой должна быть задача 2");
        Assertions.assertEquals(task1.getId(), history.get(1).getId(), "Последней должна быть задача 1");
    }
}
