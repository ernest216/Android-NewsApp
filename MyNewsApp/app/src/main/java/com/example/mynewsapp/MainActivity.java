package com.example.mynewsapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mynewsapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflating the layout for this activity using View Binding
        com.example.mynewsapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        // Setup NavController for handling navigation in the NavHostFragment
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_guide) {
            // Show the guide or tutorial
            showGuide();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showGuide() {
        // Display an AlertDialog as a guide with instructions on app usage
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("App Guide")
                .setMessage("Here is how to use the app: \n\n" +
                        "1. Navigate through the app using the bottom navigation bar to see latest news, other news in different categories and search news by keyword.\n" +
                        "2. Tap on an article to read more about it, perform a touch & hold on anywhere in the article to open the source in your default browser.\n" +
                        "3. Use the guide anytime for help.\n" +
                        "4. The news from the app are from JSON search results for current and historic news articles published by over 80,000 worldwide sources in newsapi.prg \n.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }







}