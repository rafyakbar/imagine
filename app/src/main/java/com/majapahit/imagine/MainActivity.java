package com.majapahit.imagine;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.majapahit.imagine.util.DataHelper;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    DataHelper dataHelper;
    SQLiteDatabase db;
    Random r = new Random();

    HomeFragment homeFragment;
    SearchFragment searchFragment;
    IdeaFragment ideaFragment;
    FriendFragment friendFragment;

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
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, friendFragment).commit();
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
        initializeView();
//        if (SettingModel.checkLoginOrRegister(db)){
//            initializeView();
//        }
//        else {
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            this.finish();
//        }
    }

    private void initializeView(){
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        ideaFragment = new IdeaFragment();
        friendFragment = new FriendFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, homeFragment).commit();
    }
}

