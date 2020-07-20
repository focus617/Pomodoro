package com.example.pomodoro.ui.countdowntimer;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro.database.AppRepository;

public class MyViewModelFactory implements ViewModelProvider.Factory {

    private AppRepository mRepository;

    public MyViewModelFactory(AppRepository appRepository) {
        mRepository = appRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CountDownViewModel.class)) {
            // 默认使用对应ViewModel类的构造函数创建实例对象
            return (T) new CountDownViewModel(mRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
