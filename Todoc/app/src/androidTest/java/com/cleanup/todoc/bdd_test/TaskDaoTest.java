package com.cleanup.todoc.bdd_test;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cleanup.todoc.database.dao.classeDatabase.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {
    //FOR DATA
    private TodocDatabase mTodocDatabase;

    // DATA SET FOR TEST PROJECT
    private static long PROJECT_ID = 1;
    private static Project PROJECT_DEMO = new Project(PROJECT_ID, "Tartenpion", 0xFFEADAD1);

    //DATA SET FOR TEST TASK
    private static Task NEW_TASK_1 = new Task(PROJECT_ID,"Laver", 25);
    private static Task NEW_TASK_2 = new Task(PROJECT_ID, "Réparer le néon", 35);

    //force l'execution des tests en synchrone
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.mTodocDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        mTodocDatabase.close();
    }

    @Test
    public void insertAndGetProject() throws InterruptedException {
        // BEFORE : Adding a new project
        this.mTodocDatabase.mProjectDao().createProject(PROJECT_DEMO);
        // TEST IF THE PROJECT IS WELL CREATED
        Project project = LiveDataTestUtil.getValue(this.mTodocDatabase.mProjectDao().getProject(PROJECT_ID));
        assertTrue(project.getName().equals(PROJECT_DEMO.getName()) && project.getId() == PROJECT_ID);
    }

    @Test
    public void getTasksWhenNoTaskInserted() throws InterruptedException {
        // TEST IF THE LIST OF TASK IS REALLY EMPTY
        List<Task> tasks = LiveDataTestUtil.getValue(this.mTodocDatabase.mTaskDao().getAllTasks());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void getOnlyOneTask() throws InterruptedException{
        //create project
        this.mTodocDatabase.mProjectDao().createProject(PROJECT_DEMO);
        //Insert tasks in task table
        this.mTodocDatabase.mTaskDao().insertTask(NEW_TASK_1);
        this.mTodocDatabase.mTaskDao().insertTask(NEW_TASK_2);
        //get only one task
        List<Task> tasks = LiveDataTestUtil.getValue(this.mTodocDatabase.mTaskDao().getAllTasks());
        List<Task> tasks1 = LiveDataTestUtil.getValue(this.mTodocDatabase.mTaskDao().getTask(tasks.get(1).getId()));
        //assert
        assertEquals("Réparer le néon", tasks1.get(0).getName());
        assertEquals(1, tasks1.size());
        assertEquals(2, tasks.size());
    }

    @Test
    public void insertAndGetAllTasks() throws InterruptedException{
        //create project
        this.mTodocDatabase.mProjectDao().createProject(PROJECT_DEMO);
        //Insert tasks in task table
        this.mTodocDatabase.mTaskDao().insertTask(NEW_TASK_1);
        this.mTodocDatabase.mTaskDao().insertTask(NEW_TASK_2);
        //Test if the list get only 2 members
        List<Task> tasks = LiveDataTestUtil.getValue(this.mTodocDatabase.mTaskDao().getAllTasks());
        assertTrue(tasks.size() == 2);
    }

    @Test
    public void updateTaskAndGetTheNewName() throws InterruptedException{
        //create project
        this.mTodocDatabase.mProjectDao().createProject(PROJECT_DEMO);
        //Insert tasks in task table
        this.mTodocDatabase.mTaskDao().insertTask(NEW_TASK_1);
        this.mTodocDatabase.mTaskDao().insertTask(NEW_TASK_2);
        //get the list of task
        List<Task> tasks = LiveDataTestUtil.getValue(this.mTodocDatabase.mTaskDao().getAllTasks());
        //create a new task with is the first element of the task list
        Task task3 = tasks.get(0);
        //set the name by another
        task3.setName("Passer la laveuse");
        //then update the task
        this.mTodocDatabase.mTaskDao().updateTask(task3);
        //assert that the name is correctly change
        assertEquals( "Passer la laveuse", tasks.get(0).getName());
    }

    @Test
    public void deleteTaskAndCheckIfTheListContainOnlyOne() throws InterruptedException{
        //create project
        this.mTodocDatabase.mProjectDao().createProject(PROJECT_DEMO);
        //Insert tasks in task table
        this.mTodocDatabase.mTaskDao().insertTask(NEW_TASK_1);
        this.mTodocDatabase.mTaskDao().insertTask(NEW_TASK_2);
        //get the first element of the list
        Task task3 = LiveDataTestUtil.getValue(this.mTodocDatabase.mTaskDao().getAllTasks()).get(0);
        //delete the first element
        this.mTodocDatabase.mTaskDao().deleteTask(task3.getId());
        //get the list of task
        List<Task> tasks = LiveDataTestUtil.getValue(this.mTodocDatabase.mTaskDao().getAllTasks());
        //assert the list get only one element
        assertTrue(tasks.size() == 1);
    }
}
