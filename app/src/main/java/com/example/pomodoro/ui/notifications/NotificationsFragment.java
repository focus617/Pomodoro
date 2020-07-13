package com.example.pomodoro.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.example.pomodoro.database.Activity;
import com.example.pomodoro.viewModel.CountDownViewModel;
import com.example.pomodoro.viewModel.MainViewModel;
import com.example.pomodoro.database.Project;

import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

public class NotificationsFragment extends Fragment {

    private Project project;
    private Activity activity;
    private long startTimeStamp, endTimeStamp;

    private MainViewModel mModel;
    private FragmentNotificationsBinding mBinding;
    private CountDownViewModel mCountDownViewModel;
    private ActivityRecyclerViewAdapter mAdapter; //Adapter of recyclerView for activities
    private RecyclerView.LayoutManager mLayoutManager; // Layoutmanagher of recyclerview for activities

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the ViewModel.
        mModel = new ViewModelProvider(requireActivity(),
                new SavedStateViewModelFactory(requireActivity().getApplication(),this))
                .get(MainViewModel.class);

        // Get the Fragment ViewModel.
        mCountDownViewModel =  new ViewModelProvider(requireActivity(),
                new SavedStateViewModelFactory(requireActivity().getApplication(),this))
                .get(CountDownViewModel.class);

        // create adapter for RecyclerView
        mAdapter = new ActivityRecyclerViewAdapter(mModel);

        //Get current project
        project = mModel.getSelectedProject().getValue();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Create the observer which updates the UI.
        final Observer<List<Activity>> observer = new Observer<List<Activity>>() {
            @Override
            public void onChanged(@Nullable List<Activity> activityList) {
                mAdapter.setActivityList(activityList);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mModel.getActListLive().observe(getViewLifecycleOwner(), observer);

        // Databinding
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_notifications,container, false);
        mBinding.setModel(mModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        //Toast.makeText(getActivity(), String.format("Current Project:"+project.getTitle()), Toast.LENGTH_SHORT).show();

        /* 构造 Activity 的 RecycleView */
        // Set the layoutManager
        Context context = mBinding.getRoot().getContext();
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
        mBinding.lvActivity.setLayoutManager(mLayoutManager);
        // Set the adapter
        mBinding.lvActivity.setAdapter(mAdapter);

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
                mAdapter.swapItem(from, to);

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
                mAdapter.removeItem(pos);

                // 通知Adapter更新，此动作应是Adapter的内生逻辑
                //adapter.notifyItemRemoved(pos);
            }

            //当用户操作完毕后，记录项目清单的顺序
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                mAdapter.adjustPriority();
            }
        };
        // 传入ItemTouchHelper.Callback
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        // 将touchHelper和recyclerView绑定
        touchHelper.attachToRecyclerView(mBinding.lvActivity);

        activity = mModel.getSelectedActivity().getValue();
        Timber.d("ActTotalTime="+mModel.getActivityTotalTime().getValue());


        mBinding.btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                long totalTime = Integer.parseInt(mBinding.etHour.getText().toString()) * 60
                        * 60 + Integer.parseInt(mBinding.etMin.getText().toString()) * 60
                        + Integer.parseInt(mBinding.etSec.getText().toString());

                /* 修订定时值 */
                activity.setTotalTime(totalTime);
                mModel.getActivityTotalTime().setValue(totalTime);

                startCountDownTimer();
            }
        });

        mBinding.etHour.setText("00");
        mBinding.etHour.addTextChangedListener(new TextWatcher() {
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
                        mBinding.etHour.setText("59");
                    } else if (value < 0) {
                        mBinding.etHour.setText("00");
                    }
                }
                checkToEnableBtnStart();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBinding.etMin.setText("00");
        mBinding.etMin.addTextChangedListener(new TextWatcher() {

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
                        mBinding.etMin.setText("59");
                    } else if (value < 0) {
                        mBinding.etMin.setText("00");
                    }
                }
                checkToEnableBtnStart();
            }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        mBinding.etSec.setText("00");
        mBinding.etSec.addTextChangedListener(new TextWatcher() {

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
                        mBinding.etSec.setText("59");
                    } else if (value < 0) {
                        mBinding.etSec.setText("00");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mBinding.btnStart.setVisibility(View.VISIBLE);
        mBinding.btnStart.setEnabled(false);

        return mBinding.getRoot();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.d("onSaveInstanceState Called");
    }

    // 检查时分秒数据的有效性，若有效就启用
    private void checkToEnableBtnStart() {
        mBinding.btnStart.setEnabled((!TextUtils.isEmpty(mBinding.etHour.getText()) && Integer
                .parseInt(mBinding.etHour.getText().toString()) > 0)
                || (!TextUtils.isEmpty(mBinding.etMin.getText()) && Integer
                .parseInt(mBinding.etMin.getText().toString()) > 0)
                || (!TextUtils.isEmpty(mBinding.etSec.getText()) && Integer
                .parseInt(mBinding.etSec.getText().toString()) > 0));
    }

    private void startCountDownTimer() {
        Calendar currentTime = Calendar.getInstance();
        startTimeStamp = currentTime.getTimeInMillis();

        NavDirections action = NotificationsFragmentDirections.actionNavigationNotificationsToNavigationCountdown();
        Navigation.findNavController(mBinding.btnStart).navigate(action);
    }

    //TODO: 在此增加“添加 ActivityRecord”的功能
    public void endCountDownTimer() {
        Calendar currentTime = Calendar.getInstance();
        endTimeStamp = currentTime.getTimeInMillis();
    }
}