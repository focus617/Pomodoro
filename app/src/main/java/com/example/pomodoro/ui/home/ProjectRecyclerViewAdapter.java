package com.example.pomodoro.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro.database.Project;
import com.example.pomodoro.databinding.ListItemProjectBinding;
import com.example.pomodoro.viewModel.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

public class ProjectRecyclerViewAdapter extends RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder> {

    private MainViewModel viewModel;
    private List<Project> mValues = new ArrayList<>();  // 避免空指针

    public ProjectRecyclerViewAdapter(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    //Interface of Adapter
    public void setProjectList(List<Project> datas) {
        mValues = datas;

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

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建卡片  参数:  布局，组件组，是否是根节点
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position), this);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProjectRecyclerViewAdapter mAdapter;
        private ListItemProjectBinding mBinding;
        private Project mItem;

        private ViewHolder(ListItemProjectBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.mBinding.getRoot().setOnClickListener(this);
        }

        @NotNull
        public static ProjectRecyclerViewAdapter.ViewHolder from(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(ListItemProjectBinding.inflate(layoutInflater, parent, false));
        }

        public void bind(Project item, ProjectRecyclerViewAdapter adapter) {
            mAdapter = adapter;
            mItem = item;
            mBinding.setProject(item);
            mBinding.executePendingBindings();
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mItem.getTitle() + "'";
        }

        //添加点击事件
        public void onClick(View v) {
            mAdapter.viewModel.setSelectedProject(mItem);
            //Toast.makeText(v.getContext(), "你点击了Item: "+ mItem.getTitle(), Toast.LENGTH_SHORT).show();
            Timber.d("onClick: %s", mItem.getTitle());

            // Navigate to NotificationFragment
            NavDirections action = HomeFragmentDirections.actionNavigationHomeToNavigationNotifications();
            Navigation.findNavController(v).navigate(action);

        }

    }
}

