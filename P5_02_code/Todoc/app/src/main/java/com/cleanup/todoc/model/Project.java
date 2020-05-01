package com.cleanup.todoc.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;


@Entity(tableName = "project")
public class Project {
    /**
     * Fields
     */
    @PrimaryKey(autoGenerate = true)
    private  long id;

    @NonNull
    @ColumnInfo(name = "name")
    private  String name;

    @ColorInt
    @ColumnInfo(name = "color")
    private  int color;

    /**
     * Instantiates a new Project.
     *
     * @param id    the unique identifier of the project to set
     * @param name  the name of the project to set
     * @param color the hex (ARGB) code of the color associated to the project to set
     */
    @Ignore
    @VisibleForTesting
    public Project(long id, @NonNull String name, @ColorInt int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    /**
     * Instantiates a new Project with BDD.
     *
     * @param name  the name of the project to set
     * @param color the hex (ARGB) code of the color associated to the project to set
     */
    public Project(@NonNull String name, @ColorInt int color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Returns all the projects of the application.
     *
     * @return all the projects of the application
     */
    @NonNull
    public static Project[] getAllProjects() {
        return new Project[]{
                new Project(1L,"Projet Tartampion", 0xFFEADAD1),
                new Project(2L,"Projet Lucidia", 0xFFB4CDBA),
                new Project(3L,"Projet Circus", 0xFFA3CED2),
        };
    }

    /**
     * Returns the project with the given unique identifier, or null if no project with that
     * identifier can be found.
     *
     * @param id the unique identifier of the project to return
     * @return the project with the given unique identifier, or null if it has not been found
     */
    @Nullable
    public static Project getProjectById(long id) {
        for (Project project : getAllProjects()) {
            if (project.id == id)
                return project;
        }
        return null;
    }

    /**
     * Returns the unique identifier of the project.
     *
     * @return the unique identifier of the project
     */
    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @ColorInt
    public int getColor() {
        return color;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    @NonNull
    public String toString() {
        return getName();
    }
}
