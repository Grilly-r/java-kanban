public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        System.out.println("=== Демонстрация нового HistoryManager ===");
        
        System.out.println("\n1. Создаем две задачи, эпик с тремя подзадачами и эпик без подзадач");
        
        Task task1 = manager.createTask(new Task("Задача 1", "Описание задачи 1"));
        Task task2 = manager.createTask(new Task("Задача 2", "Описание задачи 2"));
        
        System.out.println("Создана задача: " + task1);
        System.out.println("Создана задача: " + task2);

        Epic epicWithSubtasks = manager.createEpic(new Epic("Эпик с подзадачами", "Эпик, который содержит подзадачи"));
        System.out.println("Создан эпик: " + epicWithSubtasks);

        Subtask subtask1 = manager.createSubtask(new Subtask("Подзадача 1", "Описание подзадачи 1", epicWithSubtasks.getId()));
        Subtask subtask2 = manager.createSubtask(new Subtask("Подзадача 2", "Описание подзадачи 2", epicWithSubtasks.getId()));
        Subtask subtask3 = manager.createSubtask(new Subtask("Подзадача 3", "Описание подзадачи 3", epicWithSubtasks.getId()));
        
        System.out.println("Создана подзадача: " + subtask1);
        System.out.println("Создана подзадача: " + subtask2);
        System.out.println("Создана подзадача: " + subtask3);

        Epic epicWithoutSubtasks = manager.createEpic(new Epic("Эпик без подзадач", "Эпик без подзадач"));
        System.out.println("Создан эпик: " + epicWithoutSubtasks);

        System.out.println("\n2. Запрашиваем созданные задачи несколько раз в разном порядке");
        
        manager.getTask(task1.getId());
        printHistory("1. После просмотра task1", manager);
        
        manager.getTask(task2.getId());
        printHistory("2. После просмотра task2", manager);
        
        manager.getEpic(epicWithSubtasks.getId());
        printHistory("3. После просмотра epicWithSubtasks", manager);
        
        manager.getSubtask(subtask1.getId());
        printHistory("4. После просмотра subtask1", manager);
        
        manager.getSubtask(subtask2.getId());
        printHistory("5. После просмотра subtask2", manager);
        
        manager.getSubtask(subtask3.getId());
        printHistory("6. После просмотра subtask3", manager);
        
        manager.getEpic(epicWithoutSubtasks.getId());
        printHistory("7. После просмотра epicWithoutSubtasks", manager);

        System.out.println("\n3. Повторно просматриваем некоторые задачи - проверяем отсутствие повторов");
        
        manager.getTask(task1.getId());
        printHistory("После повторного просмотра task1", manager);
        
        manager.getSubtask(subtask2.getId());
        printHistory("После повторного просмотра subtask2", manager);
        
        manager.getEpic(epicWithSubtasks.getId());
        printHistory("После повторного просмотра epicWithSubtasks", manager);

        System.out.println("\n4. Удаляем задачу, которая есть в истории");
        
        manager.deleteTask(task2.getId());
        printHistory("После удаления task2", manager);

        System.out.println("\n5. Удаляем эпик с тремя подзадачами - проверяем, что удаляется всё");
        
        manager.deleteEpic(epicWithSubtasks.getId());
        printHistory("После удаления epicWithSubtasks (и всех его подзадач)", manager);

        System.out.println("\n=== Дополнительная проверка: неограниченная история ===");
        
        for (int i = 1; i <= 20; i++) {
            Task tempTask = manager.createTask(new Task("Темпорарная задача " + i, "Описание " + i));
            manager.getTask(tempTask.getId());
        }
        
        printHistory("После создания и просмотра 20 дополнительных задач", manager);
        
        System.out.println("\nДемонстрация завершена!");
    }
    
    private static void printHistory(String description, TaskManager manager) {
        System.out.println(description + ":");
        
        java.util.List<Task> history = manager.getHistory();
        if (history.isEmpty()) {
            System.out.println("История пуста");
        } else {
            System.out.println("Размер истории: " + history.size());
            for (int i = 0; i < history.size(); i++) {
                Task task = history.get(i);
                String taskType = task instanceof Epic ? "Эпик" : 
                                 task instanceof Subtask ? "Подзадача" : "Задача";
                System.out.println("  " + (i + 1) + ". " + taskType + ": ID=" + task.getId() + ", название='" + task.getName() + "'");
            }
        }
        System.out.println();
    }
}
