package com.example.mynewsapp.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynewsapp.NewsRecyclerAdapter;
import com.example.mynewsapp.R;
import com.example.mynewsapp.databinding.FragmentNotificationsBinding;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

// Fragment class for displaying news based on user's search and showing search history
public class NotificationsFragment extends Fragment {

    // Binding instance for accessing layout views
    private FragmentNotificationsBinding binding;
    // RecyclerView for displaying search results
    private RecyclerView recyclerView;
    // ListView for displaying search history
    private ListView listViewSearchHistory;
    private NewsRecyclerAdapter adapter;
    // List to hold Article objects fetched from the API
    private List<Article> articles = new ArrayList<>();
    // List to hold search history strings
    private List<String> searchHistoryList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize RecyclerView and ListView from the layout
        recyclerView = root.findViewById(R.id.news_recycler_view);
        listViewSearchHistory = root.findViewById(R.id.search_history_list_view);
        // Setting up RecyclerView layout manager and adapter
        setRecyclerView();
        // Configure the SearchView
        setupSearchView();

        return root;
    }

    // Configure SearchView to handle search queries and focus changes
    private void setupSearchView() {
        SearchView searchView = binding.searchView;
        // Listener for query text submit event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Save the search query, perform search, and hide search history
                saveSearchQuery(query);
                searchNews(query);
                listViewSearchHistory.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optionally handle text change for real-time search/filter
                return false;
            }
        });

        // Listener for SearchView focus to display or hide search history
        searchView.setOnQueryTextFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                displaySearchHistory();
            } else {
                listViewSearchHistory.setVisibility(View.GONE);
            }
        });
    }

    // Initialize RecyclerView with a LinearLayoutManager and adapter
    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsRecyclerAdapter(getContext(), articles);
        recyclerView.setAdapter(adapter);
    }

    // Save each search query to SharedPreferences for persistence across sessions
    @SuppressLint("MutatingSharedPrefs")
    private void saveSearchQuery(String query) {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        // Retrieve the current search history, ensuring a new HashSet is created to avoid direct modification
        Set<String> currentSearchHistory = new HashSet<>(sharedPreferences.getStringSet("searchHistory", new HashSet<>()));
        // Add the new query
        currentSearchHistory.add(query);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("searchHistory", currentSearchHistory);
        editor.apply();
        displaySearchHistory(); // Refresh the search history display
    }

    // Display the search history in a ListView below the SearchView
    private void displaySearchHistory() {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        // Retrieve the search history as a new HashSet to ensure immutability is not a concern
        Set<String> searchHistory = new HashSet<>(sharedPreferences.getStringSet("searchHistory", new HashSet<>()));
        searchHistoryList.clear();
        searchHistoryList.addAll(searchHistory);
        ArrayAdapter<String> searchHistoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>(searchHistoryList));
        listViewSearchHistory.setAdapter(searchHistoryAdapter);
        listViewSearchHistory.setVisibility(View.VISIBLE);

        listViewSearchHistory.setOnItemClickListener((parent, view, position, id) -> {
            String query = searchHistoryList.get(position);
            binding.searchView.setQuery(query, true); // Perform the search with the selected query
            searchNews(query); // Don't forget to perform the search
            listViewSearchHistory.setVisibility(View.GONE); // Optionally hide the list after a selection
        });
    }


    // Fetch news articles based on the search query
    private void searchNews(String query) {
        // Instantiating the NewsApiClient with my API key
        NewsApiClient newsApiClient = new NewsApiClient("a0f5290878314ff089658d35413c4b35");
        // Requesting top headlines from the United States in English
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .language("en")
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
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
                        // Handle the error
                        Log.i("GOT Failure", throwable.getMessage());
                    }
                }
        );
    }

    // Clean up when the view is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leak
    }
}