package com.example.mradmin.androidnewsapp.Entities;

import java.io.Serializable;
import java.net.URLEncoder;

/**
 * Created by mrAdmin on 25.08.2017.
 */

public class NewsItem implements Serializable{

    private String title;
    private String description;
    private String link;
    private String image;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public NewsItem(String title, String description, String link, String image, String date) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.image = image;
        this.date = date;
    }

    public NewsItem() {
    }

    @Override
    public String toString() {
        return title;
    }
}
