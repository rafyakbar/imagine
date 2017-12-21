package com.majapahit.imagine;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majapahit.imagine.url.Server;
import com.majapahit.imagine.util.DataHelper;
import com.majapahit.imagine.util.SettingModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    DataHelper dataHelper;
    SQLiteDatabase db;

    private Button loginButton;
    private Button signupButton;
    private ImageView logoImage;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dataHelper = new DataHelper(getApplicationContext());
        db = dataHelper.getReadableDatabase();

        loginButton = findViewById(R.id.loginactivity_login_button);
        signupButton = findViewById(R.id.loginactivity_signup_button);
        logoImage = findViewById(R.id.loginactivity_logo_imageview);
        email = findViewById(R.id.loginactivity_email);
        password = findViewById(R.id.loginactivity_password);

        //email.setText("rafy683@gmail.com");
        //password.setText("secret");

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
        ArrayList<String> params = new ArrayList<>();
        params.add(email.getText().toString().trim());
        params.add(password.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final String[] result = new String[1];
        result[0] = "";

        final ProgressDialog progressDialog = new ProgressDialog(getWindow().getContext());
        String url = Server.SIGNIN_URL;
        for (String v :
                params) {
            v = v.replace(" ", "=+-+=");
            try {
                url += URLEncoder.encode(v, "UTF-8") + "/";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Log.d("url", url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);
                        result[0] = response;
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("message", "Error: " + error.getMessage());
                Log.d("message", "Failed with error msg:\t" + error.getMessage());
                Log.d("message", "Error StackTrace: \t" + error.getStackTrace());
                try {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    Log.e("message", new String(htmlBodyBytes), error);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                result[0] = "failed";
                progressDialog.dismiss();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        progressDialog.setMessage("Login in...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (result[0].equals("failed")){
                    Toast.makeText(getApplicationContext(), "Wrong email or password!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(result[0]);
                        SettingModel.update(db, "id", jsonObject.getString("id"));
                        SettingModel.update(db, "name", jsonObject.getString("name"));
                        SettingModel.update(db, "email", jsonObject.getString("email"));
                        SettingModel.update(db, "location", jsonObject.getString("location"));
                        SettingModel.update(db, "about", jsonObject.getString("about"));
                        SettingModel.update(db, "dir", jsonObject.getString("dir"));
                        openMainActivity();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.toString() + ":" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void openMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
