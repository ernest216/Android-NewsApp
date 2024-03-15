package com.example.mynewsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;
import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    List<Article> articles;
    Context context;

    // Constructor
    public NewsRecyclerAdapter(Context context, List<Article> articles) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_row, parent, false);
        // Return a new holder instance
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articles.get(position);
        // Set article title and source name
        holder.newsHeadline.setText(article.getTitle());
        holder.articleSource.setText(article.getSource().getName());
        // Load the article image using Picasso
        Picasso.get().load(article.getUrlToImage())
                .error(R.drawable.no_image) // Placeholder in case of an error
                .placeholder(R.drawable.no_image) // Placeholder while the image loads
                .into(holder.articleImageView);

        holder.itemView.setOnClickListener(v -> {
            // Correctly create a new Intent instance for NewsActivity
            Intent intent = new Intent(v.getContext(), NewsActivity.class);
            // Call putExtra on the intent instance, passing in the URL
            intent.putExtra("url", article.getUrl());
            // Use the context from the view to start the activity
            v.getContext().startActivity(intent);
        });

    }

    // Method to return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return articles.size();
    }

    // Defines the ViewHolder class for the RecyclerView
    static class NewsViewHolder extends RecyclerView.ViewHolder {
        // Define image view and text views to hold the article image, headline, and source respectively.
        ImageView articleImageView;
        TextView newsHeadline, articleSource;

        // Constructor for the ViewHolder, receives a single itemView which represents the entire row
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImageView = itemView.findViewById(R.id.article_image_view);
            newsHeadline = itemView.findViewById(R.id.news_headline);
            articleSource = itemView.findViewById(R.id.article_source);
        }
    }
}