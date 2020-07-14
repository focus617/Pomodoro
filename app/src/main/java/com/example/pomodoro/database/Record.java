package com.example.pomodoro.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity_record_table")
public class Record {

    @PrimaryKey(autoGenerate = true)
    private Long id = 0L;

    @ColumnInfo(name = "start_time_milli")
    private Long startTimeMilli;

    @ColumnInfo(name = "end_time_milli")
    private Long endTimeMilli;

    @ColumnInfo(name = "activity_id")
    private int activityID = -1;

    /**
     * Room会使用这个构造器来存储数据，也就是当你从表中得到 Project对象时候，Room会使用这个构造器
     * */
    public Record(Long id, Long startTimeMilli, Long endTimeMilli, int activityID) {
        this.id = id;
        this.startTimeMilli = startTimeMilli;
        this.endTimeMilli = endTimeMilli;
        this.activityID = activityID;
    }

    /**
     * 由于Room只能识别和使用一个构造器，如果希望定义多个构造器，你可以使用Ignore标签，让Room忽略这个构造器
     * 同样，@Ignore标签还可用于字段，使用@Ignore标签标记过的字段，Room不会持久化该字段的数据
     * */
    @Ignore
    public Record(int activityID) {
        this.activityID = activityID;
        this.startTimeMilli = System.currentTimeMillis();
        this.endTimeMilli = startTimeMilli;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartTimeMilli() {
        return startTimeMilli;
    }

    public void setStartTimeMilli(Long startTimeMilli) {
        this.startTimeMilli = startTimeMilli;
    }

    public Long getEndTimeMilli() {
        return endTimeMilli;
    }

    public void setEndTimeMilli(Long endTimeMilli) {
        this.endTimeMilli = endTimeMilli;
    }

    public int getActivityID() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
    }


}
