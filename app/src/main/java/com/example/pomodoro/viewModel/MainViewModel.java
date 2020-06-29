package com.example.pomodoro.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {
    private ArrayList<Project> list;
    private MutableLiveData<ArrayList<Project>> prjList;

    public MainViewModel(@NonNull Application application)  {
        super(application);
        list = new ArrayList<Project>();
        prjList = new MutableLiveData<>();
        prjList.setValue(list);

    }

    public MutableLiveData<ArrayList<Project>> getPrjList() {
        if (prjList == null) {
            prjList = new MutableLiveData<>();
            prjList.setValue(list);
        }
        return prjList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        prjList = null;
    }

    public void addProject(Project prj) {
        list.add(prj);
        prjList.setValue(list);
    }

    public void clearPrjList() {
        list.clear();
        prjList.setValue(list);
    }

}

