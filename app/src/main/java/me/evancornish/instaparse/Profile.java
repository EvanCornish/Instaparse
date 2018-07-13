package me.evancornish.instaparse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.evancornish.instaparse.model.Post;

public class Profile extends AppCompatActivity {

    String userID;
    TextView tvNPosts;
    TextView tvNFollowers;
    TextView tvNFollowing;
    TextView tvProfName;
    TextView tvProfUserName;
    TextView tvIsFollowing;
    RecyclerView rvProfPosts;
    ParseUser user;
    ArrayList<Post> posts;
    PostAdapter adapter;
    boolean isYou;
    boolean areFollowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvNPosts = findViewById(R.id.tvNPosts);
        tvNFollowers = findViewById(R.id.tvNFollowers);
        tvNFollowing = findViewById(R.id.tvNFollowing);
        tvProfName = findViewById(R.id.tvProfName);
        tvProfUserName = findViewById(R.id.tvProfUserName);
        tvIsFollowing = findViewById(R.id.tvIsFollowing);
        posts = new ArrayList<>();
        adapter = new PostAdapter(posts);
        rvProfPosts = findViewById(R.id.rvProfPosts);
        rvProfPosts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProfPosts.setAdapter(adapter);

        userID = getIntent().getStringExtra("user");
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(userID, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    user = object;

                    getLists();
                }
            }
        });

        isYou = userID.equals(ParseUser.getCurrentUser().getObjectId());
        if (!isYou) {
            tvIsFollowing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (areFollowing) {
                        tvIsFollowing.setText("FOLLOW");
                        tvNFollowers.setText(Integer.toString(Integer.parseInt(tvNFollowers.getText().toString()) - 1));
                    } else {
                        tvIsFollowing.setText("FOLLOWING");
                        tvNFollowers.setText(Integer.toString(Integer.parseInt(tvNFollowers.getText().toString()) + 1));
                    }
                    areFollowing = !areFollowing;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    public void getLists() {
        Post.Query query = new Post.Query();
        query.getTop().withUser();
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (Post object : objects) {
                        if (object.getUser().getObjectId().equals(userID)) {
                            posts.add(0, object);
                            adapter.notifyItemInserted(0);
                        }
                    }
                    tvNPosts.setText(Integer.toString(posts.size()));
                } else
                    tvNPosts.setText("0");
            }
        });

        tvNFollowers.setText(Integer.toString((int) (Math.random() * 1000)));
        tvNFollowing.setText(Integer.toString((int) (Math.random() * 1000)));
        tvProfName.setText(user.getString("name"));
        tvProfUserName.setText("@" + user.getUsername());
        if (!isYou)
            tvIsFollowing.setText(areFollowing ? "FOLLOWING" : "FOLLOW");
    }
}
