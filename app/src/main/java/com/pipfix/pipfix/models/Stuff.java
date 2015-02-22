package com.pipfix.pipfix.models;

import android.net.Uri;
import android.util.Log;

import com.pipfix.pipfix.apis.PipfixAPI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
    private String user;
    private Float userAverage;
    private Float average;
    private List<Vote> votes;

    public Stuff() {}

    public Stuff(String newUser) {
        user = newUser;
    }

    public Float getUserAverage() { return userAverage; }

    public void setUserAverage(Float userAverage) { this.userAverage = userAverage; }

    public Float getAverage() { return average; }

    public void setAverage(Float average) { this.average = average;     }

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

    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }

    public List<Vote> getVotes() {
        if (votes == null) {
            votes = new ArrayList<Vote>();
        }
        return votes;
    }

    public void setVotes(List<Vote> votes) {this.votes = votes; }

    public void saveUserStuff() {
        if (stuff_id != null && user != null) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").authority("pipfix.herokuapp.com")
                        .appendPath("api")
                        .appendPath("users")
                        .appendPath(user)
                        .appendPath("user-stuff")
                        .appendPath(stuff_id);

                JSONObject json = new JSONObject();
                json.put("stuff", stuff_id);
                json.put("user", user);

                HttpPatch httppatch = new HttpPatch(builder.build().toString() + '/');
                httppatch.addHeader("Content-Type" , "application/json");
                httppatch.setEntity(new StringEntity( json.toString(), HTTP.UTF_8));

                HttpResponse response = httpclient.execute(httppatch);
                if (response.getStatusLine().getStatusCode() == 404) {
                    response.getEntity().consumeContent();
                    Uri.Builder post_builder = new Uri.Builder();
                    post_builder.scheme("http").authority("pipfix.herokuapp.com")
                            .appendPath("api")
                            .appendPath("users")
                            .appendPath(user)
                            .appendPath("user-stuff");
                    HttpPost httppost = new HttpPost(post_builder.build().toString() + '/');
                    httppost.addHeader("Content-Type" , "application/json");
                    httppost.setEntity(new StringEntity( json.toString(), HTTP.UTF_8));
                    response = httpclient.execute(httppost);
                }
                Log.v(LOG_TAG, "Termine de update user-stuff");
            } catch (JSONException e) {
                Log.v(LOG_TAG, "JSON " + e.toString());
            } catch (ClientProtocolException e) {
                Log.v(LOG_TAG, "POST ERROR " + e.toString());
            } catch (IOException e) {
                Log.v(LOG_TAG, "POST ERROR " + e.toString());
            }
        }
    }

    public void saveStuff() {
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
                httppatch.setEntity(new StringEntity( json.toString(), HTTP.UTF_8));

                HttpResponse response = httpclient.execute(httppatch);
                if (response.getStatusLine().getStatusCode() == 404) {
                    response.getEntity().consumeContent();
                    Uri.Builder post_builder = new Uri.Builder();
                    post_builder.scheme("http").authority("pipfix.herokuapp.com")
                            .appendPath("api")
                            .appendPath("stuff");
                    HttpPost httppost = new HttpPost(post_builder.build().toString() + '/');
                    httppost.addHeader("Content-Type" , "application/json");
                    httppost.setEntity(new StringEntity( json.toString(), HTTP.UTF_8));
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

    public void findUserAverage() {
        if (stuff_id != null && user != null) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").authority("pipfix.herokuapp.com")
                        .appendPath("api")
                        .appendPath("users")
                        .appendPath(user)
                        .appendPath("user-stuff")
                        .appendPath(stuff_id);

                HttpGet httpget = new HttpGet(builder.build().toString() + '/');

                httpget.addHeader("Content-Type" , "application/json");
                HttpResponse response = httpclient.execute(httpget);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() != 0) {
                    JSONObject searchResultJson = new JSONObject(buffer.toString());
                    setUserAverage((float)searchResultJson.getLong("average"));
                    setAverage((float)searchResultJson.getLong("global_average"));
                    JSONArray array = searchResultJson.getJSONArray("votes");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        Vote newVote = new Vote();
                        newVote.setPips(obj.getInt("pips"));
                        newVote.setUser(obj.getString("username"));
                        newVote.setComment(obj.getString("comment"));
                        newVote.setStuff_id(stuff_id);
                        getVotes().add(newVote);
                    }
                }

                Log.v(LOG_TAG, "Termine de update user-stuff");
            } catch (JSONException e) {
                Log.v(LOG_TAG, "JSON " + e.toString());
            } catch (ClientProtocolException e) {
                Log.v(LOG_TAG, "POST ERROR " + e.toString());
            } catch (IOException e) {
                Log.v(LOG_TAG, "POST ERROR " + e.toString());
            }
        }
    }

    public void findUserVote() {
        PipfixAPI api = new PipfixAPI();
        JSONObject object = api.getUserVotesForStuff(user, stuff_id);
        if (object != null) {
            try {
                setPips((Integer) object.get("pips"));
            } catch (JSONException e) {
                Log.v(LOG_TAG, "JSON " + e.toString());
            }
        }
    }

    public void save() {
        saveStuff();
        saveUserStuff();
    }

    @Override public String toString() {
        return title;
    }
}
