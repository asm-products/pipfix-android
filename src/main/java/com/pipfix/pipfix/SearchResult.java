package com.pipfix.pipfix;

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
