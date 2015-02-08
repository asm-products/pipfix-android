package com.pipfix.pipfix;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.pipfix.pipfix.models.Stuff;
import com.pipfix.pipfix.tasks.FixPipsTask;
import com.pipfix.pipfix.tasks.GetStuffTask;
import com.pipfix.pipfix.tasks.GetVoteTask;
import com.pipfix.pipfix.utils.AsyncTaskListener;

import org.json.JSONException;
import org.json.JSONObject;


public class PipFixActivity extends ActionBarActivity {

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
        setContentView(R.layout.activity_pip_fix);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pip_fix, menu);
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

        private Stuff stuff;
        private View rootView;

        public PlaceholderFragment() {
            stuff = new Stuff();
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_pip_fix, container, false);
            handleIntent(getActivity().getIntent());
            return rootView;
        }

        public void initializeRatingBar(Integer pips) {
            RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);
            if (pips != null) {

                ratingBar.setRating((float)pips/2);
            }
            //display the current rating value in the result (textview) automatically
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
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
                PipFixActivity act = (PipFixActivity) getActivity();
                act.setStuff(stuff);
            }
        }
    }
}