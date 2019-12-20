package com.appsnipp.schooleducation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.appsnipp.schooleducation.fragments.DashboardFragment;
import com.appsnipp.schooleducation.fragments.FragmentoCategorias;
import com.appsnipp.schooleducation.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    NavigationView navigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            Fragment fragmentoGenerico = null;
            FragmentManager fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                    break;
                case R.id.navigationMyCourses:
                    break;
                case R.id.navigationHome:
                    fragmentoGenerico = new DashboardFragment();
                    break;
                case  R.id.navigationSearch:
                    break;
                case  R.id.navigationMenu:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                    break;
            }

            if (fragmentoGenerico != null) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragmentoGenerico)
                        .commit();
            }

            // Setear título actual
            setTitle(item.getTitle());

            return true;
        }
    };
    private String[] dataArray;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDarkMode(getWindow());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        dataArray = this.getApplicationContext().getResources().getStringArray(R.array.entradas_categoria);

        //RecyclerView reciclador = findViewById(R.id.reciclador);


        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //reciclador.setLayoutManager(layoutManager);

        //AdapterCategories adaptador = new AdapterCategories(this,dataArray);
        //reciclador.setAdapter(adaptador);

//        if (navigationView != null) {
//            prepararDrawer(navigationView);
//            // Seleccionar item por defecto
//            seleccionarItem(navigationView.getMenu().getItem(0));
//
//
//        }


        Utils.showBadge(this, bottomNavigationView, R.id.navigationMyProfile, "5");

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


    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemDrawer.getItemId()) {
            case R.id.nav_account:

                Intent intentPantalla = new Intent(MainActivity.this, AccountScrollActivity.class);
                startActivity(intentPantalla);
                break;
            case R.id.nav_gallery:

                break;
            case R.id.nav_slideshow:
                fragmentoGenerico = new FragmentoCategorias();

                break;
            case R.id.nav_manage:
                //fragmentoGenerico = new Fragment();

                break;
            case R.id.nav_share:

                break;
            case R.id.nav_dark_mode:
                DarkModePrefManager darkModePrefManager = new DarkModePrefManager(this);
                darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
                break;
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragmentoGenerico)
                    .commit();
        }

        // Setear título actual
        setTitle(itemDrawer.getTitle());
    }

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawer.closeDrawers();
                        return true;
                    }
                });

    }

    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        seleccionarItem(item);

//        // Handle navigation view item clicks here.
//
//        int id = item.getItemId();
//
//        if (id == R.id.nav_account) {
//            Intent intentPantalla = new Intent(MainActivity.this, AccountScrollActivity.class);
//            startActivity(intentPantalla);
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_dark_mode) {
//            //code for setting dark mode
//            //true for dark mode, false for day mode, currently toggling on each click
//            DarkModePrefManager darkModePrefManager = new DarkModePrefManager(this);
//            darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            recreate();
//
//        }

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

//        window.getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
//            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//            int systemUiVisibilityFlags = window.getDecorView().getSystemUiVisibility();
//            systemUiVisibilityFlags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            window.getDecorView().setSystemUiVisibility(systemUiVisibilityFlags);
//
//            //white mode
//            if(mode==1){
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//
//            }else {
//                window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.statusBarColor));
//            }
//
//        }
    }
}
