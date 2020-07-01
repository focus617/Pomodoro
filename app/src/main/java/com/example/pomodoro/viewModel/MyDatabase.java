package com.example.pomodoro.viewModel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Project.class}, version = 1,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase INSTANCE;

    // create singleton database
    static synchronized MyDatabase getDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MyDatabase.class,"Pomodoro")
                    .build();
        }
        return INSTANCE;
    }
    public abstract ProjectDAO getProjectDao();
}
