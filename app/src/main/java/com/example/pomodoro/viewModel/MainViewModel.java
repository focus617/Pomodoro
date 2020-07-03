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
    private MutableLiveData<Project> currentProject;   // 当前选择的目标活动
    private MutableLiveData<Activity> currentActivity ;
    public int allTimeCount = 0;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new MyRepository(application);
    }

    public LiveData<List<Project>> getPrjListLive() {
        return repository.getPrjListLive();
    }

    public MutableLiveData<Project> getCurrentProject() {
        if (null == currentProject){
            currentProject = new MutableLiveData<>();
            currentProject.setValue(getPrjListLive().getValue().get(0));
        }
        return currentProject;
    }

    public void setCurrentProject(Project project) {
        if (null == currentProject) {
            currentProject = new MutableLiveData<>();
        }
        currentProject.setValue(project);
    }

    public MutableLiveData<Activity> getCurrentActivity() {
        if (null == currentActivity) {
            currentActivity = new MutableLiveData<>();
            currentActivity.setValue(createDummyActivity());
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        if (null == currentActivity) {
            currentActivity = new MutableLiveData<>();
        }
        currentActivity.setValue(activity);
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

    public Activity createDummyActivity(){
        Activity act =  new Activity("番茄工作时间",R.drawable.focus, 25*60*1000);
        return act;
    }

    public Project getProjectById(int id){
        return repository.getProjectById(id);
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


}
