package com.cleanup.todoc.database.dao.classeDatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;


@Database(entities = {Project.class, Task.class}, version = 1, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {

    //Singleton
    private static volatile TodocDatabase INSTANCE;

    //DAO
    public abstract ProjectDao mProjectDao();
    public abstract TaskDao mTaskDao();

    //Field
    private static Project[] projects = Project.getAllProjects();

    /**
     * Create an instance if null or return it
     * @param context
     * @return
     */
    public static TodocDatabase getInstance(Context context){
        if (INSTANCE == null) {
            synchronized (TodocDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodocDatabase.class, "Todoc.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Usesto populate the DB
     * @return DB with values
     */
    private static Callback prepopulateDatabase() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                for (Project p : projects) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", p.getName());
                    contentValues.put("color", p.getColor());

                    db.insert("project", OnConflictStrategy.IGNORE, contentValues);
                }
            }
        };
    }
}
