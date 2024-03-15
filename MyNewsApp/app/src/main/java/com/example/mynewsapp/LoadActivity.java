package com.example.mynewsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

// LoadActivity serves as a splash screen, displaying a loading UI before transitioning to the MainActivity.
public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the layout defined in activity_load.xml
        setContentView(R.layout.activity_load);

        // Handler to post a delayed task. This approach allows the splash screen to be displayed for a set amount of time.
        new Handler().postDelayed(() -> {
            // Intent to start MainActivity after the delay
            startActivity(new Intent(LoadActivity.this, MainActivity.class));
            // Finish the current activity to remove it from the back stack
            finish();
        }, 4000); // Delay time in milliseconds. Here, it is set to 4 seconds.
    }
}
