package me.evancornish.instaparse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.evancornish.instaparse.model.Post;

public class HomeActivity extends AppCompatActivity {

    ArrayList<Post> posts;
    RecyclerView rvTimeline;
    PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        posts = new ArrayList<>();
        adapter = new PostAdapter(posts);
        rvTimeline = findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(this));
        rvTimeline.setAdapter(adapter);

        loadTopPosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miRefresh:
                loadTopPosts();
                break;
            case R.id.miAdd:
                Intent intentAdd = new Intent(this, Create.class);
                startActivity(intentAdd);
                break;
            case R.id.miLogOut:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intentOut = new Intent(HomeActivity.this, MainActivity.class);
                            intentOut.putExtra("Forget",true);
                            startActivity(intentOut);
                            finish();
                        }
                    }
                });
                break;
        }
        return true;
    }

    public void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();


        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    if (posts.size() != 0) {
                        posts.clear();
                        adapter.notifyDataSetChanged();
                    }
                    for (int i = 0; i < objects.size(); ++i) {
                        // Log.d("HomeActivity","Post[" + i + "] = " + objects.get(i).getDescription() + "\nusername = " + objects.get(i).getUser().getUsername());
                        posts.add(0, objects.get(i));
                        adapter.notifyItemInserted(0);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });

        rvTimeline.scrollToPosition(0);
    }
}
