package com.example.mradmin.androidnewsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mradmin.androidnewsapp.Entities.NewsItem;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        NewsItem curNewsItem = (NewsItem) getIntent().getSerializableExtra("news_item");

        ImageView imageView = (ImageView) findViewById(R.id.imageViewNewsImageDetail);
        TextView textViewTitle = (TextView) findViewById(R.id.textViewNewsTitleDetail);
        TextView textViewDate = (TextView) findViewById(R.id.textViewNewsDateDetail);
        TextView textViewDescription = (TextView) findViewById(R.id.textViewNewsDescriptionDetail);

        if (curNewsItem.getImage() != null || !curNewsItem.getImage().isEmpty()){
            Picasso.with(NewsDetailActivity.this).load(curNewsItem.getImage()).placeholder(R.mipmap.ic_launcher).into(imageView);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
        }

        textViewTitle.setText(curNewsItem.getTitle());
        textViewDate.setText(curNewsItem.getDate());
        textViewDescription.setText(curNewsItem.getDescription());
    }
}
