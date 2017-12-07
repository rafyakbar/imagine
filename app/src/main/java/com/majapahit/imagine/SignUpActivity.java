package com.majapahit.imagine;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.majapahit.imagine.util.DataHelper;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    DataHelper dataHelper;
    SQLiteDatabase db;

    private Button signupButton;
    private Button loginButton;
    private ImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupButton = findViewById(R.id.signupactivity_signup_button);
        loginButton = findViewById(R.id.signupactivity_login_button);
        logoImage = findViewById(R.id.signupactivity_logo_imageview);

        dataHelper = new DataHelper(getApplicationContext());
        db = dataHelper.getReadableDatabase();

        try {
            logoImage.setImageBitmap(BitmapFactory.decodeStream(getAssets().open("imagine.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signingUp();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });
    }

    public void signingUp(){
        db.execSQL("" +
                "UPDATE setting " +
                "SET value='1' " +
                "WHERE name='id'");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void openLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
