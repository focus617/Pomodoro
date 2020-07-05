package com.example.pomodoro;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.Toast;

import com.example.pomodoro.viewModel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MainViewModel model;    // ViewModel for whole activity

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
        Log.d(TAG, "onCreate: "+ appBarConfiguration.toString());
        
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        ActionBar actionBar = getSupportActionBar();

        // Get the ViewModel.
        model = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    public boolean onSupportNavigateUp() {

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        /* if quit from CountdownFragment */
        if(navController.getCurrentDestination().getId() == R.id.navigation_countdown){
            /* 如果当前定时器尚未到期 */
            if(model.getTimeCounter().getValue()!=0){
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
            else navController.popBackStack();
        } else {
            navController.popBackStack();
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为ActionBar扩展菜单项
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                /**
                 * {@link RecyclerView.Adapter} Create dummy project list that can display a {@link DummyItem}.
                 * TODO: Clean it in future and Replace the implementation with code for your data type.
                 */
                model.createDummyPrjList();
                return true;

            case R.id.action_about:
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();

                // Clear database for testing purpose
                // TODO: Clean in future
                model.deleteAllProjects();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}