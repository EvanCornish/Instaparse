package me.evancornish.instaparse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.List;

import me.evancornish.instaparse.model.Post;

public class DetailedPostActivity extends AppCompatActivity {

    String postID;
    Post post;

    TextView tvDName;
    TextView tvDUserName;
    ImageView ivDPic;
    TextView tvDDescription;
    TextView tvDTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_post);

        tvDName = findViewById(R.id.tvDName);
        tvDUserName = findViewById(R.id.tvDUserName);
        ivDPic = findViewById(R.id.ivDPic);
        tvDDescription = findViewById(R.id.tvDDescription);
        tvDTime = findViewById(R.id.tvDTime);

        postID = getIntent().getStringExtra("Post");
        final Post.Query postsQuery = new Post.Query();
        postsQuery.withUser();
        postsQuery.getInBackground(postID, new GetCallback<Post>() {
            @Override
            public void done(Post object, ParseException e) {
                if (e == null) {
                    post = object;

                    tvDName.setText(post.getUser().getString("name"));
                    tvDUserName.setText(post.getUser().getString("username"));
                    tvDDescription.setText(post.getDescription());
                    tvDTime.setText(post.getCreatedAt().toString());
                    Glide.with(DetailedPostActivity.this)
                            .load(post.getImage().getUrl())
                            .into(ivDPic);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
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
}
