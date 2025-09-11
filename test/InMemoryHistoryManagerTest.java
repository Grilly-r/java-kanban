import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import java.util.List;

public class InMemoryHistoryManagerTest {
    
    private HistoryManager historyManager;
    
    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }
    
    @Test
    public void testAddTaskToHistory() {
        Task task = new Task(1, "Тестовая задача", "Описание", TaskStatus.NEW);
        historyManager.add(task);
        
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(1, history.size(), "История должна содержать одну задачу");
        Assertions.assertEquals(task.getId(), history.get(0).getId(), "Задача в истории должна иметь тот же ID");
    }
    
    @Test
    public void testHistoryIsUnlimited() {
        for (int i = 1; i <= 100; i++) {
            Task task = new Task(i, "Задача " + i, "Описание " + i, TaskStatus.NEW);
            historyManager.add(task);
        }
        
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(100, history.size(), "История должна содержать все добавленные задачи");
        
        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i + 1, history.get(i).getId(), "Порядок в истории должен соответствовать порядку добавления");
        }
    }
    
    @Test
    public void testRemoveDuplicateViews() {
        Task task1 = new Task(1, "Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Задача 2", "Описание 2", TaskStatus.NEW);
        Task task3 = new Task(3, "Задача 3", "Описание 3", TaskStatus.NEW);
        
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1);
        
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(3, history.size(), "В истории должно быть 3 уникальные задачи");
        
        Assertions.assertEquals(2, history.get(0).getId(), "Первой должна быть задача 2");
        Assertions.assertEquals(3, history.get(1).getId(), "Второй должна быть задача 3");
        Assertions.assertEquals(1, history.get(2).getId(), "Последней должна быть задача 1");
    }
    
    @Test
    public void testRemoveTaskFromHistory() {
        Task task1 = new Task(1, "Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Задача 2", "Описание 2", TaskStatus.NEW);
        Task task3 = new Task(3, "Задача 3", "Описание 3", TaskStatus.NEW);
        
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        
        historyManager.remove(2);
        
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(2, history.size(), "После удаления должно остаться 2 задачи");
        
        Assertions.assertEquals(1, history.get(0).getId(), "Первой должна быть задача 1");
        Assertions.assertEquals(3, history.get(1).getId(), "Второй должна быть задача 3");
    }
    
    @Test
    public void testRemoveNonexistentTask() {
        Task task1 = new Task(1, "Задача 1", "Описание 1", TaskStatus.NEW);
        historyManager.add(task1);
        
        historyManager.remove(999);
        
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(1, history.size(), "Удаление несуществующей задачи не должно влиять на историю");
        Assertions.assertEquals(1, history.get(0).getId(), "Задача 1 должна остаться в истории");
    }
    
    @Test
    public void testRemoveFromEmptyHistory() {
        historyManager.remove(1);
        
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(0, history.size(), "Удаление из пустой истории не должно вызывать ошибок");
    }
    
    @Test
    public void testRemoveFirstTask() {
        Task task1 = new Task(1, "Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Задача 2", "Описание 2", TaskStatus.NEW);
        Task task3 = new Task(3, "Задача 3", "Описание 3", TaskStatus.NEW);
        
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        
        historyManager.remove(1);
        
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(2, history.size(), "После удаления первой задачи должно остаться 2 задачи");
        Assertions.assertEquals(2, history.get(0).getId(), "Первой должна быть задача 2");
        Assertions.assertEquals(3, history.get(1).getId(), "Второй должна быть задача 3");
    }
    
    @Test
    public void testRemoveLastTask() {
        Task task1 = new Task(1, "Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Задача 2", "Описание 2", TaskStatus.NEW);
        Task task3 = new Task(3, "Задача 3", "Описание 3", TaskStatus.NEW);
        
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        
        historyManager.remove(3);
        
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(2, history.size(), "После удаления последней задачи должно остаться 2 задачи");
        Assertions.assertEquals(1, history.get(0).getId(), "Первой должна быть задача 1");
        Assertions.assertEquals(2, history.get(1).getId(), "Второй должна быть задача 2");
    }
    
    @Test
    public void testRemoveOnlyTask() {
        Task task1 = new Task(1, "Задача 1", "Описание 1", TaskStatus.NEW);
        historyManager.add(task1);
        
        historyManager.remove(1);
        
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(0, history.size(), "После удаления единственной задачи история должна быть пустой");
    }
    
    @Test
    public void testHistoryPreservesTaskData() {
        Task originalTask = new Task(1, "Исходная задача", "Исходное описание", TaskStatus.NEW);
        historyManager.add(originalTask);
        
        originalTask.setName("Измененное название");
        originalTask.setDescription("Измененное описание");
        originalTask.setStatus(TaskStatus.DONE);
        
        List<Task> history = historyManager.getHistory();
        Task taskFromHistory = history.get(0);
        
        Assertions.assertEquals("Исходная задача", taskFromHistory.getName(), "Название в истории должно остаться неизменным");
        Assertions.assertEquals("Исходное описание", taskFromHistory.getDescription(), "Описание в истории должно остаться неизменным");
        Assertions.assertEquals(TaskStatus.NEW, taskFromHistory.getStatus(), "Статус в истории должен остаться неизменным");
    }
    
    @Test
    public void testAddNullTaskDoesNothing() {
        historyManager.add(null);
        
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(0, history.size(), "Добавление null не должно влиять на историю");
    }
    
    @Test
    public void testHistoryWithDifferentTaskTypes() {
        Task task = new Task(1, "Задача", "Описание", TaskStatus.NEW);
        Epic epic = new Epic(2, "Эпик", "Описание эпика", TaskStatus.NEW);
        Subtask subtask = new Subtask(3, "Подзадача", "Описание подзадачи", TaskStatus.NEW, 2);
        
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(3, history.size(), "История должна содержать 3 элемента");
        
        Assertions.assertTrue(history.get(0) instanceof Task, "Первый элемент должен быть Task");
        Assertions.assertTrue(history.get(1) instanceof Epic, "Второй элемент должен быть Epic");
        Assertions.assertTrue(history.get(2) instanceof Subtask, "Третий элемент должен быть Subtask");
    }
}
