package com.example.pomodoro.ui.notifications;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.pomodoro.R;
import com.example.pomodoro.databinding.FragmentCountdownBinding;
import com.example.pomodoro.viewModel.Activity;
import com.example.pomodoro.viewModel.MainViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class CountDownFragment extends Fragment {
    private static final String TAG = "CountDownFragment";

    private static final int MSG_WHAT_TIME_IS_UP = 1;
    private static final int MSG_WHAT_TIME_TICK = 2;

    private Activity activity;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private MediaPlayer mp;

    private MainViewModel model;
    private FragmentCountdownBinding binding;

    private LifeObserverCountDownFg observer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the ViewModel.
        model = new ViewModelProvider(requireActivity(),
                new SavedStateViewModelFactory(requireActivity().getApplication(), this))
                .get(MainViewModel.class);

        //Get current activity
        activity = model.getSelectedActivity().getValue();

        // 增加一个lifecycle Observer
        observer = new LifeObserverCountDownFg();
        getLifecycle().addObserver(observer);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Databinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_countdown, container, false);
        binding.setModel(model);
        binding.setLifecycleOwner(requireActivity());

        Toast.makeText(getActivity(), String.format("CountDown Fragment:" +
                model.getSelectedActivity().getValue().getTitle()), Toast.LENGTH_SHORT).show();

        binding.btnStart.setVisibility(View.GONE);
        binding.btnPause.setVisibility(View.VISIBLE);
        binding.btnReset.setVisibility(View.VISIBLE);
        binding.btnResume.setVisibility(View.GONE);

        binding.btnPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopTimer();
                binding.btnPause.setVisibility(View.GONE);
                binding.btnResume.setVisibility(View.VISIBLE);
            }
        });

        binding.btnResume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimer();
                binding.btnPause.setVisibility(View.VISIBLE);
                binding.btnResume.setVisibility(View.GONE);
            }
        });

        binding.btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopTimer();
                model.resetTimeCounter(activity);
                startTimer();

                binding.btnResume.setVisibility(View.GONE);
                binding.btnPause.setVisibility(View.VISIBLE);
            }
        });

        startTimer();

        return binding.getRoot();
    }

    // TODO: move allTimeCount into a service, in order to avoid the fragment lifecycle impact.
    private void startTimer() {
        Log.d(TAG, "startTimer: ");

        if (timerTask == null) {

            timerTask = new TimerTask() {

                @Override
                public void run() {
                    Log.d(TAG, "run: timer count--: " +
                            String.valueOf(model.getTimeCounter().getValue()));

                    model.countdown();

                    // 每秒通知 Activity - TimerView 减一
                    handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);
                    if (model.getTimeCounter().getValue() <= 0) {
                        Log.d(TAG, "run: time is up!");

                        handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
                        stopTimer();
                    }
                }
            };
            timer.schedule(timerTask, 1000, 1000);
        }
    }

    private void stopTimer() {
        Log.d(TAG, "stopTimer: ");

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    // 因为 TimerTask在一个线程里面，需要使用Handler通知 Activity 来更新计时器UI
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_WHAT_TIME_TICK:
                    // 因为使用了Databinding，就不需要每秒手动更新UI了。
                    int timecount = model.getTimeCounter().getValue();
                    int alltime = model.getActivityAllTime().getValue();
                    int progress = timecount * 100 / alltime;
                    binding.progressCircular.setProgress(progress);
                    break;

                case MSG_WHAT_TIME_IS_UP:
                    mp = MediaPlayer.create(getContext(), R.raw.music);
                    mp.start();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.timeup_dialog_title);
                    builder.setMessage(R.string.timeup_dialog_message);
                    builder.setNegativeButton(R.string.dialog_quit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "AlertDialog.onCancel: ");
                            mp.stop();
                            mp.release();
                            NavController navController = Navigation.findNavController(getView());
                            navController.popBackStack();
                        }
                    });
                    builder.show();

                    binding.btnReset.setVisibility(View.VISIBLE);
                    binding.btnResume.setVisibility(View.GONE);
                    binding.btnPause.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        timer.cancel();
    }

}
