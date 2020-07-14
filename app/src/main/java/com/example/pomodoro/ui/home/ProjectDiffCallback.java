package com.example.pomodoro.ui.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.pomodoro.database.Project;

public class ProjectDiffCallback extends DiffUtil.ItemCallback<Project> {
    @Override
    public boolean areItemsTheSame(@NonNull Project oldItem, @NonNull Project newItem) {
        //To change body of created functions use File | Settings | File Templates.
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Project oldItem, @NonNull Project newItem) {
        //To change body of created functions use File | Settings | File Templates.
        return oldItem.getTitle().equals(newItem.getTitle());
    }
}
