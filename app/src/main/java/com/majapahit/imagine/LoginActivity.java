package com.majapahit.imagine;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.majapahit.imagine.util.DataHelper;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    DataHelper dataHelper;
    SQLiteDatabase db;

    private Button loginButton;
    private Button signupButton;
    private ImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dataHelper = new DataHelper(getApplicationContext());
        db = dataHelper.getReadableDatabase();

        loginButton = findViewById(R.id.loginactivity_login_button);
        signupButton = findViewById(R.id.loginactivity_signup_button);
        logoImage = findViewById(R.id.loginactivity_logo_imageview);

        try {
            logoImage.setImageBitmap(BitmapFactory.decodeStream(getAssets().open("imagine.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logginIn();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });
    }

    public void openSignUpActivity(){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void logginIn(){
        db.execSQL("" +
                "UPDATE setting " +
                "SET value='1' " +
                "WHERE name='id'");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
