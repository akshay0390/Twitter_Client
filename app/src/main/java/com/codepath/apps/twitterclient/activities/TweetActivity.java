package com.codepath.apps.twitterclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.TwitterClientApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class TweetActivity extends AppCompatActivity {

    private ImageView userProfileImage;
    private TextView userName;
    private EditText etTweetBody;
    private TwitterClient twitterClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        twitterClient = TwitterClientApplication.getRestClient();
        setupViews();
        populateUserInfo();

    }

    private void setupViews(){
        userProfileImage = (ImageView) findViewById(R.id.ivUserProfileImage);
        userName = (TextView) findViewById(R.id.tvName);
        etTweetBody = (EditText) findViewById(R.id.etTweet);
    }

    private void populateUserInfo(){
        twitterClient.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    userName.setText(response.getString("name"));
                    Picasso.with(getBaseContext()).load(response.getString("profile_image_url")).into(userProfileImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tweetBtn) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void postTweet(MenuItem item) {

        String tweetText = etTweetBody.getText().toString();
        twitterClient.postTweet(new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                setResult(200);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("ERROR", errorResponse.toString());
                Toast.makeText(TweetActivity.this,"Some Error Occured while Posting your Tweet!",Toast.LENGTH_SHORT).show();
            }
        },tweetText);

    }
}
