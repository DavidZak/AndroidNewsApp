package com.example.mradmin.androidnewsapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mradmin.androidnewsapp.Entities.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mrAdmin on 25.08.2017.
 */

public class NewsAdapter extends BaseAdapter {

    Context context;
    ArrayList<NewsItem> newsList;

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public NewsItem getItem(int i) {
        return newsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public NewsAdapter(Context context, ArrayList<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = View.inflate(context, R.layout.news_item_row, null);
        }

        NewsItem curNewsItem = newsList.get(i);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewNewsImage);
        TextView textViewTitle = (TextView) view.findViewById(R.id.textViewNewsTitle);
        TextView textViewDate = (TextView) view.findViewById(R.id.textViewNewsDate);
        TextView textViewDescription = (TextView) view.findViewById(R.id.textViewNewsDescription);

        if (curNewsItem.getImage() != null && !curNewsItem.getImage().isEmpty()){
            Picasso.with(context).load(curNewsItem.getImage()).placeholder(R.mipmap.ic_launcher).into(imageView);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
        }

        textViewTitle.setText(curNewsItem.getTitle());
        textViewDate.setText(curNewsItem.getDate());
        textViewDescription.setText(curNewsItem.getDescription());

        return view;
    }
}
