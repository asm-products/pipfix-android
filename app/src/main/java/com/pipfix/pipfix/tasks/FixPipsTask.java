package com.pipfix.pipfix.tasks;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import org.json.JSONException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.ClientProtocolException;
import android.util.Log;
import org.json.JSONObject;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import android.net.Uri;

import com.pipfix.pipfix.R;
import com.pipfix.pipfix.models.Stuff;
import com.pipfix.pipfix.utils.ListenableAsyncTask;

public class FixPipsTask extends ListenableAsyncTask<String, Void, String> {

    private final String LOG_TAG = FixPipsTask.class.getSimpleName();

    private Stuff stuff;
    private String user;
    private Integer pips;
    private String comment;

    public FixPipsTask(Stuff newStuff, String newUser, Integer newPips, String newComment) {
        stuff = newStuff;
        user = newUser;
        pips = newPips;
        comment = newComment;

    }

    private HttpPatch getHttpatch() throws JSONException, IOException{
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority("pipfix.herokuapp.com")
                .appendPath("api")
                .appendPath("votes")
                .appendPath(stuff.getStuffId());

        HttpPatch httppatch = new HttpPatch(builder.build().toString() + '/');

        JSONObject json = new JSONObject();
        json.put("pips", pips);
        json.put("user", user);
        json.put("comment", comment);

        StringEntity se = new StringEntity( json.toString());
        httppatch.setEntity(se);
        //httppatch.addHeader("Authorization" , "Token f48cca5812c4fb1c154c96a872ec539aa5154c6f");
        httppatch.addHeader("Content-Type" , "application/json");
        Log.v(LOG_TAG, "Responseeeee " + httppatch.toString());
        return httppatch;
    }

    private HttpPost getHttpPost() throws JSONException, IOException{
        JSONObject json = new JSONObject();
        json.put("stuff_id", stuff.getStuffId());
        json.put("pips", pips);
        json.put("user", user);
        json.put("comment", comment);

        StringEntity se = new StringEntity( json.toString());
        HttpPost httppost = new HttpPost("http://pipfix.herokuapp.com/api/votes/");
        httppost.setEntity(se);

        //httppost.addHeader("Authorization" , "Token f48cca5812c4fb1c154c96a872ec539aa5154c6f");
        httppost.addHeader("Content-Type" , "application/json");
        Log.v(LOG_TAG, "Responseeeee " + httppost.toString());
        return httppost;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            // Add your data
            HttpResponse response = null;
            if (stuff.getPips() != null) {
                response = httpclient.execute(getHttpatch());
            } else {
                response = httpclient.execute(getHttpPost());
            }
            Log.v(LOG_TAG, "Responseeeee " + EntityUtils.toString(response.getEntity()));

        } catch (JSONException e) {
            Log.v(LOG_TAG, "JSON " + e.toString());
        } catch (ClientProtocolException e) {
            Log.v(LOG_TAG, "POST ERROR " + e.toString());
        } catch (IOException e) {
            Log.v(LOG_TAG, "POST ERROR " + e.toString());
        }

        return null;
    }

}
