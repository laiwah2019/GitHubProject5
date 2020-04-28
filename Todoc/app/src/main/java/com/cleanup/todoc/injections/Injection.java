package com.cleanup.todoc.injections;

import android.content.Context;

import com.cleanup.todoc.database.dao.classeDatabase.TodocDatabase;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Injection {

    /**
     * Create a new TaskDataRepository
     * @param context
     * @return a new TaskDataRepository
     */
    public static TaskDataRepository provideTaskDataSource(Context context) {
        TodocDatabase database = TodocDatabase.getInstance(context);
        return new TaskDataRepository(database.mTaskDao());
    }

    /**
     * Create a new ProjectDataRepository
     * @param context
     * @return a new ProjectDataRepository
     */
    public static ProjectDataRepository provideProjectDataSource(Context context) {
        TodocDatabase database = TodocDatabase.getInstance(context);
        return new ProjectDataRepository(database.mProjectDao());
    }

    /**
     *
     * @return new Executor
     */
    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    /**
     * create ViewModelFactory for injection
     * @param context
     * @return ViewModelFactory
     */
    public static ViewModelFactory provideViewModelFactory(Context context) {
        TaskDataRepository dataSourceTask = provideTaskDataSource(context);
        ProjectDataRepository dataSourceProject = provideProjectDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceProject, dataSourceTask, executor);
    }
}
