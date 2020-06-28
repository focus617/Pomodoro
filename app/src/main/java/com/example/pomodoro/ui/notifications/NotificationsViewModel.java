package com.example.pomodoro.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> selectedActivity;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
        selectedActivity = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void select(String activity) {
        selectedActivity.setValue(activity);
    }

    public LiveData<String> getSelected() {
        return selectedActivity;
    }
}