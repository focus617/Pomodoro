package com.example.pomodoro.ui.notifications;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.pomodoro.R;
import com.example.pomodoro.viewModel.Activity;
import com.example.pomodoro.viewModel.MainViewModel;
import com.example.pomodoro.viewModel.Project;

import java.util.Calendar;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private static final String TAG = "NotificationsFragment";
    private static final String PROJECT_KEY = "Project_Key";


    private int prjId;
    Project project;
    Activity activity;
    private long startTimeStamp, endTimeStamp;

    private Button btnStart, btnPause, btnResume, btnReset;
    private EditText etHour, etMin, etSec;

    private MainViewModel model;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Get the ViewModel.
        model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        //Get current project
        project = model.getCurrentProject().getValue();

        Toast.makeText(getActivity(), "Current Project: " + project.getTitle(), Toast.LENGTH_SHORT).show();

        // Create the observer which updates the UI.
        final Observer<Project> observer = new Observer<Project>() {
            @Override
            public void onChanged(@Nullable Project project) {
                project = model.getCurrentProject().getValue();
                Log.d(TAG, "onChanged: project= " + project.getTitle());
                //TODO: adjust corresponding actionlist
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.getCurrentProject().observe(this, observer);
        activity = model.getCurrentActivity().getValue();

        btnStart = (Button) root.findViewById(R.id.btnStart);
        btnPause = (Button) root.findViewById(R.id.btnPause);
        btnResume = (Button) root.findViewById(R.id.btnResume);
        btnReset = (Button) root.findViewById(R.id.btnReset);
        btnPause.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int allTime = Integer.parseInt(etHour.getText().toString()) * 60
                        * 60 + Integer.parseInt(etMin.getText().toString()) * 60
                        + Integer.parseInt(etSec.getText().toString());

                activity.setAllTime(allTime);

                startCountDownTimer();
            }
        });

        etHour = (EditText) root.findViewById(R.id.etHour);
        etMin = (EditText) root.findViewById(R.id.etMin);
        etSec = (EditText) root.findViewById(R.id.etSec);

        etHour.setText("00");
        etHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*
                 * 这个方法是在Text改变过程中触发调用的， 它的意思就是说在原有的文本中，
                 * 从start开始的count个字符替换长度为before的旧文本，
                 * 注意这里没有将要之类的字眼，也就是说一句执行了替换动作。
                 */
                if (!TextUtils.isEmpty(s)) {

                    int value = Integer.parseInt(s.toString());

                    if (value > 59) {
                        etHour.setText("59");
                    } else if (value < 0) {
                        etHour.setText("00");
                    }
                }
                checkToEnableBtnStart();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etMin.setText("00");
        etMin.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {

                    int value = Integer.parseInt(s.toString());

                    if (value > 59) {
                        etMin.setText("59");
                    } else if (value < 0) {
                        etMin.setText("00");
                    }
                }
                checkToEnableBtnStart();
            }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        etSec.setText("00");
        etSec.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {

                    int value = Integer.parseInt(s.toString());

                    if (value > 59) {
                        etSec.setText("59");
                    } else if (value < 0) {
                        etSec.setText("00");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnStart.setVisibility(View.VISIBLE);
        btnStart.setEnabled(false);

        return root;

    }

    // 将目标 Project 进行保存
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PROJECT_KEY, prjId);
    }

    // 检查时分秒数据的有效性，若有效就启用
    private void checkToEnableBtnStart() {
        btnStart.setEnabled((!TextUtils.isEmpty(etHour.getText()) && Integer
                .parseInt(etHour.getText().toString()) > 0)
                || (!TextUtils.isEmpty(etMin.getText()) && Integer
                .parseInt(etMin.getText().toString()) > 0)
                || (!TextUtils.isEmpty(etSec.getText()) && Integer
                .parseInt(etSec.getText().toString()) > 0));
    }

    private void startCountDownTimer() {
        Calendar currentTime = Calendar.getInstance();
        startTimeStamp = currentTime.getTimeInMillis();

        NavDirections action = NotificationsFragmentDirections.actionNavigationNotificationsToNavigationCountdown();
        Navigation.findNavController(btnStart).navigate(action);

        model.resetTimeCounter(activity);   // 初始化TimeCounter
    }

    //TODO: 在此增加“添加 ActivityRecord”的功能
    public void endCountDownTimer() {
        Calendar currentTime = Calendar.getInstance();
        endTimeStamp = currentTime.getTimeInMillis();
    }
}