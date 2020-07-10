package com.example.pomodoro;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pomodoro.ui.home.HomeFragmentDirections;
import com.example.pomodoro.ui.notifications.NotificationsFragmentDirections;
import com.example.pomodoro.viewModel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MainViewModel mModel;    // ViewModel for whole activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_dashboard)
                .build();
        Log.d(TAG, "onCreate: " + appBarConfiguration.toString());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        ActionBar actionBar = getSupportActionBar();

        // Get the ViewModel.
        mModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    public boolean onSupportNavigateUp() {

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        /* if quit from CountdownFragment */
        if (navController.getCurrentDestination().getId() == R.id.navigation_countdown) {
            Log.d(TAG, "onSupportNavigateUp: nav_countdown");
            /* 如果当前定时器尚未到期 */
            if (mModel.getTimeCounter().getValue() != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.quit_dialog_title));
                builder.setPositiveButton(R.string.dialog_positive_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navController.popBackStack();
                    }
                });
                builder.setNegativeButton(R.string.dialog_negative_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            /* 如果正常到期 */
            else {
                Log.d(TAG, "onSupportNavigateUp: normal exit");
                navController.popBackStack();
            }
        } else {
            Log.d(TAG, "onSupportNavigateUp: other nav ");
            navController.navigateUp();
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        /* if quit from CountdownFragment */
        if (navController.getCurrentDestination().getId() == R.id.navigation_countdown) {
            Log.d(TAG, "onBackPressed: nav_countdown");
            /* 如果当前定时器尚未到期 */
            if (mModel.getTimeCounter().getValue() != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.quit_dialog_title));
                builder.setPositiveButton(R.string.dialog_positive_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navController.popBackStack();
                    }
                });
                builder.setNegativeButton(R.string.dialog_negative_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            /* 如果正常到期 */
            else {
                Log.d(TAG, "onBackPressed: normal exit");
                super.onBackPressed();
            }
        } else {
            Log.d(TAG, "onBackPressed: other nav ");
            super.onBackPressed();
        }

    }
}