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
import com.pipfix.pipfix.apis.OmdbAPI;
import com.pipfix.pipfix.models.Stuff;
import com.pipfix.pipfix.utils.ListenableAsyncTask;

public class GetStuffTask extends ListenableAsyncTask<String, Void, Stuff> {

    private String user;

    public GetStuffTask(String user) {
        this.user = user;
    }

    private final String LOG_TAG = GetStuffTask.class.getSimpleName();

    @Override
    protected Stuff doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        OmdbAPI omdb_api = new OmdbAPI();
        Stuff stuff = omdb_api.getStuffDetails(params[0]);
        if (stuff != null) {
            stuff.setUser(user);
            stuff.save();
            stuff.findUserVote();
            stuff.findUserAverage();

        }
        return stuff;
    }
}
