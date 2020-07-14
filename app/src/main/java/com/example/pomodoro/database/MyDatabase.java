package com.example.pomodoro.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Project.class, Activity.class, Record.class}, version = 1,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    // volatile: make sure the value of INSTANCE is always up-to-date
    //           and the same to all execution threads.
    private static volatile MyDatabase INSTANCE = null;

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

    public abstract ActivityDAO getActivityDao();

    public abstract RecordDao getRecordDao();

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database){
            database.execSQL("ALTER TABLE project ADD COLUMN XXX INTEGER NOT NULL DEFAULT 1");
        }
    };
}
