package com.example.pomodoro.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao // Database Access Object
public interface ProjectDAO {
    @Insert
    void insert(Project...projects);

    @Update
    void update(Project...projects);

    @Delete
    void  deleteProject(Project...projects);

    @Query("SELECT * FROM PROJECT WHERE id = :id")
    Project getById(int id);

    @Query("DELETE FROM PROJECT")
    void clear();

    @Query("SELECT * FROM PROJECT ORDER BY priority")
    LiveData<List<Project>> getAllProjectsLive();   //当Room查询返回时LiveData，将自动在后台线程上异步运行。
}
