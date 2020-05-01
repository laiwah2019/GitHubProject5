package com.cleanup.todoc.injections;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.ViewModel.TaskViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    //fields viewModel
    private final ProjectDataRepository mProjectDataRepository;
    private final TaskDataRepository mTaskDataRepository;
    private final Executor mExecutor;

    /**
     * Constructor
     * @param projectDataRepository
     * @param taskDataRepository
     * @param executor
     */
    public ViewModelFactory(ProjectDataRepository projectDataRepository, TaskDataRepository taskDataRepository, Executor executor) {
        this.mProjectDataRepository = projectDataRepository;
        this.mTaskDataRepository = taskDataRepository;
        this.mExecutor = executor;
    }

    /**
     *
     * @param modelClass
     * @param <T>
     * @return com.cleanup.todoc.ViewModel
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TaskViewModel.class)) {
            return (T) new TaskViewModel(this.mProjectDataRepository,
                                        this.mTaskDataRepository,
                                        this.mExecutor);
        }
        throw new IllegalArgumentException("Unknown com.cleanup.todoc.ViewModel class");
    }
}
