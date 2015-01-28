package com.pipfix.pipfix;

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
import android.content.Intent;
import android.app.SearchManager;
import android.content.Context;
import android.widget.TextView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import org.json.JSONObject;
import org.json.JSONException;
import android.support.v7.widget.SearchView;

import com.pipfix.pipfix.tasks.FixPipsTask;
import com.pipfix.pipfix.tasks.GetStuffTask;
import com.pipfix.pipfix.tasks.GetVoteTask;
import com.pipfix.pipfix.models.Stuff;
import com.pipfix.pipfix.utils.ListenableAsyncTask;
import com.pipfix.pipfix.utils.AsyncTaskListener;
import java.util.List;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class StuffDetailsFragment extends Fragment {

    private Stuff stuff;
    private View rootView;

    public StuffDetailsFragment() {
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
        inflater.inflate(R.menu.main_fragment, menu);

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

    public void initializeRatingBar(Integer pips) {
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);
        if (pips != null) {

            ratingBar.setRating((float)pips/2);
        }
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                boolean fromUser) {
                FixPipsTask fixpips = new FixPipsTask(stuff);
                fixpips.execute(String.valueOf(rating*2));

            }
        });
    }

    public void handleIntent(Intent intent) {
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            stuff.setStuffId(intent.getStringExtra(Intent.EXTRA_TEXT));
            GetStuffTask getStuffTask = new GetStuffTask(rootView);
            getStuffTask.execute(stuff.getStuffId());
            GetVoteTask getVoteTask = new GetVoteTask(stuff);
            getVoteTask.listenWith(new AsyncTaskListener<JSONObject>() {
                public void onPostExecute(JSONObject result) {
                    try {
                        if (result!=null) {
                            stuff.setPips((Integer)result.get("pips"));
                        }
                    } catch (JSONException e) {}
                    initializeRatingBar(stuff.getPips());
                }
            });
            getVoteTask.execute();

        }
    }
}