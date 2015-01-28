package com.pipfix.pipfix;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import android.widget.Toast;
import android.widget.AdapterView;
import android.content.Intent;
import android.app.SearchManager;
import android.content.Context;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pipfix.pipfix.SearchResult;
import com.pipfix.pipfix.tasks.SearchStuffTask;
/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class SearchFragment extends Fragment {

    private ArrayAdapter<SearchResult> searchAdapter;

    public SearchFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

        List<SearchResult> searchResult = new ArrayList<SearchResult>();

        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        searchAdapter =
                new ArrayAdapter<SearchResult>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_search, // The name of the layout ID.
                        R.id.list_item_search_textview, // The ID of the textview to populate.
                        searchResult);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_search);
        listView.setAdapter(searchAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SearchResult forecast = searchAdapter.getItem(position);
                // Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), StuffDetailsActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast.getStuffId());
                startActivity(intent);
            }
        });
        handleIntent(getActivity().getIntent());
        return rootView;
    }

    public void handleIntent(Intent intent) {
        if (intent != null && Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchStuffTask searchStuffTask = new SearchStuffTask(searchAdapter);
            searchStuffTask.execute(query);
        }
        
    }

    
}