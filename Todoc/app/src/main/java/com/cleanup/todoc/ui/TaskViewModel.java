package com.cleanup.todoc.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.SortMethod;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;


public class TaskViewModel extends ViewModel {

    //REPOSITORY
    private final ProjectDataRepository mProjectDataRepository;
    private final TaskDataRepository mTaskDataRepository;
    private final Executor mExecutor;

    //DATA
    @Nullable
    private LiveData<List<Project>> projects;
    private MutableLiveData<SortMethod> sorts = new MutableLiveData<>();

    /**
     * Constructor
     * @param projectDataRepository
     * @param taskDataRepository
     * @param executor
     */
    public TaskViewModel(ProjectDataRepository projectDataRepository, TaskDataRepository taskDataRepository, Executor executor) {
        this.mProjectDataRepository = projectDataRepository;
        this.mTaskDataRepository = taskDataRepository;
        this.mExecutor = executor;
    }

    /**
     * Initialise the project list for demo
     */
    public void init() {
        // PROJECTS
        if (this.projects == null) {
            this.projects = this.mProjectDataRepository.getProjects();
        }
    }

    /************************************************************************************
     *                                   CRUD task
     * *********************************************************************************/
    /**
     * get one task
     * @param taskId
     * @return the task whose id = taskid
     */
    public LiveData<List<Task>> getTask(long taskId){
        return mTaskDataRepository.getTask(taskId) ;
    }

    public LiveData<List<Task>> getAllTask(){
        return this.mTaskDataRepository.getAllTasks();
    }

    public void createTask(final Task task) {
        this.mExecutor.execute(() -> {
            this.mTaskDataRepository.createTask(task);
        });
    }

    public void deleteTask(long itemId) {
        mExecutor.execute(() -> {
            mTaskDataRepository.deleteTask(itemId);
        });
    }

    public void updateTask(Task task) {
        mExecutor.execute(() -> {
            mTaskDataRepository.updateTask(task);
        });
    }

    /********************************************************************************
     *  getter for project
     * *****************************************************************************/
    public LiveData<List<Project>> getAllProjects(){
        return this.projects;
    }

    /********************************************************************************
     *   SortMethod to listen the modification
     *******************************************************************************/
    public LiveData<SortMethod> updateSortMethod(SortMethod s){
        sorts.setValue(s);
        Log.i("Sorts VM", sorts.getValue().toString());
        return this.sorts;
    }

    public SortMethod getSort(){
        if (sorts == null){
            sorts.setValue(SortMethod.NONE);
            return sorts.getValue();
        }
        return sorts.getValue();
    }
}
