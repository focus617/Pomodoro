package com.example.pomodoro.ui.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.pomodoro.database.Project;

public class ProjectDiffCallback extends DiffUtil.ItemCallback<Project> {
    @Override
    public boolean areItemsTheSame(@NonNull Project oldItem, @NonNull Project newItem) {
        //help to discover if an item was added, removed, or moved.
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Project oldItem, @NonNull Project newItem) {
        //help to discover if an item has been updated
        return oldItem.getTitle().equals(newItem.getTitle());
    }
}
