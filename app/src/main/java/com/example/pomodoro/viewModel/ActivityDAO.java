package com.example.pomodoro.viewModel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao // Database Access Object
public interface ActivityDAO {
    @Insert
    void insertActivity(Activity... activities);

    @Update
    void  updateActivity(Activity... activities);

    @Delete
    void  deleteActivity(Activity... activities);

    @Query("SELECT * FROM ACTIVITY WHERE id = :id")
    Activity getActivityById(int id);

    @Query("DELETE FROM ACTIVITY")
    void deleteAllActivities();

    @Query("SELECT * FROM ACTIVITY ORDER BY priority")
    LiveData<List<Activity>> getAllActivitiesLive();   //当Room查询返回时LiveData，将自动在后台线程上异步运行。
}
