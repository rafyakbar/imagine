package com.majapahit.imagine;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.majapahit.imagine.util.DataHelper;
import com.majapahit.imagine.util.SettingModel;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    DataHelper dataHelper;
    SQLiteDatabase db;
    Random r = new Random();

    HomeFragment homeFragment;
    SearchFragment searchFragment = null;
    IdeaFragment ideaFragment = null;
    FriendFragment friendFragment = null;
    SettingFragment settingFragment = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, homeFragment).commit();
                    return true;
                case R.id.navigation_search:
                    if (searchFragment == null)
                        searchFragment = new SearchFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, searchFragment).commit();
                    return true;
                case R.id.navigation_idea:
                    if (ideaFragment == null)
                        ideaFragment = new IdeaFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, ideaFragment).commit();
                    return true;
                case R.id.navigation_friends:
                    if (friendFragment == null)
                        friendFragment = new FriendFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, friendFragment).commit();
                    return true;
                case R.id.navigation_setting:
                    if (settingFragment == null)
                        settingFragment = new SettingFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, settingFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        dataHelper = new DataHelper(getApplicationContext());
        db = dataHelper.getReadableDatabase();
//        initializeView();
        if (SettingModel.checkLoginOrRegister(db)){
            initializeView();
            db.execSQL("" +
                    "UPDATE setting " +
                    "SET value='-' " +
                    "WHERE name='id'");
        }
        else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void initializeView(){
        homeFragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, homeFragment).commit();
    }
}

