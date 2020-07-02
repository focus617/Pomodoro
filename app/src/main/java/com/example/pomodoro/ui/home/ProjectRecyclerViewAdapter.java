package com.example.pomodoro.ui.home;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pomodoro.R;
import com.example.pomodoro.viewModel.MainViewModel;
import com.example.pomodoro.viewModel.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectRecyclerViewAdapter extends RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "MyItemRecyclerViewAdapt";

    private MainViewModel viewModel;
    private List<Project> mValues = new ArrayList<>();  // 避免空指针

    public ProjectRecyclerViewAdapter(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    //Interface of Adapter
    public void setProjectList(List<Project> mValues) {
        this.mValues = mValues;

        // Update the UI when source changed.
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        Project prj = mValues.get(position);
        viewModel.deleteProjects(prj);
        notifyItemRemoved(position);
    }

    public void swapItem(int from, int to) {
        Collections.swap(mValues, from, to);
        notifyItemMoved(from, to);
    }

    // Adjust priority of each project
    public void adjustPriority(){
        for (int i = 0; i < mValues.size(); i++) {
            Project prj = mValues.get(i);
            prj.setPriority(i+1);
            viewModel.updateProjects(prj);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_project, parent, false);
                //创建卡片  参数:  布局，组件组，是否是根节点

        final ViewHolder holder = new ViewHolder(itemView);

        //添加点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Project prj = mValues.get(position);
                Toast.makeText(v.getContext(), "你点击了Item: "+ prj.getTitle(), Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onClick: ");

                // 从目标 ItemFragment 向 定时器 NotificationFragment 传递参数：项目
                HomeFragmentDirections.ActionNavigationHomeToNavigationNotifications action =
                        HomeFragmentDirections.actionNavigationHomeToNavigationNotifications().setProjectId(prj.getId());
                Navigation.findNavController(v).navigate(action);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mImageView.setImageResource(mValues.get(position).getImageId());
        holder.mTitleView.setText(mValues.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTitleView;
        public Project mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.item_image);
            mTitleView = (TextView) view.findViewById(R.id.item_title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}