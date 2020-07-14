package com.example.pomodoro.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecordDao {

    @Insert
    void insert(Record...Records);

    @Update
    void update(Record...Records);

    @Query("SELECT * from activity_record_table WHERE Id = :key")
    Record getById(Long key);

    @Query("DELETE FROM activity_record_table")
    void clear();

    @Query("SELECT * FROM activity_record_table ORDER BY Id DESC LIMIT 1")
    Record getLatestRecord();

    @Query("SELECT * FROM activity_record_table ORDER BY Id DESC")
    LiveData<List<Record>> getAllRecordsLive();
}
