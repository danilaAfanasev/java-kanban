import java.awt.image.AreaAveragingScaleFilter;
import java.util.HashMap;
import java.util.*;

public class TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private int nextID = 1;

    private int getNextID(){
        return nextID++;
    }

    public Task addTask(Task task){
        task.setId(getNextID());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic addEpic(Epic epic){
        epic.setId(getNextID());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask addSubtask(Subtask subtask){
        subtask.setId(getNextID());
        Epic epic = epics.get(subtask.getEpicID());
        epic.addSubtasks(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    public Task getTaskById(int id){
        return tasks.get(id);
    }

    public Epic getEpicById(int id){
        return epics.get(id);
    }

    public Subtask SubtaskById(int id){
        return subtasks.get(id);
    }

    public ArrayList<Task> getTasks(){
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics(){
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks(){
        return new ArrayList<>(subtasks.values());
    }

    public Task updateTask(Task task){
        Integer taskId = task.getId();

        if (taskId == null || !tasks.containsKey(taskId)){
            return null;
        }

        tasks.replace(taskId, task);
        return task;
    }

    public Epic updateEpic(Epic epic){
        Integer epicId = epic.getId();

        if (epicId == null || !epics.containsKey(epicId)){
            return null;
        }

        Epic oldEpic = epics.get(epicId);
        ArrayList<Subtask> oldEpicSubtask = oldEpic.getSubtasksList();
        if (!oldEpicSubtask.isEmpty()){
            for (Subtask subtask : oldEpicSubtask){
                subtasks.remove(subtask.getId());
            }
        }

        epics.replace(epicId, epic);

        ArrayList<Subtask> newEpicSubtasks = epic.getSubtasksList();
        if (!newEpicSubtasks.isEmpty()){
            for (Subtask subtask : newEpicSubtasks){
                subtasks.put(subtask.getId(), subtask);
            }
        }

        updateEpicStatus(epic);
        return epic;
    }

    private void updateEpicStatus(Epic epic){
        int doneCount = 0;
        int newCount = 0;

        ArrayList<Subtask> list = epic.getSubtasksList();

        for (Subtask subtask : list){
            if (subtask.getStatus() == Status.DONE){
                doneCount++;
            }
            if (subtask.getStatus() == Status.NEW){
                newCount++;
            }
        }

        if (doneCount == list.size()){
            epic.setStatus(Status.DONE);
        } else if (newCount == list.size()){
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public Subtask updateSubtask(Subtask subtask){
        Integer subtaskId = subtask.getId();
        if (subtaskId == null || !subtasks.containsKey(subtaskId)){
            return null;
        }

        int epicId = subtask.getEpicID();
        Subtask oldSubtask = subtasks.get(subtaskId);
        subtasks.replace(subtaskId, subtask);

        Epic epic = epics.get(epicId);
        ArrayList<Subtask> subtaskList = epic.getSubtasksList();
        subtaskList.remove(oldSubtask);
        subtaskList.add(subtask);
        epic.setSubtasksList(subtaskList);
        updateEpicStatus(epic);
        return subtask;
    }

    public void deleteTasks(){
        tasks.clear();
    }

    public void deleteEpics(){
        epics.clear();
        subtasks.clear();
    }

    public void deleteSubtasks(){
        subtasks.clear();
        for (Epic epic : epics.values()){
            epic.clearSubstaks();
            epic.setStatus(Status.NEW);
        }
    }

    public void deleteTaskById(int id){
        tasks.remove(id);
    }

    public void deleteEpicByID(int id) {
        ArrayList<Subtask> epicSubtasks = epics.get(id).getSubtasksList();
        epics.remove(id);
        for (Subtask subtask : epicSubtasks) {
            subtasks.remove(subtask.getId());
        }
    }

    public void deleteSubtaskByID(int id) {
        Subtask subtask = subtasks.get(id);
        int epicID = subtask.getEpicID();
        subtasks.remove(id);

        Epic epic = epics.get(epicID);
        ArrayList<Subtask> subtaskList = epic.getSubtasksList();
        subtaskList.remove(subtask);
        epic.setSubtasksList(subtaskList);
        updateEpicStatus(epic);
    }
}
