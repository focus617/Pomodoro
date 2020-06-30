package com.example.pomodoro;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pomodoro.viewModel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
    final int COUNT = 15;           // No of Dummy projects for testing purpose

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
                 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
                 * TODO: Replace the implementation with code for your data type.
                 */
                // Create dummy project for testing
                model.deleteAllProjects();
                model.createDummyItems(COUNT);
                return true;

            case R.id.action_about:
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                return true;

            default:
                Toast.makeText(this, "You clicked backward", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                navController.popBackStack();
                return super.onOptionsItemSelected(item);
        }
    }
}