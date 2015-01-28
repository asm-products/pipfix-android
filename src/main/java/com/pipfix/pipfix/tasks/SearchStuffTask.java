package com.pipfix.pipfix.tasks;

import java.io.InputStreamReader;
import java.io.IOException;
import android.os.AsyncTask;
import org.json.JSONException;
import java.io.BufferedReader;
import java.util.List;
import android.net.Uri;
import java.util.ArrayList;
import android.util.Log;
import java.net.URL;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import org.json.JSONArray;
import java.io.InputStream;
import android.widget.ArrayAdapter;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;

import com.pipfix.pipfix.SearchResult;

public class SearchStuffTask extends AsyncTask<String, Void, SearchResult[]> {

    private ArrayAdapter<SearchResult> searchAdapter;

    private final String LOG_TAG = SearchStuffTask.class.getSimpleName();

    public SearchStuffTask(ArrayAdapter<SearchResult> adapter) {
        this.searchAdapter = adapter;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private SearchResult[] getSearchResultFromJson(String searchResultStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OMDB_LIST = "Search";
        final String OMDB_TITLE = "Title";
        final String OMDB_YEAR = "Year";
        final String OMDB_ID = "imdbID";
      

        JSONObject searchResultJson = new JSONObject(searchResultStr);
        JSONArray resultArray = searchResultJson.getJSONArray(OMDB_LIST);

        List<SearchResult> resultStrs = new ArrayList<SearchResult>();
        for(int i = 0; i < resultArray.length(); i++) {

            JSONObject stuff = resultArray.getJSONObject(i);

            String title = stuff.getString(OMDB_TITLE);
            String year = stuff.getString(OMDB_YEAR);
            String id = stuff.getString(OMDB_ID);
            
            SearchResult searchResult = new SearchResult();
            searchResult.setTitle(title + "(" + year + ")");
            searchResult.setStuffId(id);
            resultStrs.add(searchResult);
        }

        for (SearchResult s : resultStrs) {
            Log.v(LOG_TAG, "Stuff entry: " + s);
        }

        SearchResult[] answerArray = new SearchResult[ resultStrs.size() ];
        return resultStrs.toArray( answerArray );
    }

    @Override
    protected SearchResult[] doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        HttpClient httpclient = new DefaultHttpClient();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority("www.omdbapi.com")
            .appendQueryParameter("s", params[0])
            .appendQueryParameter("r", "json");

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
            
            Log.v(LOG_TAG, "Stuff search string: " + searchResultStr);
            return getSearchResultFromJson(searchResultStr);
            

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
    protected void onPostExecute(SearchResult[] result) {
        if (result != null) {
            searchAdapter.clear();
            for(SearchResult dayForecastStr : result) {
                searchAdapter.add(dayForecastStr);
            }
        }
    }
}