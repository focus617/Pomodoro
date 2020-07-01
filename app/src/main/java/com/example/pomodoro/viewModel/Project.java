package com.example.pomodoro.viewModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

import static java.util.Calendar.*;

// 表示项目（目标）的数据类，用来存储创建的项目，并提供给ItemFragment
@Entity
public class Project {

    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * 如果希望与成员变量的名称不同，请通过name指定列的名称。
     * */
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "imageID")
    private int imageId;
    @ColumnInfo(name = "priority")
    private int priority;
    @ColumnInfo(name = "createTime")
    private long createTime;

    public Project(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
        this.createTime = getInstance().getTimeInMillis();
        this.priority = 0;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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
