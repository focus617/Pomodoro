package com.example.pomodoro.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;

import static java.util.Calendar.*;

// 表示项目（目标）的数据类，用来存储创建的项目，并提供给HomeFragment
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
    private int priority;      /* 显示的顺序 */
    @ColumnInfo(name = "createTime")
    private long createTime;

    /**
     * Room会使用这个构造器来存储数据，也就是当你从表中得到 Project对象时候，Room会使用这个构造器
     * */
    public Project(int id, String title, int imageId, int priority, long createTime) {
        this.id = id;
        this.title = title;
        this.imageId = imageId;
        this.priority = priority;
        this.createTime = createTime;
    }

    /**
     * 由于Room只能识别和使用一个构造器，如果希望定义多个构造器，你可以使用Ignore标签，让Room忽略这个构造器
     * 同样，@Ignore标签还可用于字段，使用@Ignore标签标记过的字段，Room不会持久化该字段的数据
     * */
    @Ignore
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
