public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        System.out.println("=== Создание задач ===");
        
        // Создаем две обычные задачи
        Task task1 = manager.createTask(new Task("Купить продукты", "Молоко, хлеб, яйца"));
        Task task2 = manager.createTask(new Task("Убрать дом", "Пропылесосить, помыть полы"));
        
        System.out.println("Создана задача: " + task1);
        System.out.println("Создана задача: " + task2);

        // Создаем эпик с двумя подзадачами
        Epic epic1 = manager.createEpic(new Epic("Переезд", "Организовать переезд в новую квартиру"));
        System.out.println("Создан эпик: " + epic1);

        Subtask subtask1 = manager.createSubtask(new Subtask("Собрать вещи", "Упаковать все в коробки", epic1.getId()));
        Subtask subtask2 = manager.createSubtask(new Subtask("Заказать грузчиков", "Найти и заказать услуги грузчиков", epic1.getId()));
        
        System.out.println("Создана подзадача: " + subtask1);
        System.out.println("Создана подзадача: " + subtask2);

        // Создаем эпик с одной подзадачей
        Epic epic2 = manager.createEpic(new Epic("Отпуск", "Подготовка к отпуску"));
        System.out.println("Создан эпик: " + epic2);

        Subtask subtask3 = manager.createSubtask(new Subtask("Забронировать отель", "Найти и забронировать жилье", epic2.getId()));
        System.out.println("Создана подзадача: " + subtask3);

        System.out.println("\n=== Списки задач после создания ===");
        System.out.println("Задачи: " + manager.getAllTasks());
        System.out.println("Эпики: " + manager.getAllEpics());
        System.out.println("Подзадачи: " + manager.getAllSubtasks());

        System.out.println("\n=== Изменение статусов ===");
        
        // Изменяем статус обычной задачи
        task1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(task1);
        System.out.println("Обновлена задача: " + manager.getTaskById(task1.getId()));

        // Изменяем статус одной подзадачи первого эпика
        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);
        System.out.println("Обновлена подзадача: " + manager.getSubtaskById(subtask1.getId()));
        System.out.println("Статус эпика после обновления подзадачи: " + manager.getEpicById(epic1.getId()));

        // Изменяем статус второй подзадачи первого эпика
        subtask2.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask2);
        System.out.println("Обновлена подзадача: " + manager.getSubtaskById(subtask2.getId()));
        System.out.println("Статус эпика после завершения всех подзадач: " + manager.getEpicById(epic1.getId()));

        // Изменяем статус подзадачи второго эпика
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask3);
        System.out.println("Обновлена подзадача: " + manager.getSubtaskById(subtask3.getId()));
        System.out.println("Статус эпика с подзадачей в процессе: " + manager.getEpicById(epic2.getId()));

        System.out.println("\n=== Получение подзадач эпика ===");
        System.out.println("Подзадачи эпика 'Переезд': " + manager.getEpicSubtasks(epic1.getId()));
        System.out.println("Подзадачи эпика 'Отпуск': " + manager.getEpicSubtasks(epic2.getId()));

        System.out.println("\n=== Удаление задач ===");
        
        // Удаляем обычную задачу
        System.out.println("Удаляем задачу с ID " + task2.getId());
        manager.deleteTaskById(task2.getId());
        System.out.println("Задачи после удаления: " + manager.getAllTasks());

        // Удаляем эпик (должны удалиться и его подзадачи)
        System.out.println("Удаляем эпик с ID " + epic2.getId());
        manager.deleteEpicById(epic2.getId());
        System.out.println("Эпики после удаления: " + manager.getAllEpics());
        System.out.println("Подзадачи после удаления эпика: " + manager.getAllSubtasks());

        System.out.println("\n=== Финальное состояние ===");
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());
        System.out.println("Все подзадачи: " + manager.getAllSubtasks());
    }
}
