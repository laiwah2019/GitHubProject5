package com.cleanup.todoc.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.SortMethod;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.ui.TasksAdapter;

import java.util.Collections;
import java.util.List;


public abstract class Utils {
    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    public static void populateDialogSpinner(Spinner spinner, Context context, TasksAdapter adapter) {
        final List<Project> p = adapter.getCurrentProjects();

        final ArrayAdapter<Project> adapterSpinner = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, p);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (spinner != null) {
            spinner.setAdapter(adapterSpinner);
        }
    }

    /**
     * Sorts the {@link List< Task >} in argument and returns it
     * @param tasks a {@link List<Task>}
     * @param sortMethod a {@link SortMethod}
     * @return the {@link List<Task>} but sorted
     */
    @NonNull
    public static List<Task> sortTasks(@NonNull final List<Task> tasks,@NonNull SortMethod sortMethod) {
        if (sortMethod == null) sortMethod = SortMethod.NONE;
        if (tasks.size()!= 0) {
            switch (sortMethod) {
                case ALPHABETICAL:
                    Collections.sort(tasks, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasks, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasks, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasks, new Task.TaskOldComparator());
                    break;
            }
        }
        return tasks;
    }
}
