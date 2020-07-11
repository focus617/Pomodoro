package com.example.pomodoro.ui.notifications;

import android.util.Log;

import androidx.annotation.LongDef;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.ConcurrentModificationException;
import java.util.Timer;

import timber.log.Timber;

public class LifeObserverTimer extends Timer implements LifecycleObserver {

    public LifeObserverTimer(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate(LifecycleOwner owner){
        Timber.d("onCreate: ");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner){
        Timber.d("onDestroy: ");

    }
}
