package com.example.pomodoro.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pomodoro.R;

public class CountDownFragment extends Fragment {
    private static final String TAG = "CountDownFragment";

    private EditText etHour, etMin, etSec;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        Toast.makeText(getActivity(), "CountDown Fragment is showing now", Toast.LENGTH_SHORT).show();

        etHour = (EditText) root.findViewById(R.id.etHour);
        etMin = (EditText) root.findViewById(R.id.etMin);
        etSec = (EditText) root.findViewById(R.id.etSec);

        etHour.setText("00");
        etMin.setText("00");
        etSec.setText("00");
        return root;
    }
}
