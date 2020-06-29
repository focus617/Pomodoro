package com.example.pomodoro.viewModel;

import java.util.Calendar;

import static java.util.Calendar.*;

// 表示项目的数据类，用来存储创建的项目，并提供给HomeFragment
public class Project {

    private int id;
    private String title;
    private long create_time;
    private Calendar date;

    public Project(String prjname) {
        this.id = 0;
        this.title = prjname;
        this.create_time = getInstance().getTimeInMillis();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public int getId() {
        return this.id;
    }

    public long getCreateTime() {
        return this.create_time;
    }

    public String toString() {
        date = Calendar.getInstance();
        date.setTimeInMillis(this.create_time);
        String timeLabel =String.format("%年%d月%d日",
                date.get(YEAR),
                date.get(MONTH)+1,
                date.get(DAY_OF_MONTH));

        return this.title + timeLabel;
    }

}
