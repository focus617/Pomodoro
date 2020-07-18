package com.example.pomodoro.ui.countdowntimer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.pomodoro.R;
import com.example.pomodoro.database.Activity;
import com.example.pomodoro.databinding.FragmentCountdownBinding;
import com.example.pomodoro.viewModel.MainViewModel;

import timber.log.Timber;

public class CountDownFragment extends Fragment {

    private FragmentCountdownBinding mBinding;
    private MainViewModel mModel;
    private CountDownViewModel mCountDownViewModel;

    private Activity mActivity;
    //private LifeObserverTimer mTimer;
    private MediaPlayer mMediaPlayer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate: ");

        int activity_id = CountDownFragmentArgs.fromBundle(getArguments()).getActivityId();
        Timber.d("activityID = %d ",activity_id);

        //TODO: remove MainViewModel and create MyRepository in CountDownViewModel.
        // Get the ViewModel.
        mModel = new ViewModelProvider(requireActivity(),
                new SavedStateViewModelFactory(requireActivity().getApplication(), this))
                .get(MainViewModel.class);

        // Get the Fragment ViewModel.
        mCountDownViewModel =  new ViewModelProvider(requireActivity(),
                new SavedStateViewModelFactory(requireActivity().getApplication(),this))
                .get(CountDownViewModel.class);

        //Get current activity
        mActivity = mModel.getSelectedActivity().getValue();
        mCountDownViewModel.set_currentActivity(mActivity);

/*        // Create a Timer with lifecycle Observer func
        mTimer = new LifeObserverTimer(this.getLifecycle());*/
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //Toast.makeText(getActivity(), String.format("CountDown Fragment:" +
        //        mModel.getSelectedActivity().getValue().getTitle()), Toast.LENGTH_SHORT).show();


        // Databinding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_countdown, container, false);
        mBinding.setModel(mCountDownViewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        // Create the observer which updates the UI.
        final Observer<Long> observer = new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long timecount) {
                // 因为使用了Databinding，就不需要每秒手动更新时分秒了。
                mBinding.progressCircular.setProgress(mCountDownViewModel.progress.getValue());
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        //mModel.getTimeCounter().observe(getViewLifecycleOwner(), observer);
        mCountDownViewModel.getCurrentTime().observe(getViewLifecycleOwner(),observer);

        // Create the observer for TimeUp Event.
        final Observer<Boolean> observerTimeUp = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean hasTimeUp) {
                // TODO：如果定时到达，启动提醒Dialog
                if(hasTimeUp) {
                    Timber.d("Time is Up!");

                    mMediaPlayer = MediaPlayer.create(getContext(), R.raw.music);
                    mMediaPlayer.start();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.timeup_dialog_title);
                    builder.setMessage(R.string.timeup_dialog_message);
                    builder.setNegativeButton(R.string.dialog_quit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Timber.d("AlertDialog.onCancel: ");
                            mMediaPlayer.stop();
                            mMediaPlayer.release();
                            NavController navController = Navigation.findNavController(getView());
                            navController.popBackStack();

                            // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                            mModel.displayCountDownFragmentComplete();
                        }
                    });
                    builder.show();

                    mBinding.btnReset.setVisibility(View.VISIBLE);
                    mBinding.btnResume.setVisibility(View.GONE);
                    mBinding.btnPause.setVisibility(View.GONE);
                }
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mCountDownViewModel.getEventTimeUp().observe(getViewLifecycleOwner(), observerTimeUp);

        mBinding.btnStart.setVisibility(View.GONE);
        mBinding.btnPause.setVisibility(View.VISIBLE);
        mBinding.btnReset.setVisibility(View.VISIBLE);
        mBinding.btnResume.setVisibility(View.GONE);

        mBinding.btnPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //stopTimer();
                mCountDownViewModel.onPauseTimer();

                mBinding.btnPause.setVisibility(View.GONE);
                mBinding.btnResume.setVisibility(View.VISIBLE);
            }
        });

        mBinding.btnResume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //startTimer();
                mCountDownViewModel.onResumeTimer();

                mBinding.btnPause.setVisibility(View.VISIBLE);
                mBinding.btnResume.setVisibility(View.GONE);
            }
        });

        mBinding.btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mCountDownViewModel.onResetTimer();

                mBinding.btnResume.setVisibility(View.GONE);
                mBinding.btnPause.setVisibility(View.VISIBLE);
            }
        });

        //startTimer();
        mCountDownViewModel.onStartTimer(mActivity.getTotalTime());

        return mBinding.getRoot();
    }

    // TODO: move allTimeCount into a service, in order to avoid the fragment lifecycle impact.
/*    private void startTimer() {
        Timber.d("startTimer: ");

        if (mTimerTask == null) {

            mTimerTask = new TimerTask() {

                @Override
                public void run() {
                    Timber.d("run: timer count--: " +
                            String.valueOf(mModel.getTimeCounter().getValue()));

                    mModel.countdown();

                    // 每秒通知 Activity - TimerView 减一
                    // 因为使用了Observed LiveData，就不需要每秒手动更新UI了。
                    //handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);

                    // 如果计时到零
                    if (mModel.getTimeCounter().getValue() <= 0) {
                        Timber.d( "run: time is up!");

                        handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
                        stopTimer();
                    }
                }
            };

            // This is what initially starts the timer
            mTimer.schedule(mTimerTask, 1000, 1000);
        }
    }*/

/*    private void stopTimer() {
        Timber.d("stopTimer: ");

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }*/

    // 因为 TimerTask在一个线程里面，需要使用Handler通知 Activity 来更新计时器UI
    // Note that the Thread the handler runs on is determined by a class called Looper.
    // In this case, no looper is defined, and it defaults to the main or UI thread.
/*    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_WHAT_TIME_TICK:
                    // 因为使用了Observed LiveData，就不需要每秒手动更新UI了。
*//*                    int timecount = mModel.getTimeCounter().getValue();
                    int alltime = mModel.getActivityTotalTime().getValue();
                    int progress = timecount * 100 / alltime;
                    mBinding.progressCircular.setProgress(progress);*//*
                    break;

                case MSG_WHAT_TIME_IS_UP:
*//*                    mMediaPlayer = MediaPlayer.create(getContext(), R.raw.music);
                    mMediaPlayer.start();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.timeup_dialog_title);
                    builder.setMessage(R.string.timeup_dialog_message);
                    builder.setNegativeButton(R.string.dialog_quit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Timber.d("AlertDialog.onCancel: ");
                            mMediaPlayer.stop();
                            mMediaPlayer.release();
                            NavController navController = Navigation.findNavController(getView());
                            navController.popBackStack();
                        }
                    });
                    builder.show();

                    mBinding.btnReset.setVisibility(View.VISIBLE);
                    mBinding.btnResume.setVisibility(View.GONE);
                    mBinding.btnPause.setVisibility(View.GONE);*//*
                    break;

                default:
                    break;
            }
        }

        ;
    };*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy: ");
        //mTimer.cancel();
    }

}
