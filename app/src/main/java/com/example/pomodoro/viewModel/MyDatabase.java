package com.example.pomodoro.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

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

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database){
            database.execSQL("ALTER TABLE project ADD COLUMN XXX INTEGER NOT NULL DEFAULT 1");
        }
    };
}
