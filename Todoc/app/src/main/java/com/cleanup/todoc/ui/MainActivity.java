package com.cleanup.todoc.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.injections.Injection;
import com.cleanup.todoc.injections.ViewModelFactory;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.SortMethod;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.utils.Utils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {

    // FIELDS
    private TasksAdapter adapter;

    @NonNull
    private SortMethod sortMethod;

    @Nullable
    public AlertDialog dialog = null;

    @Nullable
    private EditText dialogEditText = null;

    @Nullable
    private Spinner dialogSpinner = null;

    //The RecyclerView
    @BindView(R.id.list_tasks)
    RecyclerView listTasks;
    @BindView(R.id.lbl_no_task)
    TextView lblNoTasks;
    @BindView(R.id.fab_add_task)
    FloatingActionButton fab;

    //FOR DATA
    private TaskViewModel mTaskViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        configureViewModel();
        configureRecyclerView();
        configureObserverViewModel();

        fab.setOnClickListener(view -> showAddTaskDialog());
    }

    /*-------------------------------------------------------------------------------------
                        UI configuration
     -------------------------------------------------------------------------------------*/
    // Configure ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mTaskViewModel = ViewModelProviders.of(this, mViewModelFactory).get(TaskViewModel.class);
        this.mTaskViewModel.init();
        this.sortMethod = this.mTaskViewModel.getSort();
    }
    // Configure RecyclerView
    private void configureRecyclerView(){
        adapter = new TasksAdapter(this);
        this.listTasks.setAdapter(this.adapter);
        this.listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
    //configure the observer for BDD change
    private void configureObserverViewModel(){
        //Configures the observer of Task change
        this.mTaskViewModel.getAllTask().observe(this, tasks -> updateTasks(tasks));
        //Configures the observer of Project change
        this.mTaskViewModel.getAllProjects().observe(this, this::updateProjects);
    }
    //configure observer for sortMethod change
    private void configureSorts() {
        this.mTaskViewModel.updateSortMethod(sortMethod).observe(this, this::updateSorts);
        Log.i("Sorts LiveData", sortMethod.toString());
    }
    /*---------------------------------------------------------------------------------------
                            Method for the viewModel observer
     ---------------------------------------------------------------------------------------*/
    /**
     * Method to mean adapter project change
     * @param projects {@link List<Project>}
     */
    private void updateProjects(final List<Project> projects) {
        this.adapter.updateProjects(projects);
    }

    /**
     * Method to update SortMethod field
     * @param sorts {@link SortMethod}
     */
    private void updateSorts(SortMethod sorts){
        this.sortMethod = sorts;
    }

    /**
     * Updates the list of tasks in the UI
     */
    private void updateTasks(final List<Task> tasks) {
        //if tasks is empty
        if (tasks.size() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
            this.adapter.updateListTasks(Utils.sortTasks(tasks, sortMethod));
        }
    }

    /*--------------------------------------------------------------------------------------------
                                     MENU
     -------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_alphabetical:
                this.sortMethod = SortMethod.ALPHABETICAL;
                updateSorts(this.sortMethod);
                configureSorts();
                break;
            case R.id.filter_alphabetical_inverted:
                this.sortMethod = SortMethod.ALPHABETICAL_INVERTED;
                updateSorts(this.sortMethod);
                configureSorts();
                break;
            case R.id.filter_recent_first:
                this.sortMethod = SortMethod.RECENT_FIRST;
                updateSorts(sortMethod);
                configureSorts();
                break;
            case R.id.filter_oldest_first:
                this.sortMethod = SortMethod.OLD_FIRST;
                updateSorts(sortMethod);
                configureSorts();
                break;
        }
        // SORT
        final List<Task> newTasks = Utils.sortTasks(this.adapter.getCurrentTasks(), sortMethod);
        this.adapter.updateListTasks(newTasks);

        return super.onOptionsItemSelected(item);
    }

    // -- DELETE LISTENER INTERFACE OF TASKS ADAPTER ------------------------------------------
    @Override
    public void onDeleteTask(Task task) {
        //use the deleteTask method of the viewModel
        this.deleteTask(task);
    }

    /*---------------------------------------------------------------------------------------
                             BDD method called with viewModel
     ----------------------------------------------------------------------------------------*/
    /**
     * Adds the given task to the BDD
     * @param task the task to be added to the list
     */
    private void addTask(@NonNull Task task) {
        this.mTaskViewModel.createTask(task);
    }

    /**
     * Delete task to the BDD
     * @param task to delete
     */
    private void deleteTask(Task task) {
        this.mTaskViewModel.deleteTask(task.getId());
    }

    /**
     * Called when the user clicks on the positive button of the Create Task Dialog.
     *
     * @param dialogInterface the current displayed dialog
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {

                Task task = new Task(taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                addTask(task);

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is allready closed
        else {
            dialogInterface.dismiss();
        }
    }

   /*----------------------------------------------------------------------------------------
                                 DIALOG
    ----------------------------------------------------------------------------------------*/
    private void showAddTaskDialog() {
        final AlertDialog dialog = this.getAddTaskDialog();
        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

       Utils.populateDialogSpinner(dialogSpinner, this, adapter);
    }

    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
     @NonNull
        private AlertDialog getAddTaskDialog() {
            final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

            alertBuilder.setTitle(R.string.add_task);
            alertBuilder.setView(R.layout.dialog_add_task);
            alertBuilder.setPositiveButton(R.string.add, null);
            alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    dialogEditText = null;
                    dialogSpinner = null;
                    dialog = null;
                }
            });

            dialog = alertBuilder.create();

            // This instead of listener to positive button in order to avoid automatic dismiss
            dialog.setOnShowListener(dialogInterface -> {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(view -> MainActivity.this.onPositiveButtonClick(dialog));
            });
            return dialog;
    }

}
