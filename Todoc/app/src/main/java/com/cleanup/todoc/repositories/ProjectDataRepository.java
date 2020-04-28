package com.cleanup.todoc.repositories;

import android.arch.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;

import java.util.List;


public class ProjectDataRepository {

    //Field
    private final ProjectDao mProjectDao;

    //constructor
    public ProjectDataRepository(ProjectDao projectDao){
        this.mProjectDao = projectDao;
    }

    // --- GET ---

    /**
     * Get one project with his Id
     * @param projectId
     * @return
     */
    public LiveData<Project> getProject(long projectId){
        return this.mProjectDao.getProject(projectId);
    }

    /**
     * get all projects
     * @return
     */
    public LiveData<List<Project>> getProjects() {
        return this.mProjectDao.getProjects();
    }

}
