package com.pipfix.pipfix.models;

/**
 * Created by mativs on 21/02/15.
 */
public class Vote {

    private String user;
    private String stuff_id;
    private String comment;
    private Integer pips;

    public Vote() {};

    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }

    public String getStuff_id() { return stuff_id; }

    public void setStuff_id(String stuff_id) { this.stuff_id = stuff_id; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

    public Integer getPips() { return pips; }

    public void setPips(Integer pips) { this.pips = pips; }
}
