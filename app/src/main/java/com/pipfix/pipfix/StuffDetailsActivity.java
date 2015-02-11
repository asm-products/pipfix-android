package com.pipfix.pipfix;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.RatingBar;

import com.melnykov.fab.FloatingActionButton;
import com.pipfix.pipfix.models.Stuff;
import com.pipfix.pipfix.tasks.FixPipsTask;
import com.pipfix.pipfix.tasks.GetStuffTask;
import com.pipfix.pipfix.tasks.GetVoteTask;
import com.pipfix.pipfix.utils.AsyncTaskListener;

import org.json.JSONException;
import org.json.JSONObject;


public class StuffDetailsActivity extends ActionBarActivity {

    public Stuff getStuff() {
        return stuff;
    }

    public void setStuff(Stuff stuff) {
        this.stuff = stuff;
    }

    private Stuff stuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff_details);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public void openPipFix(View view){
        // Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PipFixActivity.class)
                .putExtra(Intent.EXTRA_TEXT, stuff.getStuffId());
        if (stuff.getPips() != null) {
            intent.putExtra("pips", stuff.getPips().toString());
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stuff_details, menu);
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

        private final String LOG_TAG = PlaceholderFragment.class.getSimpleName();

        private Stuff stuff;
        private View rootView;

        public PlaceholderFragment() {
            stuff = new Stuff();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Add this line in order for this fragment to handle menu events.
            setHasOptionsMenu(true);

        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_main, menu);

            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(
                            getActivity().getComponentName()));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_stuff_details, container, false);

            handleIntent(getActivity().getIntent());
            return rootView;
        }


        public void handleIntent(Intent intent) {
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                stuff.setStuffId(intent.getStringExtra(Intent.EXTRA_TEXT));
                StuffDetailsActivity act = (StuffDetailsActivity) getActivity();
                act.setStuff(stuff);
                GetStuffTask getStuffTask = new GetStuffTask(rootView);
                getStuffTask.execute(stuff.getStuffId());
                GetVoteTask getVoteTask = new GetVoteTask(stuff);
                getVoteTask.listenWith(new AsyncTaskListener<JSONObject>() {
                    public void onPostExecute(JSONObject result) {
                        try {
                            if (result!=null) {
                                Log.v(LOG_TAG, "pips: " + result.get("pips"));
                                stuff.setPips((Integer)result.get("pips"));
                                StuffDetailsActivity act = (StuffDetailsActivity) getActivity();
                                act.setStuff(stuff);
                            }
                        } catch (JSONException e) {}

                    }
                });
                getVoteTask.execute();

            }
        }
    }
}
