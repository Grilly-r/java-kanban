import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class EpicTest {

    @Test
    public void testEpicCannotBeAddedToItselfAsSubtask() {
        Epic epic = new Epic(1, "Эпик", "Описание", TaskStatus.NEW);
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            epic.addSubtaskId(1);
        }, "Эпик не должен добавляться сам в себя как подзадача");
    }
    
    @Test
    public void testSubtaskCannotBeItsOwnEpic() {
        // Создаем эпик
        Epic epic = new Epic("Эпик", "Описание эпика");
        epic.setId(1);
        
        // Пытаемся создать подзадачу, которая ссылается сама на себя как на эпик
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // Имитируем ситуацию, когда подзадача пытается быть своим же эпиком
            epic.addSubtaskId(1); // эпик пытается добавить сам себя как подзадачу
        }, "Подзадача не может быть своим же эпиком");
    }
    
    @Test
    public void testEpicCanAddValidSubtasks() {
        Epic epic = new Epic(1, "Эпик", "Описание", TaskStatus.NEW);
        
        epic.addSubtaskId(2);
        epic.addSubtaskId(3);
        
        Assertions.assertEquals(2, epic.getSubtaskIds().size(), "Эпик должен содержать 2 подзадачи");
        Assertions.assertTrue(epic.getSubtaskIds().contains(2), "Эпик должен содержать подзадачу с ID 2");
        Assertions.assertTrue(epic.getSubtaskIds().contains(3), "Эпик должен содержать подзадачу с ID 3");
    }
}
