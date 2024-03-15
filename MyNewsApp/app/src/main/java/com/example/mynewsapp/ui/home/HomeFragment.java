package com.example.mynewsapp.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynewsapp.NewsRecyclerAdapter;
import com.example.mynewsapp.databinding.FragmentHomeBinding;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

// Fragment class for displaying the latest news articles
public class HomeFragment extends Fragment {

    // Binding instance for accessing the layout's views
    private FragmentHomeBinding binding;
    // RecyclerView for displaying news articles
    private RecyclerView recyclerView;
    // List to hold Article objects fetched from the API
    private List<Article> articles = new ArrayList<>();
    private NewsRecyclerAdapter adapter;


    // onCreateView callback to inflate the layout and setup UI components
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        // Inflating the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Binding text view from the layout
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Initialize recyclerView
        recyclerView = binding.newsRecyclerView;
        // Setup RecyclerView
        setRecyclerView();

        // Fetch News
        getNews();

        return root;
    }

    // Sets up the RecyclerView with a LinearLayoutManager and adapter
    void setRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsRecyclerAdapter(getContext(), articles);
        recyclerView.setAdapter(adapter);
    }

    // Fetches the latest news articles from the API
    void getNews(){
        // Instantiating the NewsApiClient with my API key
        NewsApiClient newsApiClient = new NewsApiClient("a0f5290878314ff089658d35413c4b35");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .country("us")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback(){
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        // Ensuring getActivity() is not null to avoid NullPointerException
                        if (getActivity() != null) {
                            // Running UI updates on the main thread
                            getActivity().runOnUiThread(() -> {
                                articles.clear();
                                articles.addAll(response.getArticles());
                                // Notifying the adapter to refresh the RecyclerView
                                adapter.notifyDataSetChanged();
                            });
                        }
                    }
                    @Override
                    public void onFailure(Throwable throwable) {
                        // Logging failure message
                        Log.i("GOT Failure", throwable.getMessage());
                    }
                }

        );
    }

    // Cleans up resources and references when the view is being destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullifying the binding object to prevent memory leaks
        binding = null;
    }
}