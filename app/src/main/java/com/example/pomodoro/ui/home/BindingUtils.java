package com.example.pomodoro.ui.home;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.example.pomodoro.database.Project;

public class BindingUtils {
    @BindingAdapter("projectTitle")
    public static void setProjectTitle(TextView view, Project item){
        view.setText(item.getTitle());
    }

    @BindingAdapter("projectImage")
    public static void setProjectImage(ImageView view, Project item){
        view.setImageResource(item.getImageId());
    }
}
