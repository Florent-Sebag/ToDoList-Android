package eu.epitech.todolist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    InteractDb Db;
    ToDoAdapter mAdapter;
    ListView TaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Db = new InteractDb(this);
        TaskList = (ListView)findViewById(R.id.TaskList);
        refreshTaskList();
    }

    private void refreshTaskList()
    {
        ArrayList<ToDo> taskList = Db.getTaskList();

        if (mAdapter == null)
        {
            mAdapter = new ToDoAdapter(this, taskList);
            TaskList.setAdapter(mAdapter);
        }
        else
        {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void print_newtask_dialog(final String taskToChange, final String descr, final String id)
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("New Task");

        dialog.setContentView(R.layout.editdialog);
        dialog.show();

        final EditText userInputtitle = (EditText) dialog.findViewById(R.id.title);
        final EditText userInputdescr = (EditText) dialog.findViewById(R.id.description);

        if (taskToChange != null)
        {
            ((EditText) dialog.findViewById(R.id.title)).setText(taskToChange);
            ((EditText) dialog.findViewById(R.id.description)).setText(descr);
        }
        //if button save etc
        dialog.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskToChange != null)
                    Db.EditTask(String.valueOf(userInputtitle.getText()), String.valueOf(userInputdescr.getText()), id);
                else
                    Db.InsertNewTask(String.valueOf(userInputtitle.getText()), String.valueOf(userInputdescr.getText()));
                refreshTaskList();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);

        //Change menu icon color
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_task:
                print_newtask_dialog(null, null, "0");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteTask(View view)
    {
        /*TextView taskTextView = (TextView)parent.findViewById(R.id.task_id);
        Log.e("String", (String) taskTextView.getText());
        String task = String.valueOf(taskTextView.getText());*/
        final String id = getID((View)view.getParent());
        Db.DeleteTask(id);
        refreshTaskList();
    }

    public final String getID(View parent)
    {
        TextView id = (TextView)parent.findViewById(R.id.task_id);
        final String idToChange = String.valueOf(id.getText());
        return (idToChange);
    }

    public void editTask(View view)
    {
        //Recupere le champ selectionner
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.task_title);
        TextView taskdescr = (TextView)parent.findViewById(R.id.task_description);
        final String taskToChange = String.valueOf(taskTextView.getText());
        final String taskToChange_descr = String.valueOf(taskdescr.getText());
        final String id = getID(parent);
        print_newtask_dialog(taskToChange, taskToChange_descr, id);
    }

    public void setFinished(View view)
    {
        View parent = (View)view.getParent();
        Switch simpleSwitch = (Switch) parent.findViewById(R.id.task_finished);
        Boolean switchState = simpleSwitch.isChecked();
        final String id = getID(parent);
        Db.setFinished(id, switchState);
        refreshTaskList();
    }
}
