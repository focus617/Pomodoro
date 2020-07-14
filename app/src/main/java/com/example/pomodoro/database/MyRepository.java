package com.example.pomodoro.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MyRepository {
    private LiveData<List<Project>> prjListLive;
    private LiveData<List<Activity>> actListLive;
    private ProjectDAO projectDao;
    private ActivityDAO activityDao;

    public MyRepository(Context context) {
        MyDatabase myDatabase = MyDatabase.getDatabase(context.getApplicationContext());
        projectDao = myDatabase.getProjectDao();
        prjListLive = projectDao.getAllProjectsLive();
        activityDao = myDatabase.getActivityDao();
        actListLive = activityDao.getAllActivitiesLive();
    }

    // Room在单独的线程上执行所有查询。
    // 观察到的LiveData将在数据更改时通知观察者
    public LiveData<List<Project>> getPrjListLive() {
        return prjListLive;
    }

    public Project getProjectById(int id) {return projectDao.getById(id);}

    public void insertProjects(Project...projects){
        new InsertAsyncTask(projectDao).execute(projects);
    }

    public void updateProjects(Project...projects){
        new UpdateAsyncTask(projectDao).execute(projects);
    }

    public void deleteProjects(Project...projects){
        new DeleteAsyncTask(projectDao).execute(projects);
    }

    public void deleteAllProjects(Void...voids){
        new DeleteAllAsyncTask(projectDao).execute();
    }

    static class InsertAsyncTask extends AsyncTask<Project,Void,Void> {
        private ProjectDAO projectDAO;

        public InsertAsyncTask(ProjectDAO projectDAO) {
            this.projectDAO = projectDAO;
        }

        @Override
        protected Void doInBackground(Project... projects) {
            projectDAO.insert(projects);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Project,Void,Void>{
        private ProjectDAO projectDAO;

        public UpdateAsyncTask(ProjectDAO projectDAO) {
            this.projectDAO = projectDAO;
        }

        @Override
        protected Void doInBackground(Project... projects) {
            projectDAO.update(projects);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Project,Void,Void>{
        private ProjectDAO projectDAO;

        public DeleteAsyncTask(ProjectDAO projectDAO) {
            this.projectDAO = projectDAO;
        }

        @Override
        protected Void doInBackground(Project... projects) {
            projectDAO.deleteProject(projects);
            return null;
        }
    }


    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private ProjectDAO projectDAO;

        public DeleteAllAsyncTask(ProjectDAO projectDAO) {
            this.projectDAO = projectDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            projectDAO.clear();
            return null;
        }
    }


    // Room在单独的线程上执行所有查询。
    // 观察到的LiveData将在数据更改时通知观察者
    public LiveData<List<Activity>> getActListLive() {
        return actListLive;
    }

    public Activity getActivityById(int id) {return activityDao.getById(id);}

    public void insertActivities(Activity...activities){
        new InsertAsyncTask2(activityDao).execute(activities);
    }

    public void updateActivities(Activity...activities){
        new UpdateAsyncTask2(activityDao).execute(activities);
    }

    public void deleteActivities(Activity...activities){
        new DeleteAsyncTask2(activityDao).execute(activities);
    }

    public void deleteAllActivities(Void...voids){
        new DeleteAllAsyncTask2(activityDao).execute();
    }

    static class InsertAsyncTask2 extends AsyncTask<Activity,Void,Void> {
        private ActivityDAO activityDao;

        public InsertAsyncTask2(ActivityDAO activityDao) {
            this.activityDao = activityDao;
        }

        @Override
        protected Void doInBackground(Activity... activities) {
            activityDao.insert(activities);
            return null;
        }
    }

    static class UpdateAsyncTask2 extends AsyncTask<Activity,Void,Void>{
        private ActivityDAO activityDao;

        public UpdateAsyncTask2(ActivityDAO activityDao) {
            this.activityDao = activityDao;
        }

        @Override
        protected Void doInBackground(Activity... activities) {
            activityDao.update(activities);
            return null;
        }
    }

    static class DeleteAsyncTask2 extends AsyncTask<Activity,Void,Void>{
        private ActivityDAO activityDao;

        public DeleteAsyncTask2(ActivityDAO activityDao) {
            this.activityDao = activityDao;
        }

        @Override
        protected Void doInBackground(Activity... activities) {
            activityDao.delete(activities);
            return null;
        }
    }


    static class DeleteAllAsyncTask2 extends AsyncTask<Void,Void,Void>{
        private ActivityDAO activityDao;

        public DeleteAllAsyncTask2(ActivityDAO activityDao) {
            this.activityDao = activityDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            activityDao.clear();
            return null;
        }
    }
}
