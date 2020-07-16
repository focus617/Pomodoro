package com.example.pomodoro;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pomodoro.databinding.ActivityMainBinding;
import com.example.pomodoro.ui.countdowntimer.CountDownViewModel;
import com.example.pomodoro.viewModel.MainViewModel;

import java.util.Objects;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MainViewModel mModel;           // ViewModel for whole activity
    private CountDownViewModel mCountDownViewModel;
    private ActivityMainBinding mBinding;   // Binding for main activity

    private DrawerLayout mDrawerLayout;     // Navigation Drawer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the ViewModel.
        mModel = new ViewModelProvider(this).get(MainViewModel.class);
        // Get the Fragment ViewModel.
        mCountDownViewModel =  new ViewModelProvider(this).get(CountDownViewModel.class);

        //Databinding
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mDrawerLayout = mBinding.drawerLayout;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_dashboard)
                .build();
        Timber.d("onCreate: %s", appBarConfiguration.toString());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupActionBarWithNavController(this, navController, mDrawerLayout);
        NavigationUI.setupWithNavController(mBinding.navView, navController);
        NavigationUI.setupWithNavController(mBinding.navDrawerView,navController);

    }

    @Override
    public boolean onSupportNavigateUp() {

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        /* if quit from CountdownFragment */
        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.navigation_countdown) {
            Timber.d("onSupportNavigateUp: nav_countdown");
            /* 如果当前定时器尚未到期 */
            if (!mCountDownViewModel.getEventTimeUp().getValue()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.quit_dialog_title));
                builder.setPositiveButton(R.string.dialog_positive_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCountDownViewModel.onStopTimer();
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
                Timber.d("onSupportNavigateUp: normal exit");
                navController.popBackStack();
            }
        } else {
            Timber.d("onSupportNavigateUp: other nav ");
            //navController.navigateUp();
            NavigationUI.navigateUp(navController, mDrawerLayout);
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        /* if quit from CountdownFragment */
        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.navigation_countdown) {
            Timber.d("onBackPressed: nav_countdown");
            /* 如果当前定时器尚未到期 */
            if (!mCountDownViewModel.getEventTimeUp().getValue()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.quit_dialog_title));
                builder.setPositiveButton(R.string.dialog_positive_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCountDownViewModel.onStopTimer();
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
                Timber.d("onBackPressed: normal exit");
                super.onBackPressed();
            }
        } else {
            Timber.d("onBackPressed: other nav ");
            super.onBackPressed();
        }

    }
}