package com.example.pomodoro.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pomodoro.R;

public class NotificationsFragment extends Fragment {
    private static final String TAG = "NotificationsFragment";

    private NotificationsViewModel notificationsViewModel;
    private String prj;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        // 获取目标 homeFragment 传递的参数： project
        prj = NotificationsFragmentArgs.fromBundle(getArguments()).getProject();

        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d(TAG, "onChanged: project= "+prj);
                textView.setText(prj);
            }
        });
        return root;
    }
}