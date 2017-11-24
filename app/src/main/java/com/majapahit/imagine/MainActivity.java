package com.majapahit.imagine;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    DataHelper dataHelper;
    SQLiteDatabase db;
    Random r = new Random();

    HomeFragment homeFragment;
    SearchFragment searchFragment;
    IdeaFragment ideaFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, homeFragment).commit();
                    return true;
                case R.id.navigation_search:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, searchFragment).commit();
                    return true;
                case R.id.navigation_idea:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, ideaFragment).commit();
                    return true;
                case R.id.navigation_friends:

                    return true;
                case R.id.navigation_setting:

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

        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        ideaFragment = new IdeaFragment();



        getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, homeFragment).commit();
    }
}

