package com.example.pomodoro.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao // Database Access Object
public interface ProjectDAO {
    @Insert
    void insertProject(Project...projects);

    @Update
    void  updateProject(Project...projects);

    @Delete
    void  deleteProject(Project...projects);

    @Query("DELETE FROM PROJECT")
    void deleteAllProjects();

    @Query("SELECT * FROM PROJECT ORDER BY ID")
    LiveData<List<Project>> getAllProjectsLive();
}
