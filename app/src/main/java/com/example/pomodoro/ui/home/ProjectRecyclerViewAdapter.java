package com.example.pomodoro.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro.R;
import com.example.pomodoro.database.Project;
import com.example.pomodoro.viewModel.MainViewModel;

import timber.log.Timber;

public class ProjectRecyclerViewAdapter
        extends ListAdapter<Project, ProjectRecyclerViewAdapter.ViewHolder> {

    private MainViewModel viewModel;

    public ProjectRecyclerViewAdapter(MainViewModel viewModel) {
        super(new ProjectDiffCallback());
        this.viewModel = viewModel;
    }

    //Interface of Adapter
    public void removeItem(int position) {
        Project prj = getItem(position);
        viewModel.deleteProjects(prj);
        removeItem(position);
    }

    public void swapItem(int from, int to) {
        swapItem(from, to);
    }

    // Adjust priority of each project
    public void adjustPriority(){
        for (int i = 0; i < getItemCount(); i++) {
            Project prj = getItem(i);
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
                viewModel.setSelectedProject(holder.mItem);

                //Toast.makeText(v.getContext(), "你点击了Item: "+ prj.getTitle(), Toast.LENGTH_SHORT).show();
                Timber.d("onClick: ");

                // Navigate to NotificationFragment
                NavDirections action = HomeFragmentDirections.actionNavigationHomeToNavigationNotifications();
                Navigation.findNavController(v).navigate(action);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //Project project = mValues.get(position);
        Project project = getItem(position);
        holder.bind(project);
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

        public void bind(Project item) {
            mItem = item;
            mImageView.setImageResource(item.getImageId());
            mTitleView.setText(item.getTitle());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}