package com.example.pomodoro;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.pomodoro.database.Activity;
import com.example.pomodoro.database.ActivityDAO;
import com.example.pomodoro.database.AppDatabase;
import com.example.pomodoro.database.Project;
import com.example.pomodoro.database.ProjectDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private AppDatabase db;
    private ProjectDAO projectDao;
    private ActivityDAO activityDAO;

    @Before
    public void createDb() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase.class)
        // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        projectDao = db.getProjectDao();
        activityDAO = db.getActivityDao();
        //recordDAO = db.getRecordDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndGetProject() {
        Project prj  = new Project("番茄工作", R.drawable.read_book);
        prj.setId(1);
        projectDao.insert(prj);
        Project project = projectDao.getById(1);
        assertEquals(project.getTitle(), "番茄工作");
    }

    @Test
    public void insertAndGetActivity() {
        Activity activity  = new Activity("番茄工作时间", R.drawable.focus, 25 * 60);
        activity.setId(1);
        activityDAO.insert(activity);
        Activity act = activityDAO.getById(1);
        assertEquals(act.getTitle(), "番茄工作时间");
    }

}
