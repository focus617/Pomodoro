package com.example.pomodoro.ui.notifications;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pomodoro.R;

import java.util.Timer;
import java.util.TimerTask;

public class CountDownFragment extends Fragment {
    private static final String TAG = "CountDownFragment";

    private static final int MSG_WHAT_TIME_IS_UP = 1;
    private static final int MSG_WHAT_TIME_TICK = 2;

    private int allTime, allTimeCount = 0;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private Button btnStart, btnPause, btnResume, btnReset;
    private ProgressBar prgbar;
    private TextView etHour, etMin, etSec;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allTime = CountDownFragmentArgs.fromBundle(getArguments()).getAllTime();
        Log.d(TAG, String.format("onCreateView: allTime=%d",allTime));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_countdown, container, false);

        Toast.makeText(getActivity(), "CountDown Fragment", Toast.LENGTH_SHORT).show();

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
                allTimeCount = allTime;
                int hour = allTimeCount / 60 / 60;
                int min = (allTimeCount / 60) % 60;
                int sec = allTimeCount % 60;

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

        allTimeCount = allTime;
        int hour = allTimeCount / 60 / 60;
        int min = (allTimeCount / 60) % 60;
        int sec = allTimeCount % 60;

        etHour.setText(hour + "");
        etMin.setText(min + "");
        etSec.setText(sec + "");

        startTimer();

        return root;
    }

    private void startTimer() {
        Log.d(TAG, "startTimer: ");

        if (timerTask == null) {

            timerTask = new TimerTask() {

                @Override
                public void run() {
                    Log.d(TAG, "run: timer count--");

                    allTimeCount--;

                    // 每秒通知 Activity - TimerView 减一
                    handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);
                    if (allTimeCount <= 0) {
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
                    int hour = allTimeCount / 60 / 60;
                    int min = (allTimeCount / 60) % 60;
                    int sec = allTimeCount % 60;

                    etHour.setText(hour + "");
                    etMin.setText(min + "");
                    etSec.setText(sec + "");
                    break;

                case MSG_WHAT_TIME_IS_UP:
                    new AlertDialog.Builder(getContext())
                            .setTitle("Time is up!")
                            .setMessage("Time is up!")
                            .setNegativeButton("Cancel", null).show();

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
