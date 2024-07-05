package test;

import controllers.Managers;
import controllers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Status;
import model.Epic;
import model.Subtask;
import model.Task;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void getHistoryShouldReturnListOf10Tasks() {
        for (int i = 0; i < 20; i++) {
            taskManager.addTask(new Task("Name", "Description"));
        }

        List<Task> tasks = taskManager.getTasks();
        for (Task task : tasks) {
            taskManager.getTaskByID(task.getId());
        }

        List<Task> list = taskManager.getHistory();
        assertEquals(10, list.size(), "Неверное количество элементов");
    }

    @Test
    public void getHistoryShouldReturnOldTaskAfterUpdate() {
        Task wipeDust = new Task("Протереть пыль", "С новой, чистой тряпкой");
        taskManager.addTask(wipeDust);
        taskManager.getTaskByID(wipeDust.getId());
        taskManager.updateTask(new Task(wipeDust.getId(), "Не забыть протереть пыль",
                "Можно использовать старую тряпку, но лучше новую", Status.IN_PROGRESS));

        List<Task> tasks = taskManager.getHistory();
        Task oldTask = tasks.getFirst();
        assertEquals(wipeDust.getName(), oldTask.getName(), "В истории не сохранилась старая версия задачи");
        assertEquals(wipeDust.getDescription(), oldTask.getDescription(),
                "В истории не сохранилась старая версия задачи");
    }

    @Test
    public void getHistoryShouldReturnOldEpicAfterUpdate() {
        Epic changeWallpaper = new Epic("Поменять обои", "Нужно успеть до Нового года");
        taskManager.addEpic(changeWallpaper);
        taskManager.getEpicByID(changeWallpaper.getId());
        taskManager.updateEpic(new Epic(changeWallpaper.getId(), "Имя", "Описание", Status.IN_PROGRESS));

        List<Task> epics = taskManager.getHistory();
        Epic oldEpic = (Epic) epics.getFirst();
        assertEquals(changeWallpaper.getName(), oldEpic.getName(),
                "В истории не сохранилась старая версия эпика");
        assertEquals(changeWallpaper.getDescription(), oldEpic.getDescription(),
                "В истории не сохранилась старая версия эпика");
    }

    @Test
    public void getHistoryShouldReturnOldSubtaskAfterUpdate() {
        Epic changeWallpaper = new Epic("Поменять обои", "Нужно успеть до Нового года");
        taskManager.addEpic(changeWallpaper);
        Subtask changeWallpaperSubtaskFirst = new Subtask("Выбрать новые обои", "Минималистичные",
                changeWallpaper.getId());
        taskManager.addSubtask(changeWallpaperSubtaskFirst);
        taskManager.getSubtaskByID(changeWallpaperSubtaskFirst.getId());
        taskManager.updateSubtask(new Subtask(changeWallpaperSubtaskFirst.getId(), "Имя",
                "Описание", Status.IN_PROGRESS, changeWallpaper.getId()));

        List<Task> subtasks = taskManager.getHistory();
        Subtask oldSubtask = (Subtask) subtasks.getFirst();
        assertEquals(changeWallpaperSubtaskFirst.getName(), oldSubtask.getName(),
                "В истории не сохранилась старая версия эпика");
        assertEquals(changeWallpaperSubtaskFirst.getDescription(), oldSubtask.getDescription(),
                "В истории не сохранилась старая версия эпика");
    }
}