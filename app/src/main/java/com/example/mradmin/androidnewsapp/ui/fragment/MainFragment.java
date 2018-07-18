package com.example.mradmin.androidnewsapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mradmin.androidnewsapp.R;
import com.example.mradmin.androidnewsapp.entity.NewsItem;
import com.example.mradmin.androidnewsapp.ui.activity.NewsDetailActivity;
import com.example.mradmin.androidnewsapp.ui.adapter.NewsAdapter;
import com.example.mradmin.androidnewsapp.util.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yksoft on 17.07.18.
 */

public class MainFragment extends Fragment implements NewsAdapter.ListenerAdapter {

    public static final String TAG = "MainFragment";
    public static final String BUNDLE_URL = "bundleUrl";
    public static final String BUNDLE_TITLE = "bundleTitle";

    private Unbinder unbinder;

    @BindView(R.id.ll_progress)
    LinearLayout llProgress;
    @BindView(R.id.ll_error)
    LinearLayout llError;
    @BindView(R.id.newsListView)
    RecyclerView newsListView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<NewsItem> newsList;
    private NewsAdapter newsAdapter;

    private String url = null;
    private String title = null;

    public static MainFragment newInstance(String url, String title) {

        Bundle args = new Bundle();
        args.putString(BUNDLE_URL, url);
        args.putString(BUNDLE_TITLE, title);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        unbinder = ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null){
            if (args.getString(BUNDLE_URL) != null)
                url = args.getString(BUNDLE_URL);
            if (args.getString(BUNDLE_TITLE) != null)
                title = args.getString(BUNDLE_TITLE);
        }

        initAdapter();
        requestVolley(url);

        swipeRefreshLayout.setOnRefreshListener(() -> requestVolley(url));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    private void requestVolley(String url) {
        Util.setViewVisibility(llProgress, View.VISIBLE);
        Util.setViewVisibility(llError, View.GONE);

        //Parsing
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                /*Document document = Jsoup.parse(response);

                Elements itemElements = document.getElementsByTag("item");

                for (Element itemElement : itemElements) {
                    String title = itemElement.child(0).text();
//                        title = new String(itemElement.child(0).text().getBytes("cp1252"), "UTF-8");

                    String description = itemElement.child(1).text();
                    String link = itemElement.child(2).text();
                    String guid = itemElement.child(3).text();
                    String date = itemElement.child(4).text();
                    String image = itemElement.getElementsByTag("enclosure").attr("url");

                    NewsItem newsItem = new NewsItem(title, description, link, image, date);

                    newsList.add(newsItem);
                }*/

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = null;
                try {
                    dBuilder = dbFactory.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                try {
                    Document doc = dBuilder.parse(Util.convertStringToDocument(response));
                    doc.getDocumentElement().normalize();

                    NodeList itemElements = doc.getElementsByTagName("item");

                    for (int i = 0;i < itemElements.getLength(); i++) {
                        Element itemElement = (Element) itemElements.item(i);
                        String title = new String(itemElement.getElementsByTagName("title").item(0).getTextContent().getBytes("ISO-8859-1"), "UTF-8");
                        String description = new String(itemElement.getElementsByTagName("description").item(0).getTextContent().getBytes("ISO-8859-1"), "UTF-8");
                        description = Html.fromHtml(description).toString();
                        String link = new String(itemElement.getElementsByTagName("link").item(0).getTextContent().getBytes("ISO-8859-1"), "UTF-8");
                        String guid = null;
                        if (checkItemIsNull(itemElement, guid))
                            guid = new String(itemElement.getElementsByTagName("guid").item(0).getTextContent().getBytes("ISO-8859-1"), "UTF-8");
                        String date = new String(itemElement.getElementsByTagName("pubDate").item(0).getTextContent().getBytes("ISO-8859-1"), "UTF-8");

                        String image = null;
                        if (checkItemIsNull(itemElement, "enclosure"))
                            image = itemElement.getElementsByTagName("enclosure").item(0).getAttributes().item(0).getTextContent();

                        NewsItem newsItem = new NewsItem(title, description, link, image, date);

                        newsList.add(newsItem);
                    }
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                newsAdapter.setItems(newsList);

                Util.setSwipeRefreshRefreshing(swipeRefreshLayout, false);
                Util.setViewVisibility(llProgress, View.GONE);
                Util.setViewVisibility(llError, View.GONE);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.setSwipeRefreshRefreshing(swipeRefreshLayout, false);
                Util.setViewVisibility(llProgress, View.GONE);
                Util.setViewVisibility(llError, View.VISIBLE);
            }
        });

        requestQueue.add(request);
    }

    private void initAdapter(){
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        newsListView.setLayoutManager(linearLayoutManager);

        newsListView.setHasFixedSize(true);
        newsListView.setAdapter(newsAdapter);
    }

    private boolean checkItemIsNull(Element element, String tagName){
        if (element.getElementsByTagName(tagName) == null || element.getElementsByTagName(tagName).getLength() == 0){
            return false;
        }
        if (element.getElementsByTagName(tagName).item(0) == null)
            return false;
        return true;
    }

    @Override
    public void clickItem(NewsItem newsItem) {
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra("news_item", newsItem);
        intent.putExtra("news_title", title);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}
