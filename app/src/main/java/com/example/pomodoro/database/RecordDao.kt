package com.example.pomodoro.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pomodoro.viewModel.ActivityRecord

@Dao
interface RecordDao {

    @Insert
    fun insert(record: ActivityRecord)

    @Update
    fun update(record: ActivityRecord)

    @Query("SELECT * from activity_record_table WHERE Id = :key")
    fun getRecordById(key: Long): ActivityRecord?

    @Query("DELETE FROM activity_record_table")
    fun clear()

    @Query("SELECT * FROM activity_record_table ORDER BY Id DESC LIMIT 1")
    fun getLatestRecord(): ActivityRecord?

    @Query("SELECT * FROM activity_record_table ORDER BY Id DESC")
    fun getAllRecordsLive(): LiveData<List<ActivityRecord>>
}