package eu.epitech.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class InteractDb extends SQLiteOpenHelper {

    private static final String DB_NAME="SebagDb";
    private static final int DB_VER = 4;
    private static final String DB_TABLE="Task";
    private static final String DB_COLUMN = "TaskName";

    public InteractDb(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL,Description TEXT, Done INTEGER);", DB_TABLE, DB_COLUMN);
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DROP TABLE IF EXISTS %s", DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    public void InsertNewTask(String title, String descr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ToAdd = new ContentValues();
        ToAdd.put(DB_COLUMN, title);
        ToAdd.put("Description", descr);
        ToAdd.put("Done", 0);
        db.insertWithOnConflict(DB_TABLE, null, ToAdd, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void DeleteTask(String ToDelete)
    {
        String Args[] = new String[]{ToDelete};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, "ID = ?", Args);
        db.close();
    }

    public void EditTask(String title, String descr, String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Task SET TaskName='" + title + "' WHERE ID=" + id);
        db.execSQL("UPDATE Task SET Description='" + descr + "' WHERE ID=" + id);
        db.close();
    }

    public void setFinished(final String id, boolean isFinish)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        if (isFinish)
            db.execSQL("UPDATE Task SET Done=1 WHERE ID=" + id);
        else
            db.execSQL("UPDATE Task SET Done=0 WHERE ID=" + id);
        db.close();
    }

    public ArrayList<ToDo> getTaskList()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<ToDo> res = new ArrayList<ToDo>();
        Cursor it = db.query(DB_TABLE, new String[]{DB_COLUMN, "Description", "ID", "Done"}, null, null, null, null, null);

        int index_title;
        int index_description;
        int index_id;
        int index_done;
        boolean isFinish = false;

        while (it.moveToNext())
        {
            index_title = it.getColumnIndex(DB_COLUMN);
            index_description = it.getColumnIndex("Description");
            index_id = it.getColumnIndex("ID");
            index_done = it.getColumnIndex("Done");
            if (it.getInt(index_done) == 1)
                isFinish = true;
            ToDo ToAdd = new ToDo(it.getString(index_title), it.getString(index_description), isFinish, it.getInt(index_id));
            res.add(ToAdd);
            isFinish = false;
        }
        it.close();
        db.close();
        return (res);
    }

}
