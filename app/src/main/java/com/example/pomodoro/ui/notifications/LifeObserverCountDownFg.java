package com.example.pomodoro.ui.notifications;

import android.util.Log;

import androidx.annotation.LongDef;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.ConcurrentModificationException;

public class LifeObserverCountDownFg implements LifecycleObserver {
    private static final String TAG = "lifeObserver_CountDownF";

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate(){
        Log.d(TAG, "onCreate: ");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(){
        Log.d(TAG, "onDestroy: ");

    }
}
