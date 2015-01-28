package com.pipfix.pipfix.tasks;

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

public class GetStuffTask extends AsyncTask<String, Void, String> {

    private View rootView;

    public GetStuffTask(View view) {
        this.rootView = view;
    }

    private final String LOG_TAG = GetStuffTask.class.getSimpleName();

    @Override
    protected String doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        HttpClient httpclient = new DefaultHttpClient();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority("www.omdbapi.com")
            .appendQueryParameter("i", params[0]);

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
            Log.v(LOG_TAG, "Stuff search string: " + searchResultStr);
            return searchResultJson.toString();

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
            ((TextView) rootView.findViewById(R.id.stuff_details_text))
                .setText(result);
        }
    }
}