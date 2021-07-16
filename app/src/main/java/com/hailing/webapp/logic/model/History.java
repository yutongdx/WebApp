package com.hailing.webapp.logic.model;

public class History {
    public String url;
    public String title;
    public String picture;

    public History() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public History(String url, String title, String picture) {
        this.url = url;
        this.title = title;
        this.picture = picture;
    }
}
