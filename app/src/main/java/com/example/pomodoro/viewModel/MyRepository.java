package com.example.pomodoro.viewModel;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MyRepository {
    private LiveData<List<Project>> prjListLive;
    private ProjectDAO projectDao;

    public MyRepository(Context context) {
        MyDatabase myDatabase = MyDatabase.getDatabase(context.getApplicationContext());
        projectDao = myDatabase.getProjectDao();
        prjListLive = projectDao.getAllProjectsLive();
    }

    // Room在单独的线程上执行所有查询。
    // 观察到的LiveData将在数据更改时通知观察者
    public LiveData<List<Project>> getPrjListLive() {
        return prjListLive;
    }

    public Project getProjectById(int id) {return projectDao.getProjectById(id);}

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
            projectDAO.insertProject(projects);
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
            projectDAO.updateProject(projects);
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
            projectDAO.deleteAllProjects();
            return null;
        }
    }

}
