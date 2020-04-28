package com.cleanup.todoc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cleanup.todoc.model.Project;

import java.util.List;


@Dao
public interface ProjectDao {

    /**
     * Create new project
     * @param project
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createProject(Project project);

    /**
     * Get all projects
     * @return List of projects
     */
    @Query("SELECT * FROM project")
    LiveData<List<Project>> getProjects();

    /**
     * Get only one project
     * @param projectId id of the project
     * @return
     */
    @Query("SELECT * FROM Project WHERE id = :projectId")
    LiveData<Project> getProject(long projectId);

    /**
     * Insert  method
     * @param project
     * @return
     */
    @Insert
    long insertProject(Project project);

}
