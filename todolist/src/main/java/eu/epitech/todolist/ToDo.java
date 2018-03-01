package eu.epitech.todolist;

public class ToDo {

    public String title;
    public String description;
    public boolean isFinish;
    public int id;

    public ToDo(String title, String description, boolean isFinish, int id)
    {
        this.title = title;
        this.description = description;
        this.isFinish = isFinish;
        this.id = id;
    }
}
