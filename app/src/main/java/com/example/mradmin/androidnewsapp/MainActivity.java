package com.example.mradmin.androidnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mradmin.androidnewsapp.Entities.NewsItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "https://life.ru/xml/feed.xml";

    private ArrayList<NewsItem> newsList;
    private ListView newsListView;
    private NewsAdapter newsAdapter;

    private TextView mTextMessage;

    private static int navSelector = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_new:
                    //mTextMessage.setText(R.string.title_home);
                    navSelector = 0;
                    return true;
                case R.id.navigation_top:
                    //mTextMessage.setText(R.string.title_dashboard);
                    navSelector = 1;
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.app_name);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        newsList = new ArrayList<>();
        newsListView = (ListView) findViewById(R.id.newsListView);

        //Parsing
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Document document = Jsoup.parse(response);
                Elements itemElements = document.getElementsByTag("item");

                for (Element itemElement : itemElements) {

                    String title = itemElement.child(0).text();
                    String description = itemElement.child(1).text();
                    String link = itemElement.child(2).text();
                    String guid = itemElement.child(3).text();
                    String date = itemElement.child(4).text();
                    String image = itemElement.getElementsByTag("enclosure").attr("url");

                    NewsItem newsItem = new NewsItem(title, description, link, image, date);

                    newsList.add(newsItem);
                }

                newsAdapter = new NewsAdapter(MainActivity.this, newsList);
                newsListView.setAdapter(newsAdapter);
                newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
                        intent.putExtra("news_item", newsList.get(i));
                        startActivity(intent);

                    }
                });

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(request);


    }

}
