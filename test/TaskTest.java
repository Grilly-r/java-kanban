import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TaskTest {

    @Test
    public void testTasksWithSameIdAreEqual() {
        Task task1 = new Task(1, "Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task(1, "Задача 2", "Описание 2", TaskStatus.DONE);
        
        Assertions.assertEquals(task1, task2, "Задачи с одинаковым ID должны быть равны");
    }
    
    @Test
    public void testTasksWithDifferentIdAreNotEqual() {
        Task task1 = new Task(1, "Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Задача 1", "Описание 1", TaskStatus.NEW);
        
        Assertions.assertNotEquals(task1, task2, "Задачи с разными ID не должны быть равны");
    }
    
    @Test
    public void testTasksInheritorsWithSameIdAreEqual() {
        Task task = new Task(1, "Задача", "Описание", TaskStatus.NEW);
        Epic epic = new Epic(1, "Эпик", "Описание", TaskStatus.NEW);
        
        Assertions.assertEquals(task, epic, "Наследники Task с одинаковым ID должны быть равны");
    }
    
    @Test
    public void testTasksInheritorsWithSameIdAreEqual2() {
        Task task = new Task(1, "Задача", "Описание", TaskStatus.NEW);
        Subtask subtask = new Subtask(1, "Подзадача", "Описание", TaskStatus.NEW, 2);
        
        Assertions.assertEquals(task, subtask, "Наследники Task с одинаковым ID должны быть равны");
    }
    
    @Test
    public void testEpicAndSubtaskWithSameIdAreEqual() {
        Epic epic = new Epic(1, "Эпик", "Описание", TaskStatus.NEW);
        Subtask subtask = new Subtask(1, "Подзадача", "Описание", TaskStatus.NEW, 2);
        
        Assertions.assertEquals(epic, subtask, "Epic и Subtask с одинаковым ID должны быть равны");
    }
}
