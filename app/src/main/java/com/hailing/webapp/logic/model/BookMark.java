package com.hailing.webapp.logic.model;

public class BookMark {
    private int id;
    private String icon;
    private String title;
    private String url;

    public BookMark() {

    }

    public BookMark(int id, String icon, String title, String url){
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
