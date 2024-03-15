package com.example.mynewsapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsActivity extends AppCompatActivity {

    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Retrieve the URL passed to this Activity
        String url = getIntent().getStringExtra("url");

        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        // Register the WebView for context menu
        registerForContextMenu(webView);

        // Handle the back press to navigate back in WebView or finish the activity
        final OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // If WebView can go back, consume the back press and navigate back
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    // Otherwise, disable this callback and invoke the back press again
                    setEnabled(false);
                    NewsActivity.super.onBackPressed();
                }
            }
        };
        // Register the callback
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // Inflate the context menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // Handling selection of context menu item
        if (item.getItemId() == R.id.open_web_page) {
            // Find the WebView and get its current URL
            WebView webView = findViewById(R.id.web_view);
            String url = webView.getUrl();
            // Create an intent to view the URL in a browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
        return super.onContextItemSelected(item);
    }


}
