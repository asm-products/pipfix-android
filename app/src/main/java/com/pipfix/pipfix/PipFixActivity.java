package com.pipfix.pipfix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.pipfix.pipfix.models.Stuff;
import com.pipfix.pipfix.tasks.FixPipsTask;
import com.pipfix.pipfix.utils.AsyncTaskListener;


public class PipFixActivity extends ActionBarActivity {

    private final String LOG_TAG = PipFixActivity.class.getSimpleName();

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

    public void onSubmit(MenuItem item){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        EditText editText = (EditText) findViewById(R.id.editText);
        String user = sharedPref.getString("user_id", "");

        FixPipsTask task = new FixPipsTask(stuff,
                user,
                (int)Math.round(Float.valueOf(ratingBar.getRating())),
                editText.getText().toString());
        task.listenWith(new AsyncTaskListener<String>() {
            public void onPostExecute(String result) {
                finish();
            }
        });
        task.execute();
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




        public void handleIntent(Intent intent) {
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                stuff.setStuffId(intent.getStringExtra(Intent.EXTRA_TEXT));
                String pips = intent.getStringExtra("pips");
                if (pips != null) {
                    stuff.setPips(Integer.valueOf(pips));
                }
                PipFixActivity act = (PipFixActivity) getActivity();
                act.setStuff(stuff);
            }
        }
    }
}