package com.codepath.apps.restclienttemplate.models;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {
    Tweet tweet;
    User user;
    ImageView ivProfilePic;
    TextView tvUser;
    TextView tvAt;
    TextView tvTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvUser = findViewById(R.id.tvUser);
        tvAt = findViewById(R.id.tvAt);
        tvTweet = findViewById(R.id.tvTweet);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvUser.setText(tweet.user.getName());
        tvAt.setText('@' + tweet.user.getScreenName());
        tvTweet.setText(tweet.getBody());

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .into(ivProfilePic);





    }
}
