package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.TwitterClientApplication;
import com.codepath.apps.twitterclient.adapters.TweetItemAdapter;
import com.codepath.apps.twitterclient.listeners.EndlessScrollListener;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeTimeline extends AppCompatActivity {

    private TwitterClient twitterClient;
    private ArrayList<Tweet> tweets;
    private ListView lvHomeTimeline;
    private TweetItemAdapter tweetItemAdapter;
    private Long maxId = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timeline);

        twitterClient = TwitterClientApplication.getRestClient();
        lvHomeTimeline = (ListView) findViewById(R.id.lvHomeTimeline);
        tweets = new ArrayList<>();
        tweetItemAdapter = new TweetItemAdapter(this,tweets);
        lvHomeTimeline.setAdapter(tweetItemAdapter);
        populateHomeTimeLine(null);
        setupOnScrollListener();
    }

    private void setupOnScrollListener(){
        lvHomeTimeline.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        maxId = tweets.get(tweets.size()-1).getUid();
        populateHomeTimeLine(maxId);
    }

    private void populateHomeTimeLine(final Long maxId){
        twitterClient.getHomeTimeLine(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweetItemAdapter.addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("error",throwable.getMessage());
                Toast.makeText(HomeTimeline.this, "Error occurred while retrieving Tweets", Toast.LENGTH_SHORT).show();
            }
        }, maxId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_timeline, menu);
        //ActionBar mActionBar = getSupportActionBar();
        //mActionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME| ActionBar.DISPLAY_SHOW_TITLE);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tweet) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showTweetActivity(MenuItem item) {
        Intent i = new Intent(this, TweetActivity.class);
        startActivityForResult(i,200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 200 && resultCode == 200){
            tweetItemAdapter.clear();
            populateHomeTimeLine(null);
        }
    }
}
