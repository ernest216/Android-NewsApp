package com.example.mynewsapp.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynewsapp.NewsRecyclerAdapter;
import com.example.mynewsapp.R;
import com.example.mynewsapp.databinding.FragmentDashboardBinding;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import java.util.ArrayList;
import java.util.List;

// Fragment class that displays news articles in various categories
public class DashboardFragment extends Fragment implements View.OnClickListener {

    // Binding instance for accessing the layout's views
    private FragmentDashboardBinding binding;
    // RecyclerView for displaying news articles
    private RecyclerView recyclerView;
    // List to hold Article objects fetched from the API
    private List<Article> articles = new ArrayList<>();
    private NewsRecyclerAdapter adapter;
    // Buttons for selecting news categories
    Button btnBusiness, btnEntertainment, btnHealth, btnScience, btnSports, btnTechnology;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        new ViewModelProvider(this).get(DashboardViewModel.class);

        // Inflating the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initializing RecyclerView and category buttons
        recyclerView = binding.newsRecyclerView;

        btnBusiness = binding.btn1;
        btnEntertainment = binding.btn2;
        btnHealth = binding.btn3;
        btnScience = binding.btn4;
        btnSports = binding.btn5;
        btnTechnology = binding.btn6;

        btnBusiness.setOnClickListener(this);
        btnEntertainment.setOnClickListener(this);
        btnHealth.setOnClickListener(this);
        btnScience.setOnClickListener(this);
        btnSports.setOnClickListener(this);
        btnTechnology.setOnClickListener(this);

        // Setup RecyclerView
        setRecyclerView();

        // Fetch News for the first category as default
        getNews("business");

        return root;
    }

    // Method to configure the RecyclerView
    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsRecyclerAdapter(getContext(), articles);
        recyclerView.setAdapter(adapter);
    }

    // Method to fetch news articles for a specific category
    private void getNews(String category) {
        // Create an instance of NewsApiClient with the API key
        NewsApiClient newsApiClient = new NewsApiClient("a0f5290878314ff089658d35413c4b35");
        // Request top headlines for the specified category
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .category(category.toLowerCase()) // Category is set here
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        if (getActivity() != null) {
                            // Run UI updates on the main thread
                            getActivity().runOnUiThread(() -> {
                                articles.clear();
                                articles.addAll(response.getArticles());
                                // Notify the adapter to refresh the UI
                                adapter.notifyDataSetChanged();
                            });
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Handle the error
                        Log.i("GOT Failure", throwable.getMessage());
                    }
                }
        );
    }

    // Clean up resources and references when the view is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Method to handle category button clicks
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        String category;
        // Determine which button was clicked and set the appropriate category
        switch (view.getId()) {
            case R.id.btn_1:
                category = "business";
                break;
            case R.id.btn_2:
                category = "entertainment";
                break;
            case R.id.btn_3:
                category = "health";
                break;
            case R.id.btn_4:
                category = "science";
                break;
            case R.id.btn_5:
                category = "sports";
                break;
            case R.id.btn_6:
                category = "technology";
                break;
            default:
                category = "general";
        }
        getNews(category);
    }
}
