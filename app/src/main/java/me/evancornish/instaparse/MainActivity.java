package me.evancornish.instaparse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button bLogin;
    private Button bToSignUp;

    ArrayList<String> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        bLogin = findViewById(R.id.bLogin);
        bToSignUp = findViewById(R.id.bToSignUp);

        if (getIntent().getBooleanExtra("Forget", false)) {
            info = null;
            writeInfo();
        }

        if (getIntent().getBooleanExtra("New User", false)) {
            info = getIntent().getStringArrayListExtra("Info");
            writeInfo();
        }

        readInfo();
        if (info != null && info.size() > 0) {
            etUsername.setText(info.get(0));
            etPassword.setText(info.get(1));

            login(info.get(0), info.get(1), true);
        }

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                login(username, password, false);
            }
        });

        bToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        return true;
    }

    private void login(String username, final String password, boolean saved) {
        final boolean saveMe = !saved;
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "Login successful");
                    if(saveMe) {
                        info = new ArrayList<>();
                        info.add(user.getUsername());
                        info.add(password);
                        writeInfo();
                    }
                    final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginActivity", "login failure");
                    e.printStackTrace();
                }
            }
        });
    }

    @NonNull
    private File getDataFile()
    {
        return new File(getFilesDir(),"user.txt");
    }

    private void readInfo()
    {
        try
        {
            info=new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch(IOException e)
        {
            e.printStackTrace();
            info=null;
        }
    }

    private void writeInfo()
    {
        try
        {
            FileUtils.writeLines(getDataFile(),info);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
