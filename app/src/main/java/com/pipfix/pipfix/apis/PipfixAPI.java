package com.pipfix.pipfix.apis;

import android.net.Uri;
import android.util.Log;

import com.pipfix.pipfix.models.Stuff;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by mativs on 21/02/15.
 */
public class PipfixAPI {
    private final String LOG_TAG = PipfixAPI.class.getSimpleName();

    public JSONObject getUserVotesForStuff(String user, String stuff_id) {
        HttpClient httpclient = new DefaultHttpClient();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority("pipfix.herokuapp.com")
                .appendPath("api")
                .appendPath("users")
                .appendPath(user)
                .appendPath("votes")
                .appendPath(stuff_id);

        String searchResultStr = "";
        HttpGet httpget = new HttpGet(builder.build().toString());
        try {
            // Add your data
            //httpget.addHeader("Authorization" , "Token f48cca5812c4fb1c154c96a872ec539aa5154c6f");
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

            return new JSONObject(searchResultStr);

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
