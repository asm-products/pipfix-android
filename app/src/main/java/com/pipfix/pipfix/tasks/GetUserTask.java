package com.pipfix.pipfix.tasks;

/**
 * Created by mativs on 01/02/15.
 */
import java.io.InputStreamReader;
import java.io.IOException;
import android.os.AsyncTask;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import android.widget.TextView;
import java.io.BufferedReader;
import android.net.Uri;
import android.util.Log;
import java.net.URL;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import android.view.View;
import java.io.InputStream;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;

import com.google.gson.Gson;
import com.pipfix.pipfix.LoginActivity;
import com.pipfix.pipfix.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import twitter4j.IDs;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class GetUserTask extends AsyncTask<Void, Void, String> {

    private LoginActivity loginActivity;
    private static final String TWITTER_KEY = "vLerXCrMiAxzRbJd6l45SIEmm";
    private static final String TWITTER_SECRET = "sd9ZnSeVyXmN5f5Rkj5fay75KnpzijYudOpISJS6a09EYohc8G";

    public GetUserTask(LoginActivity activity) {
        this.loginActivity = activity;
    }

    private final String LOG_TAG = GetUserTask.class.getSimpleName();

    private ArrayList<Long> getFriends() {
        ArrayList<Long> s = new ArrayList<Long>();

        try {
            TwitterSession session = Twitter.getSessionManager().getActiveSession();
            TwitterAuthToken authToken = session.getAuthToken();
            ConfigurationBuilder cb = new ConfigurationBuilder();

            cb.setOAuthConsumerKey(TWITTER_KEY)
                    .setOAuthConsumerSecret(TWITTER_SECRET)
                    .setOAuthAccessToken(authToken.token)
                    .setOAuthAccessTokenSecret(authToken.secret);
            TwitterFactory tf = new TwitterFactory(cb.build());
            twitter4j.Twitter twitter = tf.getInstance();
            long cursor = -1;
            IDs ids;
            do {
                ids = twitter.getFollowersIDs(cursor);
                for (long id : ids.getIDs()) {
                    s.add(id);
                }
            } while ((cursor = ids.getNextCursor()) != 0);
        } catch (twitter4j.TwitterException te) {
            Log.v(LOG_TAG, "Cannot connect to twitter " + te.toString());

        }
        return s;
    }

    private HttpGet getHttpGet(String... params) throws JSONException, IOException{
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority("pipfix.herokuapp.com")
                .appendPath("api")
                .appendPath("users")
                .appendPath(String.valueOf(session.getUserId()));
        HttpGet httpget = new HttpGet(builder.build().toString());
        httpget.addHeader("Content-Type" , "application/json");
        return httpget;
    }

    private HttpPost getHttpPost(String... params) throws JSONException, IOException{
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        Gson gson = new Gson();
        JSONObject json = new JSONObject();
        json.put("username", session.getUserName());
        json.put("twitter_id", String.valueOf(session.getUserId()));
        json.put("followed", new JSONArray(getFriends()));
        Log.v(LOG_TAG, "User " + json.toString());
        StringEntity se = new StringEntity( json.toString());
        HttpPost httppost = new HttpPost("http://pipfix.herokuapp.com/api/users/");
        httppost.setEntity(se);
        httppost.addHeader("Content-Type" , "application/json");
        return httppost;
    }

    protected JSONObject getJSON(HttpResponse response) throws IOException, JSONException{
        String searchResultStr = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
            Log.v(LOG_TAG, "Buffer empty");
            return null;
        }
        searchResultStr = buffer.toString();

        return new JSONObject(searchResultStr);
    }

    @Override
    protected String doInBackground(Void... params) {

        HttpClient httpclient = new DefaultHttpClient();

        try {
            HttpResponse response = httpclient.execute(getHttpGet());
            Log.v(LOG_TAG, "Status Code: " + String.valueOf(response.getStatusLine().getStatusCode()));
            if (response.getStatusLine().getStatusCode() == 404) {
                EntityUtils.toString(response.getEntity());
                HttpResponse response_post = httpclient.execute(getHttpPost());
                return getJSON(response_post).toString();
            } else {
                return getJSON(response).toString();
            }

        } catch (JSONException e) {
            Log.v(LOG_TAG, "JSON " + e.toString());
            // Log.e(LOG_TAG, e.getMessage(), e);
            // e.printStackTrace();
        } catch (ClientProtocolException e) {
            Log.v(LOG_TAG, "POST ERROR " + e.toString());
        } catch (IOException e) {
            Log.v(LOG_TAG, "POST ERROR " + e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            loginActivity.openMain();
        }
    }
}