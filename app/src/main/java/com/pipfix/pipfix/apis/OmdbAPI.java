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
public class OmdbAPI {

    private final String LOG_TAG = OmdbAPI.class.getSimpleName();

    public Stuff getStuffDetails(String stuff_id) {
        HttpClient httpclient = new DefaultHttpClient();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority("www.omdbapi.com")
                .appendQueryParameter("i", stuff_id);

        String searchResultStr = "";
        HttpGet httpget = new HttpGet(builder.build().toString());
        try {
            // Add your data
            httpget.addHeader("Authorization" , "Token f48cca5812c4fb1c154c96a872ec539aa5154c6f");
            httpget.addHeader("Content-Type" , "application/json");

            HttpResponse response = httpclient.execute(httpget);

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
            Stuff stuff = new Stuff();
            stuff.setStuffId(stuff_id);
            stuff.setTitle(searchResultJson.getString("Title"));
            stuff.setDescription(searchResultJson.getString("Plot"));
            stuff.setImage(searchResultJson.getString("Poster"));
            stuff.setYear(searchResultJson.getInt("Year"));
            return stuff;

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
