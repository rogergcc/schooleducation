package com.appsnipp.schooleducation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    NavigationView navigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                    return true;
                case R.id.navigationMyCourses:
                    return true;
                case R.id.navigationHome:
                    return true;
                case  R.id.navigationSearch:
                    return true;
                case  R.id.navigationMenu:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDarkMode(getWindow());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavigationView.setSelectedItemId(R.id.navigationHome);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        ((NavigationMenuView)navigationView.getChildAt(0)).smoothScrollToPosition(0);
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            Intent intentPantalla = new Intent(MainActivity.this, AccountScrollActivity.class);
            startActivity(intentPantalla);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_dark_mode) {
            //code for setting dark mode
            //true for dark mode, false for day mode, currently toggling on each click
            DarkModePrefManager darkModePrefManager = new DarkModePrefManager(this);
            darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //create a seperate class file, if required in multiple activities
    public void setDarkMode(Window window){
        if(new DarkModePrefManager(this).isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            changeStatusBar(0,window);
            //setLightStatusBar()
            //clearLightStatusBar(window);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            changeStatusBar(1,window);
        }
    }

    public void clearLightStatusBar( Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

//            window.setStatusBarColor(ContextCompat
//                    .getColor(R.color.colorPrimaryDark));
//
            //window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));

            window.setStatusBarColor(ContextCompat
                    .getColor(MainActivity.this,R.color.colorPrimaryDark));
        }
    }

    public static void setLightStatusBar(View view, Window window){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            window.setStatusBarColor(Color.WHITE);
        }
    }

    public void changeStatusBar(int mode, Window window){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //window.setStatusBarColor(this.getResources().getColor(R.color.contentBodyColor));
            //window.setStatusBarColor(Color.TRANSPARENT);
        }

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            int systemUiVisibilityFlags = window.getDecorView().getSystemUiVisibility();
            systemUiVisibilityFlags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.getDecorView().setSystemUiVisibility(systemUiVisibilityFlags);

            //white mode
            if(mode==1){
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                window.setStatusBarColor(Color.WHITE);
            }else {
                window.setStatusBarColor(ContextCompat
                        .getColor(MainActivity.this,R.color.statusBarColor));
            }

        }
    }
}
