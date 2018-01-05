package com.codegym.downloadfile;

/**
 * Created by Han on 1/4/2018.
 */

public class Song {
    private String name;
    private String url;

    public Song(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
