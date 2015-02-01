package com.pipfix.pipfix;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pipfix.pipfix.models.SearchResult;
import com.pipfix.pipfix.tasks.SearchStuffTask;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private ArrayAdapter<SearchResult> searchAdapter;

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Add this line in order for this fragment to handle menu events.
            setHasOptionsMenu(true);

            List<SearchResult> searchResult = new ArrayList<SearchResult>();

            // The ArrayAdapter will take data from a source (like our dummy forecast) and
            // use it to populate the ListView it's attached to.
            searchAdapter =
                    new ArrayAdapter<SearchResult>(
                            getActivity(), // The current context (this activity)
                            R.layout.fragment_search, // The name of the layout ID.
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
}
