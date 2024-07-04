import controllers.InMemoryTaskManager;
import controllers.Managers;
import model.Status;
import model.Task;
import model.Epic;
import model.Subtask;

public class Main {

    private static final InMemoryTaskManager inMemoryTaskManager = Managers.getDefault();

    public static void main(String[] args) {
        addTasks();
        printAllTasks();
        printViewHistory();
    }

    private static void addTasks() {
        Task wipeDust = new Task("Протереть пыль", "С новой, чистой тряпкой");
        inMemoryTaskManager.addTask(wipeDust);

        Task wipeDustUpdate = new Task(wipeDust.getId(), "Не забыть протереть пыль",
                "Можно использовать старую тряпку, но лучше новую", Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(wipeDustUpdate);
        inMemoryTaskManager.addTask(new Task("Прибраться в детской", "Помыть пол и прибрать игрушки"));


        Epic changeWallpaper = new Epic("Поменять обои", "Нужно успеть до Нового года");
        inMemoryTaskManager.addEpic(changeWallpaper);
        Subtask changeWallpaperSubtaskFirst = new Subtask("Выбрать новые обои", "Минималистичные",
                changeWallpaper.getId());
        Subtask changeWallpaperSubtaskSecond = new Subtask("Поклеить новые обои", "Старые - выкинуть",
                changeWallpaper.getId());
        Subtask changeWallpaperSubtaskThird = new Subtask("Убедиться, что они подходят интерьеру", "Обои подходят мебели",
                changeWallpaper.getId());

        inMemoryTaskManager.addSubtask(changeWallpaperSubtaskFirst);
        inMemoryTaskManager.addSubtask(changeWallpaperSubtaskSecond);
        inMemoryTaskManager.addSubtask(changeWallpaperSubtaskThird);

        changeWallpaperSubtaskThird.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(changeWallpaperSubtaskThird);
    }

    private static void printAllTasks() {
        System.out.println("Задачи:");
        for (Task task : Main.inMemoryTaskManager.getTasks()) {
            System.out.println(task);
        }

        System.out.println("Эпики:");
        for (Epic epic : Main.inMemoryTaskManager.getEpics()) {
            System.out.println(epic);

            for (Task task : Main.inMemoryTaskManager.getEpicSubtasks(epic)) {
                System.out.println("--> " + task);
            }
        }

        System.out.println("Подзадачи:");
        for (Task subtask : Main.inMemoryTaskManager.getSubtasks()) {
            System.out.println(subtask);
        }
    }

    private static void printViewHistory() {
        Main.inMemoryTaskManager.getTaskByID(1);
        Main.inMemoryTaskManager.getTaskByID(2);
        Main.inMemoryTaskManager.getEpicByID(3);
        Main.inMemoryTaskManager.getTaskByID(1);
        Main.inMemoryTaskManager.getSubtaskByID(4);
        Main.inMemoryTaskManager.getSubtaskByID(5);
        Main.inMemoryTaskManager.getSubtaskByID(6);
        Main.inMemoryTaskManager.getEpicByID(3);
        Main.inMemoryTaskManager.getSubtaskByID(4);
        Main.inMemoryTaskManager.getTaskByID(2);
        Main.inMemoryTaskManager.getSubtaskByID(6);
        System.out.println();

        System.out.println("История просмотров:");
        for (Task task : Main.inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
