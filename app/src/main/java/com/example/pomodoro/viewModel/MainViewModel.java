package com.example.pomodoro.viewModel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pomodoro.R;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private ProjectDAO projectDao;
    private LiveData<List<Project>> prjListLive;

    public MainViewModel(@NonNull Application application) {
        super(application);
        ProjectDatabase projectDatabase = ProjectDatabase.getDatabase(application);
        projectDao = projectDatabase.getProjectDao();
        prjListLive = projectDao.getAllProjectsLive();
    }

    public LiveData<List<Project>> getPrjListLive() {
        return prjListLive;
    }


    // Create dummy list for testing purpose
    public void createDummyItems(int count) {
        // Add some sample items.
        for (int i = 1; i <= count; i++) {
            insertProjects(new Project("计划目标 " + i,  R.drawable.ic_baseline_add_circle_24));
        }
    }

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

    static class InsertAsyncTask extends AsyncTask<Project,Void,Void>{
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
