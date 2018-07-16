package com.example.mradmin.androidnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mradmin.androidnewsapp.entity.NewsItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Parsing
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
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
                    Document doc = dBuilder.parse(convertStringToDocument(response));
                    doc.getDocumentElement().normalize();

                    System.out.println("--------------------Root element : " + doc.getDocumentElement().getNodeName());

                    NodeList itemElements = doc.getElementsByTagName("item");

                    for (int i = 0;i < itemElements.getLength(); i++) {
                        Element itemElement = (Element) itemElements.item(i);
                        String title = new String(itemElement.getElementsByTagName("title").item(0).getTextContent().getBytes("cp1252"), "UTF-8");
                        String description = new String(itemElement.getElementsByTagName("description").item(0).getTextContent().getBytes("cp1252"), "UTF-8");
                        String link = new String(itemElement.getElementsByTagName("link").item(0).getTextContent().getBytes("cp1252"), "UTF-8");
                        String guid = new String(itemElement.getElementsByTagName("guid").item(0).getTextContent().getBytes("cp1252"), "UTF-8");
                        String date = new String(itemElement.getElementsByTagName("pubDate").item(0).getTextContent().getBytes("cp1252"), "UTF-8");

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

    private static InputStream convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Source xmlSource = new DOMSource(doc);
            Result outputTarget = new StreamResult(outputStream);
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
            InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
            return is;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkItemIsNull(Element element, String tagName){
        if (element.getElementsByTagName(tagName) == null && element.getElementsByTagName(tagName).getLength() == 0){
            return false;
        }
        if (element.getElementsByTagName(tagName).item(0) == null)
            return false;
        return true;
    }

}
