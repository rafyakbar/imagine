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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    DataHelper dataHelper;
    SQLiteDatabase db;

    private Button signupButton, loginButton;
    private ImageView logoImage;
    private EditText name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupButton = findViewById(R.id.signupactivity_signup_button);
        loginButton = findViewById(R.id.signupactivity_login_button);
        logoImage = findViewById(R.id.signupactivity_logo_imageview);
        name = findViewById(R.id.signupactivity_name);
        email = findViewById(R.id.signupactivity_email);
        password = findViewById(R.id.signupactivity_password);

        //name.setText("Rafy Aulia Akbar");
        //email.setText("rafy683@gmail.com");
        //password.setText("secret");

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

    public void signingUp() {
        ArrayList<String> params = new ArrayList<>();
        params.add(name.getText().toString());
        params.add(email.getText().toString());
        params.add(password.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final String[] result = new String[1];
        result[0] = "";

        final ProgressDialog progressDialog = new ProgressDialog(getWindow().getContext());
        String url = Server.SIGNUP_URL;
        for (String v :
                params) {
            try {
                v = v.replace(" ", "=+-+=");
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
                result[0] = "ERROR";
                progressDialog.dismiss();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        progressDialog.setMessage("Signing up...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (result[0].equals("ERROR")){
                    Toast.makeText(getApplicationContext(), "Email has already been taken!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(result[0]);
                        db.execSQL("" +
                                "UPDATE setting " +
                                "SET value='"+jsonObject.getString("id")+"' " +
                                "WHERE name='id'");
                        db.execSQL("" +
                                "UPDATE setting " +
                                "SET value='"+jsonObject.getString("name")+"' " +
                                "WHERE name='name'");
                        db.execSQL("" +
                                "UPDATE setting " +
                                "SET value='"+jsonObject.getString("email")+"' " +
                                "WHERE name='email'");
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

    private void openLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
