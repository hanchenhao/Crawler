package com.github.hanchenhao;

public class News {
    String url;
    String title;
    String source;
    String contents;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public News(String url, String title, String source, String contents) {
        this.url = url;
        this.title = title;
        this.source = source;
        this.contents = contents;
    }
}
