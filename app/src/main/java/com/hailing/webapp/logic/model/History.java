package com.hailing.webapp.logic.model;

public class History {
    public String url;
    public String title;
    public String icon;
    public String time;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public History(String url, String title, String icon, String time) {
        this.url = url;
        this.title = title;
        this.icon = icon;
        this.time = time;
    }
}