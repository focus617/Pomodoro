package com.example.pomodoro.viewModel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Project.class}, version = 1,exportSchema = false)
public abstract class ProjectDatabase extends RoomDatabase {
    private static ProjectDatabase INSTANCE;

    // create singleton database
    static synchronized ProjectDatabase getDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ProjectDatabase.class,"project")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    public abstract ProjectDAO getProjectDao();
}
