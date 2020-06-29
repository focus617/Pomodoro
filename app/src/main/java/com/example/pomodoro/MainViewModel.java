package com.example.pomodoro;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Calendar;

// 表示项目的数据类，用来存储创建的项目，并提供给HomeFragment
class Project {
    private int id;
    private String title;
    private long create_time;

    public Project(String prjname) {
        this.id = 0;
        this.title = prjname;
        this.create_time = Calendar.getInstance().getTimeInMillis();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    public int getId() {
        return this.id;
    }

    public long getCreateTime() {
        return this.create_time;
    }

/*    public String toString() {
        private Calendar date;
        date = Calendar.getInstance();
        date = Calendar.setTimeInMillis(this.create_time);
        String timeLabel =String.format("%年%d月%d日",
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH)+1,
                date.get(Calendar.DAY_OF_MONTH));

        return this.title+timeLabel;
    }*/

}

public class MainViewModel extends ViewModel {
    private ArrayList<Project> list;
    public MutableLiveData< ArrayList<Project> > prjList;

    public MainViewModel(){
        prjList = new MutableLiveData<>();
        prjList.setValue(list);

    }

    public void addProject(Project prj){
        list.add(prj);
        prjList.setValue(list);
    }

    public void clearPrjList(){
        list.clear();
        prjList.setValue(list);
    }

}

