import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ManagersTest {

    @Test
    public void testGetDefaultReturnsInitializedTaskManager() {
        TaskManager manager = Managers.getDefault();
        
        Assertions.assertNotNull(manager);
        Assertions.assertTrue(manager instanceof InMemoryTaskManager);
    }
    
    @Test
    public void testGetDefaultHistoryReturnsInitializedHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        
        Assertions.assertNotNull(historyManager);
        Assertions.assertTrue(historyManager instanceof InMemoryHistoryManager);
    }
    
    @Test
    public void testManagerIsReadyToWork() {
        TaskManager manager = Managers.getDefault();
        
        Task task = manager.createTask(new Task("Тестовая задача", "Описание"));
        
        Assertions.assertNotNull(task);
        Assertions.assertEquals(1, task.getId());
        Assertions.assertEquals(1, manager.getTasks().size());
    }
}
