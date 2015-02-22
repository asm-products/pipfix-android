package com.pipfix.pipfix;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pipfix.pipfix.models.Stuff;
import com.pipfix.pipfix.models.Vote;
import com.pipfix.pipfix.tasks.GetStuffTask;
import com.pipfix.pipfix.utils.AsyncTaskListener;

import java.io.InputStream;
import java.net.URL;
import java.util.List;


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

        private Drawable loadImageFromWeb(String url)
        {
            try
            {
                InputStream is = (InputStream) new URL(url).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                return d;
            }catch (Exception e) {
                System.out.println("Exc="+e);
                return null;
            }
        }


        public void handleIntent(Intent intent) {
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                stuff.setStuffId(intent.getStringExtra(Intent.EXTRA_TEXT));
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

                String user = sharedPref.getString("user_id", "");
                Log.v(LOG_TAG, "El usuario es: " + user);
                GetStuffTask getStuffTask = new GetStuffTask(user);
                getStuffTask.listenWith(new AsyncTaskListener<Stuff>() {
                    public void onPostExecute(Stuff result){
                        StuffDetailsActivity act = (StuffDetailsActivity) getActivity();
                        act.setStuff(result);

                        ((TextView) getActivity().findViewById(R.id.stuff_details_text))
                                .setText(result.getTitle());
                        ((TextView) getActivity().findViewById(R.id.stuff_detail_description))
                                .setText(result.getDescription());
                        if (result.getUserAverage() !=  null){
                            ((TextView) getActivity().findViewById(R.id.stuff_detail_user_average))
                                    .setText(result.getUserAverage().toString());
                        }
                        if (result.getAverage() != null) {
                            ((TextView) getActivity().findViewById(R.id.stuff_detail_average))
                                    .setText(result.getAverage().toString());
                        }

                        ImageLoader.getInstance().displayImage(result.getImage(),
                                (ImageView) getActivity().findViewById(R.id.stuff_details_image));

                        StringBuilder builder = new StringBuilder();
                        List<Vote> votes = result.getVotes();
                        for (int i = 0; i < votes.size(); i++) {
                            Vote vote = votes.get(i);
                            builder.append("<b>" + vote.getUser() + "</b> " + vote.getComment() + " - " +  vote.getPips().toString() +"<br />");
                        }
                        ((TextView) getActivity().findViewById(R.id.stuff_detail_comments))
                                .setText(Html.fromHtml(builder.toString()));
                    }
                });
                getStuffTask.execute(stuff.getStuffId());


            }
        }
    }
}
