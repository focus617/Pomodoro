package com.example.pomodoro.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pomodoro.R;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private MyRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new MyRepository(application);

        //Temporary Call
        NotificationsViewModel();
    }

    public LiveData<List<Project>> getPrjListLive() {
        return repository.getPrjListLive();
    }


    // Create dummy list for testing purpose
    public void createDummyPrjList() {
        String[] projects = {
                "读书",
                "锻炼身体",
                "学习Android开发",
                "冥想",
                "工作",
                "读书",
                "锻炼身体",
                "学习Android开发",
                "冥想",
                "工作"
        };
        int[] images = {
                R.drawable.read_book,
                R.drawable.exercise,
                R.drawable.study,
                R.drawable.thinking,
                R.drawable.work,
                R.drawable.read_book,
                R.drawable.exercise,
                R.drawable.study,
                R.drawable.thinking,
                R.drawable.work
        };

        // Add some sample items.
        for (int i = 0; i < images.length; i++) {
            Project prj = new Project(projects[i],  images[i]);
            prj.setPriority(i+1);
            insertProjects(prj);
        }
    }

    public void insertProjects(Project...projects){
        repository.insertProjects(projects);
    }

    public void updateProjects(Project...projects){
        repository.updateProjects(projects);
    }

    public void deleteProjects(Project...projects){
        repository.deleteProjects(projects);
    }

    public void deleteAllProjects(Void...voids){
        repository.deleteAllProjects();
    }

    // Data for NotificationFragment
    private MutableLiveData<String> mText = new MutableLiveData<>();
    private MutableLiveData<String> selectedActivity ;
    public int allTimeCount = 0;

    public void NotificationsViewModel() {
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void select(String activity) {
        selectedActivity.setValue(activity);
    }

    public LiveData<String> getSelected() {
        return selectedActivity;
    }
}
