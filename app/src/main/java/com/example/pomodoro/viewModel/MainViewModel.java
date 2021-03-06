package com.example.pomodoro.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.pomodoro.R;
import com.example.pomodoro.database.Activity;
import com.example.pomodoro.database.AppRepository;
import com.example.pomodoro.database.Project;

import java.util.List;

import timber.log.Timber;

public class MainViewModel extends AndroidViewModel {
    final static String KEY_PROJECT = "Pomodoro_Project";
    final static String KEY_ACTIVITY = "Pomodoro_Activity";

    private MutableLiveData<Project> _selectedProject;   // 当前选择的目标
    // 当前选择的活动
    private MutableLiveData<Activity> _selectedActivity = new MutableLiveData<>();
    // Total timer count number
    private MutableLiveData<Long> _activityTotalTime= new MutableLiveData<>();

    private int selectedProjectId, selectedActivityId;
    private Project dummyProject;
    private Activity dummyActivity;

    private AppRepository repository;
    private SavedStateHandle mState;    // Introduce ViewModel.SavedState

    // LiveData to handle navigation to the selected activity
    private MutableLiveData<Activity> _navigateToSelectedActivity = new MutableLiveData<>();
    public LiveData<Activity> getNavigateToSelectedActivity() {
        return _navigateToSelectedActivity;
    }

    /**
     * When the activity is clicked, set the [_navigateToSelectedProperty] [MutableLiveData]
     * @param activity The [Activity] that was clicked on.
     */
    public void displayCountDownFragment(Activity activity) {
        _navigateToSelectedActivity.setValue(activity);
    }

    /**
     * After the navigation has taken place, make sure _navigateToSelectedActivity is set to null
     */
    public void displayCountDownFragmentComplete() {
        _navigateToSelectedActivity.setValue(null);
    }

    public MainViewModel(@NonNull Application application, SavedStateHandle state) {
        super(application);
        Timber.d("ViewModel Created");

        this.repository = new AppRepository(application);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.d("ViewModel Destroyed");

        this.repository = null;
        this.dummyProject = null;
        this.dummyActivity = null;
    }

    public AppRepository getRepository() {
        return repository;
    }

    public MutableLiveData<Project> getSelectedProject() {
        Project prj;

        if (null == _selectedProject) {
            // Introduce ViewModel.SavedState
            int id = mState.get(MainViewModel.KEY_PROJECT);
            prj = (id == 0) ? dummyProject : repository.getProjectById(id);
            _selectedProject = new MutableLiveData<>();
            _selectedProject.setValue(prj);
        }
        return _selectedProject;
    }

    public void setSelectedProject(Project project) {
        if (null == _selectedProject) {
            _selectedProject = new MutableLiveData<>();
        }
        _selectedProject.setValue(project);

        // Introduce ViewModel.SavedState
        mState.set(MainViewModel.KEY_PROJECT, project.getId());
    }

    public MutableLiveData<Activity> getSelectedActivity() {

        if (null == _selectedActivity.getValue()) {
            // If none activity was selected
            _selectedActivity.setValue(dummyActivity);
        }
        return _selectedActivity;
    }

    public void setSelectedActivity(Activity activity) {
        if (null == _selectedActivity) {
            _selectedActivity = new MutableLiveData<>();
        }
        _selectedActivity.setValue(activity);

        // Introduce ViewModel.SavedState
        mState.set(MainViewModel.KEY_ACTIVITY, activity.getId());
    }

    public MutableLiveData<Long> getActivityTotalTime() {
        if (null == _activityTotalTime.getValue()) {
            _activityTotalTime.setValue(0L);
        }
        return _activityTotalTime;
    }

    public void setActivityTotalTime(Long actTime) {
        if (null == _activityTotalTime) {
            _activityTotalTime = new MutableLiveData<>();
        }
        _activityTotalTime.postValue(actTime);
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
