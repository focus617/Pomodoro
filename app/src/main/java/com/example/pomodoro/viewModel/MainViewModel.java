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

    private MutableLiveData<Project> selectedProject;   // 当前选择的目标活动
    private MutableLiveData<Activity> selectedActivity; // 当前选择的活动
    private int selectedProjectId, selectedActivityId;
    private Project dummyProject;
    private Activity dummyActivity;

    public MutableLiveData<Integer> activityTotalTime;    // Total timer count number
    public MutableLiveData<Integer> timeCounter;        // Countdown timer

    private MyRepository repository;
    private SavedStateHandle mState;    // Introduce ViewModel.SavedState

    public MainViewModel(@NonNull Application application, SavedStateHandle state) {
        super(application);
        this.repository = new MyRepository(application);
        this.dummyProject = createDummyProject();
        this.dummyActivity = createDummyActivity();

        if (!state.contains(MainViewModel.KEY_PROJECT)) {
            selectedProjectId = 0;
            state.set(MainViewModel.KEY_PROJECT, selectedProjectId);
        }
        if (!state.contains(MainViewModel.KEY_ACTIVITY)) {
            selectedActivityId = 0;
            state.set(MainViewModel.KEY_ACTIVITY, selectedActivityId);
        }
        this.mState = state;
    }

    public MutableLiveData<Project> getSelectedProject() {
        Project prj;

        if (null == selectedProject) {
            // Introduce ViewModel.SavedState
            int id = mState.get(MainViewModel.KEY_PROJECT);
            prj = (id == 0) ? dummyProject : repository.getProjectById(id);
            selectedProject = new MutableLiveData<>();
            selectedProject.setValue(prj);
        }
        return selectedProject;
    }

    public void setSelectedProject(Project project) {
        if (null == selectedProject) {
            selectedProject = new MutableLiveData<>();
        }
        selectedProject.setValue(project);

        // Introduce ViewModel.SavedState
        mState.set(MainViewModel.KEY_PROJECT, project.getId());
    }

    public MutableLiveData<Activity> getSelectedActivity() {
        Activity act;

        if (null == selectedActivity) {
            // Introduce ViewModel.SavedState
            int id = mState.get(MainViewModel.KEY_ACTIVITY);

            if (id == 0) {
                act = dummyActivity;
            } else {
                //TODO： act = repository.getActivityById(id);
                act = dummyActivity;
            }

            selectedActivity = new MutableLiveData<>();
            selectedActivity.setValue(act);
        }
        return selectedActivity;
    }

    public void setSelectedActivity(Activity activity) {
        if (null == selectedActivity) {
            selectedActivity = new MutableLiveData<>();
        }
        selectedActivity.setValue(activity);
        activityTotalTime.setValue(activity.getTotalTime());

        // Introduce ViewModel.SavedState
        mState.set(MainViewModel.KEY_ACTIVITY, activity.getId());
    }

    public MutableLiveData<Integer> getActivityTotalTime() {
        if (null == activityTotalTime) {
            activityTotalTime = new MutableLiveData<>();
            activityTotalTime.setValue(getSelectedActivity().getValue().getTotalTime());
        }
        return activityTotalTime;
    }

    public void setActivityTotalTime(Integer actTime) {
        if (null == activityTotalTime) {
            activityTotalTime = new MutableLiveData<>();
        }
        activityTotalTime.postValue(actTime);
    }

    public MutableLiveData<Integer> getTimeCounter() {
        if (null == timeCounter) {
            timeCounter = new MutableLiveData<>();
            timeCounter.setValue(getSelectedActivity().getValue().getTotalTime()); //TODO: Check reasonable?
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
        timeCounter.setValue(act.getTotalTime());
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
        prj.setId(0);
        return prj;
    }

    // Create dummy list for testing purpose
    public void createDummyActList() {
        String[] actions = {
                "番茄工作时间",
                "喝水，休息一下眼睛，活动身体",
                "锻炼时间",
                "午睡",
                "用餐时间"
        };
        int[] total_time = {
                25*60,
                5*60,
                30*60,
                30*60,
                60*60
        };

        // Add some sample items.
        for (int i = 0; i < actions.length; i++) {
            Activity act = new Activity(actions[i], R.drawable.focus, total_time[i]);
            act.setPriority(i + 1);
            insertActivities(act);
        }
    }

    public Activity createDummyActivity() {
        Activity act = new Activity("番茄工作时间", R.drawable.focus, 25 * 60);
        act.setId(0);
        return act;
    }

    // Data functions accessing database
    public LiveData<List<Project>> getPrjListLive() {
        return repository.getPrjListLive();
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

    public LiveData<List<Activity>> getActListLive() {
        return repository.getActListLive();
    }

    public void insertActivities(Activity... activities) {
        repository.insertActivities(activities);
    }

    public void updateActivities(Activity... activities) {
        repository.updateActivities(activities);
    }

    public void deleteActivities(Activity... activities) {
        repository.deleteActivities(activities);
    }

    public void deleteAllActivities(Void... voids) {
        repository.deleteAllActivities();
    }
}
