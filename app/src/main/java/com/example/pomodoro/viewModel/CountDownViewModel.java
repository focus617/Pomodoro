package com.example.pomodoro.viewModel;

import android.os.CountDownTimer;
import android.text.format.DateUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import timber.log.Timber;

public class CountDownViewModel extends ViewModel {
    // Time when the game is over
    private final static Long DONE = 0L;

    // Countdown time interval
    private final static Long ONE_SECOND = 1000L;

    // Total time for the timer
    private Long mCountDown_Total_Time;
    private CountDownTimer mTimer;

    // Countdown time and timeUp event
    private MutableLiveData<Long> _currentTime = new MutableLiveData<Long>();
    private MutableLiveData<Boolean> _eventTimeUp = new MutableLiveData<Boolean>();

    // observable current countdown Timer value
    public LiveData<Long> getCurrentTime() {
            return _currentTime;
    }

    // observable Countdown progress
    public LiveData<Integer> progress = new LiveData<Integer>() {
        @Nullable
        @Override
        public Integer getValue() {
            return (int)(_currentTime.getValue() * 100 / mCountDown_Total_Time) ;
        }
    };

    // observable TimeUp Event
    public LiveData<Boolean> getEventTimeUp() {
            return _eventTimeUp;
    }

    public CountDownViewModel() {
        Timber.d("CountDownViewModel created.");
        this._eventTimeUp.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.d("CountDownViewModel destroyed!");
        if(mTimer != null)
            mTimer.cancel();
    }

    private void startTimer(Long totalTime){
        Timber.d("CountDownTimer start: TotalTime="+DateUtils.formatElapsedTime(totalTime));

        if(mTimer != null) mTimer.cancel();

        // Creates a timer which triggers countdown timer per second
        mTimer = new CountDownTimer(totalTime*ONE_SECOND, ONE_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                _currentTime.setValue(millisUntilFinished/ONE_SECOND);
                String currentTimeString = DateUtils.formatElapsedTime(_currentTime.getValue());
                Timber.d("CountDownTimer onTick - " + currentTimeString);
            }

            @Override
            public void onFinish() {
                Timber.d("CountDownTimer onFinish:");
                _currentTime.setValue(DONE);
                _eventTimeUp.setValue(true);
            }
        };
        _eventTimeUp.setValue(false);
        mTimer.start();

    }


    // Interface of ViewModel
    // 4. Start the timer
    public void onStartTimer(Long activityTotalTime) {
        Timber.d("onStartTimer: "+activityTotalTime);
        mCountDown_Total_Time = activityTotalTime;
        startTimer(activityTotalTime);
    }

    // 5. Pause the timer
    public void onPauseTimer() {
        Timber.d("onPauseTimer: ");
        mTimer.cancel();
    }

    // 6. restart the timer
    public void onResumeTimer() {
        Timber.d("onResumeTimer: "+_currentTime.getValue());
        startTimer(_currentTime.getValue());
    }

    // 7. reset the timer
    public void onResetTimer() {
        Timber.d("onResetTimer: ");
        if(mTimer != null)
            mTimer.cancel();
        startTimer(mCountDown_Total_Time);
    }

}
