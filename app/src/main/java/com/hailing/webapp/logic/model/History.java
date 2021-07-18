package com.hailing.webapp.logic.model;

public class History {
    private int id;
    private String url;
    private String title;
    private String icon;
    private String time;

    public History() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public History(int id,String url, String title, String icon, String time) {
        this.id=id;
        this.url = url;
        this.title = title;
        this.icon = icon;
        this.time = time;
    }
}
