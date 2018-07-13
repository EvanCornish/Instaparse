package me.evancornish.instaparse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

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
    ArrayList<String> followers;
    ArrayList<String> following;
    PostAdapter adapter;
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
        rvProfPosts.setLayoutManager(new LinearLayoutManager(this));
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

        tvIsFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areFollowing) {
                    for (String follower : followers)
                        if (follower.equals(userID))
                            followers.remove(follower);
                    tvIsFollowing.setText("FOLLOW");
                    tvNFollowers.setText(Integer.toString(Integer.parseInt(tvNFollowers.getText().toString()) - 1));
                } else {
                    followers.add(ParseUser.getCurrentUser().getObjectId());
                    tvIsFollowing.setText("FOLLOWING");
                    tvNFollowers.setText(Integer.toString(Integer.parseInt(tvNFollowers.getText().toString()) + 1));
                }
                user.put("followers", followers);
                user.saveInBackground();
            }
        });
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

        followers = (ArrayList<String>) user.get("followers");
        following = (ArrayList<String>) user.get("following");

        if (followers == null)
            followers = new ArrayList<>();
        if (following == null)
            following = new ArrayList<>();

        for (String follower : followers)
            if (follower.equals(userID))
                areFollowing = true;

        tvNFollowers.setText(Integer.toString(followers.size()));
        tvNFollowing.setText(Integer.toString(following.size()));
        tvProfName.setText(user.getString("name"));
        tvProfUserName.setText("@" + user.getUsername());
        tvIsFollowing.setText(areFollowing ? "FOLLOWING" : "FOLLOW");
    }
}
