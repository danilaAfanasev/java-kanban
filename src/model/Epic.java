package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasksList = new ArrayList<>();

    public Epic (String name, String description){
        super(name, description);
    }

    public Epic(int id, String name, String description){
        super(id, name, description);
    }

    public void addSubtasks(Subtask subtask){
        subtasksList.add(subtask);
    }

    public void clearSubstaks(){
        subtasksList.clear();
    }

    public ArrayList<Subtask> getSubtasksList(){
        return subtasksList;
    }

    public void setSubtasksList(ArrayList<Subtask> subtasksList){
        this.subtasksList = subtasksList;
    }

    @Override
    public String toString(){
        return "model.Epic{" +
                "name= " + getName() + '\'' +
                ", description = " + getDescription() + '\'' +
                ", id=" + getId() +
                ", subtaskList.size = " + subtasksList.size() +
                ", status = " + getStatus() +
                '}';
    }
}
