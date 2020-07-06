package com.example.pomodoro.ui.notifications;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro.R;
import com.example.pomodoro.viewModel.Activity;
import com.example.pomodoro.viewModel.MainViewModel;
import com.example.pomodoro.viewModel.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityRecyclerViewAdapter extends RecyclerView.Adapter<ActivityRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ActRecyclerViewAdapter";

    private MainViewModel viewModel;
    private List<Activity> mValues = new ArrayList<>();  // 避免空指针

    public ActivityRecyclerViewAdapter(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }


    //Interface of Adapter
    public void setActivityList(List<Activity> mValues) {
        this.mValues = mValues;

        // Update the UI when source changed.
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        Activity act = mValues.get(position);
        viewModel.deleteActivities(act);
        notifyItemRemoved(position);
    }

    public void swapItem(int from, int to) {
        Collections.swap(mValues, from, to);
        notifyItemMoved(from, to);
    }

    // Adjust priority of each project
    public void adjustPriority() {
        for (int i = 0; i < mValues.size(); i++) {
            Activity act = mValues.get(i);
            act.setPriority(i + 1);
            viewModel.updateActivities(act);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_activity, parent, false);
        //创建Activity卡片  参数:  布局，组件组，是否是根节点

        final ActivityRecyclerViewAdapter.ViewHolder holder = new ActivityRecyclerViewAdapter.ViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mButton.setText(holder.getText());
        holder.mAdapter = this;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final Button mButton;
        public Activity mItem;
        public ActivityRecyclerViewAdapter mAdapter;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            view.setOnClickListener(this);
            mButton = (Button) view.findViewById(R.id.btnAction);
            mButton.setOnClickListener(this);
        }

        public String getText() {
            String title = mItem.getTitle();
            String titleLabel = (title.length()>6)? title.substring(0,5)+"...":title;
            int totalTime = mItem.getTotalTime();

            String timeLabel = (totalTime/60/60 > 0)? String.format("%02d:", totalTime/60/60):"";
            timeLabel += String.format("%02d:", (totalTime/60)%60)
                       + String.format("%02d", totalTime % 60);

            Log.d(TAG, "getText: "+titleLabel + "\n" + timeLabel);
            return titleLabel + "\n" + timeLabel;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.getTitle() + "'";
        }

        @Override
        public void onClick(View v) {      //添加点击事件
            mAdapter.viewModel.setSelectedActivity(mItem);

            //Toast.makeText(v.getContext(), "你点击了Item: " + mItem.getTitle(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onClick: ");

        }
    }
}