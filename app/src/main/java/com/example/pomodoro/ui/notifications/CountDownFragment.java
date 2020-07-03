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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro.R;
import com.example.pomodoro.viewModel.Activity;
import com.example.pomodoro.viewModel.MainViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class CountDownFragment extends Fragment {
    private static final String TAG = "CountDownFragment";

    private static final int MSG_WHAT_TIME_IS_UP = 1;
    private static final int MSG_WHAT_TIME_TICK = 2;

    private Activity activity;
    private int allTime;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private Button btnStart, btnPause, btnResume, btnReset;
    private ProgressBar prgbar;
    private TextView etHour, etMin, etSec;
    private MediaPlayer mp;

    private MainViewModel model;
    private LifeObserverCountDownFg observer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the ViewModel.
        model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        //Get current activity
        activity = model.getCurrentActivity().getValue();
        allTime = activity.getAllTime();

        // 增加一个lifecycle Observer
        observer = new LifeObserverCountDownFg();
        getLifecycle().addObserver(observer);

        Log.d(TAG, String.format("onCreateView: allTime=%d", allTime));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_countdown, container, false);

        model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        Toast.makeText(getActivity(), String.format("CountDown Fragment:%d",model.allTimeCount), Toast.LENGTH_SHORT).show();

        btnStart = (Button) root.findViewById(R.id.btnStart);
        btnPause = (Button) root.findViewById(R.id.btnPause);
        btnResume = (Button) root.findViewById(R.id.btnResume);
        btnReset = (Button) root.findViewById(R.id.btnReset);
        btnStart.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.VISIBLE);
        btnResume.setVisibility(View.GONE);

        btnPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopTimer();
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimer();
                btnPause.setVisibility(View.VISIBLE);
                btnResume.setVisibility(View.GONE);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopTimer();
                model.allTimeCount = allTime;
                int hour = model.allTimeCount / 60 / 60;
                int min = (model.allTimeCount / 60) % 60;
                int sec = model.allTimeCount % 60;

                etHour.setText(hour + "");
                etMin.setText(min + "");
                etSec.setText(sec + "");
                startTimer();

                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
            }
        });

        etHour = (TextView) root.findViewById(R.id.etHour);
        etMin = (TextView) root.findViewById(R.id.etMin);
        etSec = (TextView) root.findViewById(R.id.etSec);

        int hour = model.allTimeCount / 60 / 60;
        int min = (model.allTimeCount / 60) % 60;
        int sec = model.allTimeCount % 60;

        etHour.setText(hour + "");
        etMin.setText(min + "");
        etSec.setText(sec + "");

        startTimer();

        return root;
    }

    // TODO: move allTimeCount into a service, in order to avoid the fragment lifecycle impact.
    private void startTimer() {
        Log.d(TAG, "startTimer: ");

        if (timerTask == null) {

            timerTask = new TimerTask() {

                @Override
                public void run() {
                    Log.d(TAG, "run: timer count--");

                    model.allTimeCount--;

                    // 每秒通知 Activity - TimerView 减一
                    handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);
                    if (model.allTimeCount <= 0) {
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

        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_WHAT_TIME_TICK:
                    int hour = model.allTimeCount / 60 / 60;
                    int min = (model.allTimeCount / 60) % 60;
                    int sec = model.allTimeCount % 60;

                    etHour.setText(hour + "");
                    etMin.setText(min + "");
                    etSec.setText(sec + "");
                    break;

                case MSG_WHAT_TIME_IS_UP:
                    mp = MediaPlayer.create(getContext(), R.raw.music);
                    mp.start();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("定时器");
                    builder.setMessage("闹钟时间到了!");
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "AlertDialog.onCancel: ");
                            mp.stop();
                            mp.release();
                        }
                    });
                    builder.show();

                    btnReset.setVisibility(View.VISIBLE);
                    btnResume.setVisibility(View.GONE);
                    btnPause.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        timer.cancel();
    }

}
