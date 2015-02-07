package com.pipfix.pipfix;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pipfix.pipfix.tasks.GetUserTask;
import com.pipfix.pipfix.tasks.SearchStuffTask;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;



public class LoginActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "vLerXCrMiAxzRbJd6l45SIEmm";
    private static final String TWITTER_SECRET = "sd9ZnSeVyXmN5f5Rkj5fay75KnpzijYudOpISJS6a09EYohc8G";

    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        //Twitter.getSessionManager().clearActiveSession();
        TwitterSession session = Twitter.getSessionManager().getActiveSession();


        if (session != null) {
            updateUser();
        } else {
            setContentView(R.layout.activity_login);
            loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    updateUser();
                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                }
            });
        }
    }

    protected void updateUser() {
        GetUserTask getUserTask = new GetUserTask(this);
        getUserTask.execute();
    }

    /*
    protected void updateFollowers() {
        TwitterListener listener = new TwitterAdapter() {
            @Override public void gotFollowersIDs(IDs ids) {
                System.out.println("Successfully updated the status to");
            }

            @Override public void onException(twitter4j.TwitterException te, TwitterMethod method) {
                if (method == TwitterMethod.FOLLOWERS_IDS) {
                    te.printStackTrace();
                } else {
                    throw new AssertionError("Should not happen");
                }
            }
        };
        // The factory instance is re-useable and thread safe.
        AsyncTwitterFactory factory = new AsyncTwitterFactory();
        AsyncTwitter asyncTwitter = factory.getInstance();
        asyncTwitter.addListener(listener);
        asyncTwitter.getFollowersIDs();
    }*/

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
