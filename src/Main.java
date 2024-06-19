public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task wipeDust = new Task("Протереть пыль", "С новой, чистой тряпкой");
        Task wipeDustCreated = taskManager.addTask(wipeDust);
        System.out.println(wipeDustCreated);

        Task wipeDustUpdate = new Task(wipeDust.getId(), "Не забыть протереть пыль",
                "Можно использовать старую тряпку, но лучше новую", Status.IN_PROGRESS);
        Task wipeDustUpdated = taskManager.updateTask(wipeDustUpdate);
        System.out.println(wipeDustUpdated);

        Epic changeWallpaper = new Epic("Поменять обои", "Нужно успеть до Нового года");
        taskManager.addEpic(changeWallpaper);
        System.out.println(changeWallpaper);

        Subtask changeWallpaperSubtaskFirst = new Subtask("Выбрать новые обои", "Минималистичные",
                changeWallpaper.getId());
        Subtask changeWallpaperSubtaskSecond = new Subtask("Поклеить новые обои", "Старые - выкинуть",
                changeWallpaper.getId());
        taskManager.addSubtask(changeWallpaperSubtaskFirst);
        taskManager.addSubtask(changeWallpaperSubtaskSecond);
        System.out.println(changeWallpaper);

        changeWallpaperSubtaskFirst.setStatus(Status.DONE);
        taskManager.updateSubtask(changeWallpaperSubtaskFirst);
        System.out.println(changeWallpaper);

    }
}
