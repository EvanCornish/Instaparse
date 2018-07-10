package me.evancornish.instaparse;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.evancornish.instaparse.model.Post;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("forTheGram")
                .clientKey("shootshootshootshoot")
                .server("http://evancornish-instaparse.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
