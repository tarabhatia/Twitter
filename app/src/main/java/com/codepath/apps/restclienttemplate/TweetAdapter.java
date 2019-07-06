package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.DetailsActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private List<Tweet> mTweets;
    private TwitterClient mClient;
    Context context;
    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets, TwitterClient client) {

        mTweets = tweets;
        mClient = client;
    }
    // for each row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }



    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        // get the data according to position
        final Tweet tweet = mTweets.get(position);

        //populate the views according to this data
        viewHolder.tvUsername.setText(tweet.user.name);
        viewHolder.tvBody.setText(tweet.body);
        viewHolder.tvDate.setText(getRelativeTimeAgo(tweet.createdAt));
        viewHolder.tvScreenName.setText('@' + tweet.user.screenName);

        if (tweet.isRetweeted) {
            viewHolder.imageButton2.setImageResource(R.drawable.ic_vector_retweet);
        } else {
            viewHolder.imageButton2.setImageResource(R.drawable.ic_vector_retweet_stroke);
        }

        if (tweet.isFavorited) {
            viewHolder.imageButton.setImageResource(R.drawable.ic_vector_heart);
        } else {
            viewHolder.imageButton.setImageResource(R.drawable.ic_vector_heart_stroke);
        }

        viewHolder.tvFav.setText(Integer.toString(tweet.favoriteCount));
        viewHolder.tvRetweet.setText(Integer.toString(tweet.retweetCount));


        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.isFavorited) {
                   mClient.unFavoriteTweet(tweet.uid, new JsonHttpResponseHandler());
                   viewHolder.imageButton.setImageResource(R.drawable.ic_vector_heart_stroke);
                   tweet.isFavorited = false;
                   tweet.favoriteCount--;
                   viewHolder.tvFav.setText(Integer.toString(tweet.favoriteCount));
                } else {
                   mClient.favoriteTweet(tweet.uid, new JsonHttpResponseHandler());
                    viewHolder.imageButton.setImageResource(R.drawable.ic_vector_heart);
                    tweet.favoriteCount++;
                    viewHolder.tvFav.setText(Integer.toString(tweet.favoriteCount));
                }
            }
        });


        viewHolder.imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.isRetweeted) {
                    mClient.unRetweetTweet(tweet.uid, new JsonHttpResponseHandler());
                    viewHolder.imageButton2.setImageResource(R.drawable.ic_vector_retweet_stroke);
                    tweet.isRetweeted = false;
                    tweet.retweetCount--;
                    viewHolder.tvFav.setText(Integer.toString(tweet.retweetCount));

                } else {
                    mClient.retweet(tweet.uid, new JsonHttpResponseHandler());
                    viewHolder.imageButton2.setImageResource(R.drawable.ic_vector_retweet);
                    tweet.retweetCount++;
                    viewHolder.tvFav.setText(Integer.toString(tweet.retweetCount));

                }
            }
        });



        Glide.with(context).load(tweet.user.profileImageUrl).into(viewHolder.ivProfileImage);
    }

    public int getItemCount() {
        return mTweets.size();
    }

    // create the ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvDate;
        public TextView tvScreenName;
        public ImageButton imageButton;
        public ImageButton imageButton2;
        public ImageButton imageButton3;
        public TextView tvFav;
        public TextView tvRetweet;



        @Override
        public void onClick(View v) {
            // get the position
            int position = getAdapterPosition();
            // ensures it's valid
            if (position != RecyclerView.NO_POSITION) {
                // get movies at the position in list
                Tweet tweet = mTweets.get(position);
                // creates an intent to display movie details activity
                Intent intent = new Intent(context, DetailsActivity.class);
                // passes the movie as an extra serialized via Parcels.wrap();
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // shows activity
                context.startActivity(intent);
            }

        }

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUserName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            imageButton = itemView.findViewById(R.id.imageButton);
            imageButton2 = itemView.findViewById(R.id.imageButton2);
            imageButton3 = itemView.findViewById(R.id.imageButton3);
            tvRetweet = itemView.findViewById(R.id.tvRetweet);
            tvFav = itemView.findViewById(R.id.tvFav);




            itemView.setOnClickListener(this);


        }
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
