package com.cleanup.todoc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cleanup.todoc.model.Task;

import java.util.List;


@Dao
public interface TaskDao {

    /**
     * Get one task
     * @param taskId
     * @return
     */
    @Query("SELECT * FROM task WHERE id = :taskId")
    LiveData<List<Task>> getTask(long taskId);

    /**
     * get all task
     * @return
     */
    @Query("SELECT * FROM task")
    LiveData<List<Task>> getAllTasks();

    /**
     * Insert method
     * @param task
     * @return
     */
    @Insert
    long insertTask(Task task);

    /**
     * Update method
     * @param task
     * @return
     */
    @Update
    int updateTask(Task task);

    /**
     * Delete method
     * @param taskId
     * @return
     */
    @Query("DELETE FROM task WHERE id = :taskId")
    int deleteTask(long taskId);

}
