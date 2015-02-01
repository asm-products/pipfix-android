package com.pipfix.pipfix.tasks;

/**
 * Created by mativs on 01/02/15.
 */
import java.io.InputStreamReader;
import java.io.IOException;
import android.os.AsyncTask;
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
import org.apache.http.client.HttpClient;

import com.pipfix.pipfix.R;
import com.pipfix.pipfix.models.Stuff;
import com.pipfix.pipfix.utils.ListenableAsyncTask;

public class GetVoteTask extends ListenableAsyncTask<Void, Void, JSONObject> {

    private Stuff stuff;

    public GetVoteTask(Stuff newStuff) {
        stuff = newStuff;
    }

    private final String LOG_TAG = GetVoteTask.class.getSimpleName();

    @Override
    protected JSONObject doInBackground(Void... params) {
        HttpClient httpclient = new DefaultHttpClient();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority("www.pipfix.com")
                .appendPath("api")
                .appendPath("votes")
                .appendPath(stuff.getStuffId());

        String searchResultStr = "";
        HttpGet httpget = new HttpGet(builder.build().toString());
        try {
            // Add your data
            httpget.addHeader("Authorization" , "Token f48cca5812c4fb1c154c96a872ec539aa5154c6f");
            httpget.addHeader("Content-Type" , "application/json");

            HttpResponse response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 404) {
                Log.v(LOG_TAG, "404" + searchResultStr);
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            searchResultStr = buffer.toString();

            JSONObject searchResultJson = new JSONObject(searchResultStr);
            Log.v(LOG_TAG, "Stuff search string: " + searchResultStr);
            return searchResultJson;

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

}