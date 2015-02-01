package com.pipfix.pipfix.models;

/**
 * Created by mativs on 01/02/15.
 */
public class SearchResult {

    public String title;
    public String stuff_id;

    public String getTitle(){
        return title;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public String getStuffId() {
        return stuff_id;
    }

    public void setStuffId(String newStuffId) {
        stuff_id = newStuffId;
    }

    @Override public String toString() {
        return title;
    }
}