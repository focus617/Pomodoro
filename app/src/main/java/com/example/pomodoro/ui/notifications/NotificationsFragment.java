package com.example.pomodoro.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro.R;
import com.example.pomodoro.databinding.FragmentNotificationsBinding;
import com.example.pomodoro.viewModel.Activity;
import com.example.pomodoro.viewModel.MainViewModel;
import com.example.pomodoro.viewModel.Project;

import java.util.Calendar;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private static final String TAG = "NotificationsFragment";

    private Project project;
    private Activity activity;
    private long startTimeStamp, endTimeStamp;

    private MainViewModel model;
    private FragmentNotificationsBinding binding;
    private ActivityRecyclerViewAdapter adapter; //Adapter of recyclerView for activities

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the ViewModel.
        model = new ViewModelProvider(requireActivity(),
                new SavedStateViewModelFactory(requireActivity().getApplication(),this))
                .get(MainViewModel.class);

        // create adapter for RecyclerView
        adapter = new ActivityRecyclerViewAdapter(model);

        // Create the observer which updates the UI.
        final Observer<List<Activity>> observer = new Observer<List<Activity>>() {
            @Override
            public void onChanged(@Nullable List<Activity> activityList) {
                adapter.setActivityList(activityList);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.getActListLive().observe(this, observer);

        //Get current project
        project = model.getSelectedProject().getValue();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Databinding
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notifications,container, false);
        binding.setModel(model);
        binding.setLifecycleOwner(requireActivity());

        Toast.makeText(getActivity(), String.format("Current Project:"+project.getTitle()), Toast.LENGTH_SHORT).show();

        /* 构造 Activity 的 RecycleView */
        // Set the layoutManager
        Context context = binding.getRoot().getContext();
         binding.lvActivity.setLayoutManager(new LinearLayoutManager(context,
                 LinearLayoutManager.HORIZONTAL,false));
        // Set the adapter
        binding.lvActivity.setAdapter(adapter);

        // 创建ItemTouchHelper.Callback，实现回调方法
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            // 返回允许滑动的方向
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                // 返回可滑动方向，通过使用一个int，在各个bit位标记来记录。
                // 这里drag支持上下方向，swipe支持左右方向。
                int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                // 返回设置了标识位的复合int
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            // 允许drag的前提下，当ItemTouchHelper想要将拖动的项目从其旧位置移动到新位置时调用
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // 获取被拖拽item和目标item的适配器索引（适配器索引是该item对应数据集的索引，getLayoutPosition是当前布局的位置）
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                // 交换数据集的数据
                adapter.swapItem(from, to);

                // 通知Adapter更新，此动作应是Adapter的内生逻辑
                //adapter.notifyItemMoved(from, to);

                // 返回true表示item移到了目标位置
                return true;
            }

            // 允许swipe的前提下，当用户滑动ViewHolder触发临界时调用
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // 获取滑动的item对应的适配器索引
                int pos = viewHolder.getAdapterPosition();
                // 从数据集移除数据
                adapter.removeItem(pos);

                // 通知Adapter更新，此动作应是Adapter的内生逻辑
                //adapter.notifyItemRemoved(pos);
            }

            //当用户操作完毕后，记录项目清单的顺序
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                adapter.adjustPriority();
            }
        };
        // 传入ItemTouchHelper.Callback
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        // 将touchHelper和recyclerView绑定
        touchHelper.attachToRecyclerView(binding.lvActivity);

        activity = model.getSelectedActivity().getValue();
        model.activityTotalTime.setValue(activity.getTotalTime());
        Log.d(TAG, "onCreateView: AllTime="+String.valueOf(model.activityTotalTime.getValue()));

        binding.btnPause.setVisibility(View.GONE);
        binding.btnReset.setVisibility(View.GONE);
        binding.btnResume.setVisibility(View.GONE);

        binding.btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int allTime = Integer.parseInt(binding.etHour.getText().toString()) * 60
                        * 60 + Integer.parseInt(binding.etMin.getText().toString()) * 60
                        + Integer.parseInt(binding.etSec.getText().toString());

                /* 修订定时值 */
                activity.setTotalTime(allTime);
                model.activityTotalTime.setValue(allTime);

                startCountDownTimer();
            }
        });

        binding.etHour.setText("00");
        binding.etHour.addTextChangedListener(new TextWatcher() {
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
                        binding.etHour.setText("59");
                    } else if (value < 0) {
                        binding.etHour.setText("00");
                    }
                }
                checkToEnableBtnStart();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etMin.setText("00");
        binding.etMin.addTextChangedListener(new TextWatcher() {

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
                        binding.etMin.setText("59");
                    } else if (value < 0) {
                        binding.etMin.setText("00");
                    }
                }
                checkToEnableBtnStart();
            }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        binding.etSec.setText("00");
        binding.etSec.addTextChangedListener(new TextWatcher() {

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
                        binding.etSec.setText("59");
                    } else if (value < 0) {
                        binding.etSec.setText("00");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.btnStart.setVisibility(View.VISIBLE);
        binding.btnStart.setEnabled(false);

        return binding.getRoot();

    }

    // 检查时分秒数据的有效性，若有效就启用
    private void checkToEnableBtnStart() {
        binding.btnStart.setEnabled((!TextUtils.isEmpty(binding.etHour.getText()) && Integer
                .parseInt(binding.etHour.getText().toString()) > 0)
                || (!TextUtils.isEmpty(binding.etMin.getText()) && Integer
                .parseInt(binding.etMin.getText().toString()) > 0)
                || (!TextUtils.isEmpty(binding.etSec.getText()) && Integer
                .parseInt(binding.etSec.getText().toString()) > 0));
    }

    private void startCountDownTimer() {
        Calendar currentTime = Calendar.getInstance();
        startTimeStamp = currentTime.getTimeInMillis();

        NavDirections action = NotificationsFragmentDirections.actionNavigationNotificationsToNavigationCountdown();
        Navigation.findNavController(binding.btnStart).navigate(action);

        model.resetTimeCounter(activity);   // 初始化TimeCounter

    }

    //TODO: 在此增加“添加 ActivityRecord”的功能
    public void endCountDownTimer() {
        Calendar currentTime = Calendar.getInstance();
        endTimeStamp = currentTime.getTimeInMillis();
    }
}