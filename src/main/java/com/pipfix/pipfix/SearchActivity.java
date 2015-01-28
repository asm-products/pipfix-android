package com.pipfix.pipfix;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

import android.view.Menu;
import android.view.MenuItem;


public class SearchActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.search_container, new SearchFragment())
                    .commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SearchFragment fragment = (SearchFragment)fragmentManager.findFragmentById(
            R.id.search_container);
        if (fragment != null ) {
            fragment.handleIntent(intent);
        }
    }

}