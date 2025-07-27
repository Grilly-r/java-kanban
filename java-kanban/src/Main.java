/**
 * Главный класс для тестирования трекера задач
 */
public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        
        System.out.println("=== Создание задач ===");
        
        // Создаем две обычные задачи
        Task task1 = new Task("Сделать покупки", "Купить продукты в магазине");
        Task task2 = new Task("Выучить Java", "Изучить основы программирования на Java");
        
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        
        // Создаем эпик с двумя подзадачами
        Epic epic1 = new Epic("Организовать семейный праздник", "Подготовить празднование дня рождения");
        taskManager.createEpic(epic1);
        
        Subtask subtask1 = new Subtask("Купить подарки", "Выбрать и купить подарки для именинника", epic1.getId());
        Subtask subtask2 = new Subtask("Заказать торт", "Заказать торт в кондитерской", epic1.getId());
        
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        
        // Создаем эпик с одной подзадачей
        Epic epic2 = new Epic("Купить квартиру", "Найти и приобрести новое жилье");
        taskManager.createEpic(epic2);
        
        Subtask subtask3 = new Subtask("Найти квартиру", "Поиск подходящих вариантов квартир", epic2.getId());
        taskManager.createSubtask(subtask3);
        
        System.out.println("=== Печать списков задач ===");
        
        System.out.println("Все задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        
        System.out.println("\nВсе эпики:");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
        
        System.out.println("\nВсе подзадачи:");
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }
        
        System.out.println("\n=== Изменение статусов ===");
        
        // Изменяем статусы задач
        task1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1);
        
        task2.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task2);
        
        // Изменяем статусы подзадач
        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask2);
        
        subtask3.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask3);
        
        System.out.println("После изменения статусов:");
        
        System.out.println("\nВсе задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        
        System.out.println("\nВсе эпики:");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
        
        System.out.println("\nВсе подзадачи:");
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }
        
        System.out.println("\n=== Проверка статусов эпиков ===");
        System.out.println("Эпик 1 (одна подзадача DONE, одна IN_PROGRESS) должен быть IN_PROGRESS: " + epic1.getStatus());
        System.out.println("Эпик 2 (одна подзадача DONE) должен быть DONE: " + epic2.getStatus());
        
        System.out.println("\n=== Получение подзадач эпика ===");
        System.out.println("Подзадачи эпика 1:");
        for (Subtask subtask : taskManager.getEpicSubtasks(epic1.getId())) {
            System.out.println(subtask);
        }
        
        System.out.println("\n=== Удаление задач ===");
        
        // Удаляем одну из задач
        taskManager.deleteTaskById(task1.getId());
        System.out.println("Удалили задачу с ID " + task1.getId());
        
        // Удаляем один из эпиков
        taskManager.deleteEpicById(epic2.getId());
        System.out.println("Удалили эпик с ID " + epic2.getId());
        
        System.out.println("\nОставшиеся задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        
        System.out.println("\nОставшиеся эпики:");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
        
        System.out.println("\nОставшиеся подзадачи:");
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }
        
        System.out.println("\n=== Тестирование завершено ===");
    }
}
