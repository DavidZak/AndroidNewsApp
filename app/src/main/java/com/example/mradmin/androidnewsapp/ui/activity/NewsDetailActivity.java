package com.example.mradmin.androidnewsapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mradmin.androidnewsapp.R;
import com.example.mradmin.androidnewsapp.entity.NewsItem;
import com.example.mradmin.androidnewsapp.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailActivity extends AppCompatActivity {

    @BindView(R.id.imageViewNewsImageDetail)
    ImageView imageView;
    @BindView(R.id.textViewNewsTitleDetail)
    TextView textViewTitle;
    @BindView(R.id.textViewNewsDateDetail)
    TextView textViewDate;
    @BindView(R.id.textViewNewsDescriptionDetail)
    TextView textViewDescription;

    NewsItem curNewsItem = null;
    String title = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        ButterKnife.bind(this);

        curNewsItem = (NewsItem) getIntent().getSerializableExtra("news_item");
        title = getIntent().getStringExtra("news_title");

        if (curNewsItem == null){
            finish();
        } else {
            if (curNewsItem.getImage() != null && !curNewsItem.getImage().isEmpty()) {
                Picasso.with(this).load(curNewsItem.getImage()).placeholder(R.mipmap.ic_launcher).into(imageView);
            } else {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }

            textViewTitle.setText(curNewsItem.getTitle());
            textViewDate.setText(curNewsItem.getDate());
            textViewDescription.setText(curNewsItem.getDescription());
        }

        setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_detail_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_share: {
                shareNewsLink(curNewsItem.getLink());
                return true;
            }
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareNewsLink(String link){
        if (link == null){
            Util.showAnchoredPopUpView(this, findViewById(R.id.item_share), R.layout.refresh_info_popup, R.string.link_not_exists);
            return;
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
