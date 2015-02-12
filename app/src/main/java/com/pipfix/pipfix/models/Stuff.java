package com.pipfix.pipfix.models;

import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by mativs on 01/02/15.
 */
public class Stuff {

    private final String LOG_TAG = Stuff.class.getSimpleName();

    private String title;
    private String stuff_id;
    private String image;
    private String description;
    private Integer year;
    private Integer pips;

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

    public void setPips(Integer newPips){
        pips = newPips;
    }

    public Integer getPips() {
        return pips;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Integer getYear() { return year; }

    public void setYear(Integer year) { this.year = year; }

    public void update() {
        if (stuff_id != null) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").authority("pipfix.herokuapp.com")
                        .appendPath("api")
                        .appendPath("stuff")
                        .appendPath(stuff_id);

                JSONObject json = new JSONObject();
                json.put("stuff_id", stuff_id);
                json.put("title", title);
                json.put("year", year);
                json.put("image", image);
                json.put("description", description);

                HttpPatch httppatch = new HttpPatch(builder.build().toString() + '/');
                httppatch.addHeader("Content-Type" , "application/json");
                httppatch.setEntity(new StringEntity( json.toString()));

                HttpResponse response = httpclient.execute(httppatch);
                if (response.getStatusLine().getStatusCode() == 404) {
                    response.getEntity().getContent();
                    Uri.Builder post_builder = new Uri.Builder();
                    post_builder.scheme("http").authority("pipfix.herokuapp.com")
                            .appendPath("api")
                            .appendPath("stuff");
                    HttpPost httppost = new HttpPost(post_builder.build().toString() + '/');
                    httppost.addHeader("Content-Type" , "application/json");
                    httppost.setEntity(new StringEntity( json.toString()));
                    response = httpclient.execute(httppost);
                }
            } catch (JSONException e) {
                Log.v(LOG_TAG, "JSON " + e.toString());
            } catch (ClientProtocolException e) {
                Log.v(LOG_TAG, "POST ERROR " + e.toString());
            } catch (IOException e) {
                Log.v(LOG_TAG, "POST ERROR " + e.toString());
            }
        }
    }

    @Override public String toString() {
        return title;
    }
}
