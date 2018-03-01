package eu.epitech.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoAdapter extends ArrayAdapter<ToDo>
{
    public ToDoAdapter(Context context, ArrayList<ToDo> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ToDo todo = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tasklistrow, parent, false);
        TextView title = (TextView) convertView.findViewById(R.id.task_title);
        TextView description = (TextView) convertView.findViewById(R.id.task_description);
        TextView id = (TextView) convertView.findViewById(R.id.task_id);
        Switch done = (Switch) convertView.findViewById(R.id.task_finished);
        title.setText(todo.title);
        description.setText(todo.description);
        id.setText(Integer.toString(todo.id));
        done.setChecked(todo.isFinish);
        return convertView;
    }
}
