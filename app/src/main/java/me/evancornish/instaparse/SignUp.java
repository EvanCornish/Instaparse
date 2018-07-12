package me.evancornish.instaparse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    Button bSignUp;
    EditText etNewUsername;
    EditText etNewPassword;
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        bSignUp = findViewById(R.id.bSignUp);
        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNewUsername = findViewById(R.id.etNewUsername);
                etNewPassword = findViewById(R.id.etNewPassword);
                etName = findViewById(R.id.etName);

                ParseUser user = new ParseUser();
                user.setUsername(etNewUsername.getText().toString());
                user.setPassword(etNewPassword.getText().toString());
                user.put("name", etName.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            intent.putExtra("New User", true);
                            ArrayList<String> info = new ArrayList<>();
                            info.add(etNewUsername.getText().toString());
                            info.add(etNewPassword.getText().toString());
                            intent.putExtra("Info", info);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}
