package com.amr.sinnerschraderparsingtask.data.models;

/**
 * Created by Amr El-Madah on 11/27/2017.
 */

public class LinkModel {
    private String url;
    private String title;

    public LinkModel(String title, String url) {
        this.title = title;
        this.url = url;
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

    @Override
    public String toString() {
        return "Link [title=" + title + ",url = " + url + "]";
    }
}
