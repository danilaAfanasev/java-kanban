package test;

import controllers.Managers;
import controllers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Status;
import model.Task;
import model.Subtask;
import model.Epic;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addNewTask() {
        final Task task = taskManager.addTask(new Task("addNewTask", "addNewTask description"));
        final Task savedTask = taskManager.getTaskByID(task.getId());
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");

        final List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают");
    }

    @Test
    void addNewEpicAndSubtasks() {
        final Epic changeWallpaper = taskManager.addEpic(new Epic("Поменять обои",
                "Нужно успеть до Нового года"));
        final Subtask changeWallpaperSubtaskFirst = taskManager.addSubtask(new Subtask("Выбрать новые обои",
                "Минималистичные", changeWallpaper.getId()));
        final Subtask changeWallpaperSubtaskSecond = taskManager.addSubtask(new Subtask("Поклеить новые обои",
                "Старые - выкинуть", changeWallpaper.getId()));
        final Subtask changeWallpaperSubtaskThird = taskManager.addSubtask(new Subtask("Убедиться, что они подходят интерьеру",
                "Обои подходят мебели", changeWallpaper.getId()));
        final Epic savedEpic = taskManager.getEpicByID(changeWallpaper.getId());
        final Subtask savedSubtaskFirst = taskManager.getSubtaskByID(changeWallpaperSubtaskFirst.getId());
        final Subtask savedSubtaskSecond = taskManager.getSubtaskByID(changeWallpaperSubtaskSecond.getId());
        final Subtask savedSubtaskThird = taskManager.getSubtaskByID(changeWallpaperSubtaskThird.getId());
        assertNotNull(savedEpic, "Эпик не найден.");
        assertNotNull(savedSubtaskSecond, "Подзадача не найдена.");
        assertEquals(changeWallpaper, savedEpic, "Эпики не совпадают.");
        assertEquals(changeWallpaperSubtaskFirst, savedSubtaskFirst, "Подзадачи не совпадают.");
        assertEquals(changeWallpaperSubtaskThird, savedSubtaskThird, "Подзадачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(changeWallpaper, epics.getFirst(), "Эпики не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(savedSubtaskFirst, subtasks.getFirst(), "Подзадачи не совпадают.");
    }

    @Test
    public void updateTaskShouldReturnTaskWithTheSameId() {
        final Task expected = new Task("Имя", "Описание");
        taskManager.addTask(expected);
        final Task updatedTask = new Task(expected.getId(), "Имя", "Описание", Status.NEW);
        final Task actual = taskManager.updateTask(updatedTask);
        assertEquals(expected, actual, "Вернулась задача с другим id");
    }

    @Test
    public void updateEpicShouldReturnEpicWithTheSameId() {
        final Epic expected = new Epic("Имя", "Описание");
        taskManager.addEpic(expected);
        final Epic updatedEpic = new Epic(expected.getId(), "Новое имя", "Новое описание",Status.NEW);
        final Epic actual = taskManager.updateEpic(updatedEpic);
        assertEquals(expected, actual, "Вернулся эпик с другим id");
    }

    @Test
    public void updateSubtaskShouldReturnSubtaskWithTheSameId() {
        final Epic epic = new Epic("Имя", "Описание");
        taskManager.addEpic(epic);
        final Subtask expected = new Subtask("Имя", "Описание", epic.getId());
        taskManager.addSubtask(expected);
        final Subtask updatedSubtask = new Subtask(expected.getId(), "Новое имя", "Новое описание",
                Status.DONE, epic.getId());
        final Subtask actual = taskManager.updateSubtask(updatedSubtask);
        assertEquals(expected, actual, "Вернулась подзадача с другим id");
    }

    @Test
    public void deleteTasksShouldReturnEmptyList() {
        taskManager.addTask(new Task("Протереть пыль", "С новой, чистой тряпкой"));
        taskManager.addTask(new Task("Помыть полы", "С новым средством"));
        taskManager.deleteTasks();
        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty(), "После удаления задач список должен быть пуст");
    }

    @Test
    public void deleteEpicsShouldReturnEmptyList() {
        taskManager.addEpic(new Epic("Поменять обои", "Нужно успеть до Нового года"));
        taskManager.deleteEpics();
        List<Epic> epics = taskManager.getEpics();
        assertTrue(epics.isEmpty(), "После удаления эпиков список должен быть пуст");
    }

    @Test
    public void deleteSubtasksShouldReturnEmptyList() {
        Epic changeWallpaper = new Epic("Поменять обои", "Нужно успеть до Нового года");
        taskManager.addEpic(changeWallpaper);
        taskManager.addSubtask(new Subtask("Выбрать новые обои", "Минималистичные",
                changeWallpaper.getId()));
        taskManager.addSubtask(new Subtask("Поклеить новые обои", "Старые - выкинуть",
                changeWallpaper.getId()));
        taskManager.addSubtask(new Subtask("Убедиться, что они подходят интерьеру", "Обои подходят мебели",
                changeWallpaper.getId()));

        taskManager.deleteSubtasks();
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertTrue(subtasks.isEmpty(), "После удаления подзадач список должен быть пуст");
    }

    @Test
    public void deleteTaskByIdShouldReturnNullIfKeyIsMissing() {
        taskManager.addTask(new Task(1, "Протереть пыль", "С новой, чистой тряпкой", Status.NEW));
        taskManager.addTask(new Task(2, "Помыть полы", "С новым средством", Status.DONE));
        assertNull(taskManager.deleteTaskByID(3));
    }

    @Test
    public void deleteEpicByIdShouldReturnNullIfKeyIsMissing() {
        taskManager.addEpic(new Epic(1, "Поменять обои", "Нужно успеть до Нового года", Status.NEW));
        taskManager.deleteEpicByID(1);
        assertNull(taskManager.deleteTaskByID(1));
    }

    @Test
    public void deleteSubtaskByIdShouldReturnNullIfKeyIsMissing() {
        Epic changeWallpaper = new Epic("Поменять обои", "Нужно успеть до Нового года");
        taskManager.addEpic(changeWallpaper);
        taskManager.addSubtask(new Subtask("Выбрать новые обои", "Минималистичные",
                changeWallpaper.getId()));
        taskManager.addSubtask(new Subtask("Поклеить новые обои", "Старые - выкинуть",
                changeWallpaper.getId()));
        taskManager.addSubtask(new Subtask("Убедиться, что они подходят интерьеру", "Обои подходят мебели",
                changeWallpaper.getId()));
        assertNull(taskManager.deleteSubtaskByID(5));
    }

    @Test
    void TaskCreatedAndTaskAddedShouldHaveSameVariables() {
        Task expected = new Task(1, "Протереть пыль", "С новой, чистой тряпкой", Status.DONE);
        taskManager.addTask(expected);
        List<Task> list = taskManager.getTasks();
        Task actual = list.getFirst();
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getStatus(), actual.getStatus());
    }
}