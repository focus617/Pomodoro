package com.example.pomodoro.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.pomodoro.R;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    final static String KEY_PROJECT = "Pomodoro_Project";
    final static String KEY_ACTIVITY = "Pomodoro_Activity";

    private MyRepository repository;
    private MutableLiveData<Project> selectedProject;    // 当前选择的目标活动
    private MutableLiveData<Activity> currentActivity;  // 当前选择的活动
    public MutableLiveData<Integer> timeCounter;

    // Introduce ViewModel.SavedState
    private SavedStateHandle mState;
    public MainViewModel(@NonNull Application application, SavedStateHandle mState) {
        super(application);
        repository = new MyRepository(application);
        this.mState = mState;
    }


    public LiveData<List<Project>> getPrjListLive() {
        return repository.getPrjListLive();
    }

    public MutableLiveData<Project> getSelectedProject() {
        if (null == selectedProject){
            selectedProject = new MutableLiveData<>();
            selectedProject.setValue(getPrjListLive().getValue().get(1));
        }
        return selectedProject;

/*        // Introduce ViewModel.SavedState
        if (!mState.contains(MainViewModel.KEY_PROJECT)) {
            selectedProject = new MutableLiveData<>();
            selectedProject.setValue(createDummyProject());
            mState.set(MainViewModel.KEY_PROJECT, selectedProject);
        }
        return mState.getLiveData(MainViewModel.KEY_PROJECT);*/
    }

    public void setSelectedProject(Project project) {
        if (null == selectedProject) {
            selectedProject = new MutableLiveData<>();
        }
        selectedProject.setValue(project);
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

    public MutableLiveData<Integer> getTimeCounter() {
        if (null == timeCounter) {
            timeCounter = new MutableLiveData<>();
            timeCounter.setValue(currentActivity.getValue().getAllTime());
        }
        return timeCounter;
    }

    public void setTimeCounter(Integer timeCount) {
        if (null == timeCounter) {
            timeCounter = new MutableLiveData<>();
        }
        timeCounter.postValue(timeCount);
    }

    public void resetTimeCounter(Activity act) {
        if (null == timeCounter) {
            timeCounter = new MutableLiveData<>();
        }
        timeCounter.setValue(act.getAllTime());
    }

    public void countdown() {
        int counter = timeCounter.getValue();
        if (counter > 0) {
            timeCounter.postValue(counter - 1);
        }
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
            Project prj = new Project(projects[i], images[i]);
            prj.setPriority(i + 1);
            insertProjects(prj);
        }
    }

    public Project createDummyProject() {
        Project prj = new Project("番茄工作", R.drawable.read_book);
        return prj;
    }

    public Activity createDummyActivity() {
        Activity act = new Activity("番茄工作时间", R.drawable.focus, 25 * 60 * 1000);
        return act;
    }

    public void insertProjects(Project... projects) {
        repository.insertProjects(projects);
    }

    public void updateProjects(Project... projects) {
        repository.updateProjects(projects);
    }

    public void deleteProjects(Project... projects) {
        repository.deleteProjects(projects);
    }

    public void deleteAllProjects(Void... voids) {
        repository.deleteAllProjects();
    }


}
