package com.example.pomodoro.viewModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;


// 表示活动（定时工作）的数据类，用来存储创建的活动，并提供给NotificationFragment
@Entity
public class Activity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * 如果希望与成员变量的名称不同，请通过name指定列的名称。
     * */
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "imageID")
    private int imageId;
    @ColumnInfo(name = "allTime")
    private int allTime;
    @ColumnInfo(name = "createTime")
    private long createTime;

    /**
     * Room会使用这个构造器来存储数据，也就是当你从表中得到 Activity对象时候，Room会使用这个构造器
     * */
    public Activity(int id, String title, int imageId, int allTime, long createTime) {
        this.id = id;
        this.title = title;
        this.imageId = imageId;
        this.allTime = allTime;
        this.createTime = createTime;
    }

    /**
     * 由于Room只能识别和使用一个构造器，如果希望定义多个构造器，你可以使用Ignore标签，让Room忽略这个构造器
     * 同样，@Ignore标签还可用于字段，使用@Ignore标签标记过的字段，Room不会持久化该字段的数据
     * */
    @Ignore
    public Activity(String title, int imageId, int allTime) {
        this.title = title;
        this.imageId = imageId;
        this.allTime = allTime;
        this.createTime = getInstance().getTimeInMillis();
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        //this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public int getImageId() {
        return imageId;
    }

    public int getAllTime() {
        return allTime;
    }

    public void setAllTime(int allTime) {
        this.allTime = allTime;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        //this.createTime = createTime;
    }

    public String toString() {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(this.createTime);
        String timeLabel = String.format(":%年%d月%d日",
                date.get(YEAR),
                date.get(MONTH) + 1,
                date.get(DAY_OF_MONTH));

        return this.title + timeLabel;
    }

}