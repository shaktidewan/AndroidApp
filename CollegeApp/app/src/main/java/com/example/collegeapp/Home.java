package com.example.collegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.nav_bottom_id);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //first ma default fragment display garexa vaiharu
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_id,new HomeFragment()).commit();

    }

    BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId())
                    {
                        case R.id.home_id:
                            selectedFragment =new HomeFragment();
                            break;
                        case  R.id.search_id:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.message_id:
                            selectedFragment = new MessageFragment();
                            break;

                    }
                    //Display Fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_id,selectedFragment).commit();
                    return true;
                }
            };
}
