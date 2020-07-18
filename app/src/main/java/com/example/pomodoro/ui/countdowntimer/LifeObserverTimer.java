package com.example.pomodoro.ui.countdowntimer;

import android.util.Log;

import androidx.annotation.LongDef;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Timer;

import timber.log.Timber;

public class LifeObserverTimer extends Timer implements LifecycleObserver {

    public LifeObserverTimer(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }
    private long startTimeStamp, endTimeStamp;

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate(LifecycleOwner owner){
        Timber.d("onCreate: ");

        Calendar currentTime = Calendar.getInstance();
        startTimeStamp = currentTime.getTimeInMillis();

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner){
        Timber.d("onDestroy: ");

        Calendar currentTime = Calendar.getInstance();
        endTimeStamp = currentTime.getTimeInMillis();


        //TODO: 在此增加“添加 ActivityRecord”的功能
    }
}
