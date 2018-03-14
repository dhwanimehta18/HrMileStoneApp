package com.hrmilestoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrmilestoneapp.utils.PreferenceManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    Toolbar toolbar;
    Fragment fragment;
    ImageView profile_imageview;
    TextView tv_nav_user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        findHeader(headerView);
        profile_imageview = headerView.findViewById(R.id.profile_imageview);
        tv_nav_user_name = headerView.findViewById(R.id.tv_nav_user_name);

        String f_name = PreferenceManager.getprefUserFirstName(this);
        String l_name = PreferenceManager.getprefUserLastName(this);

        tv_nav_user_name.setText(f_name + " " + l_name);

        profile_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Image View Clicked", Toast.LENGTH_LONG).show();
                toolbar.setTitle(R.string.title_profile);
                fragment = new ProfileFragment();
                loadFragment(fragment);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setTitle("Dashboard");
        loadFragment(new DashboardFragment());
    }

    private void findHeader(View headerView) {

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
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            toolbar.setTitle(R.string.title_dashboard);
            fragment = new DashboardFragment();
            loadFragment(fragment);

        } else if (id == R.id.nav_events) {
            toolbar.setTitle(R.string.title_events);
            fragment = new EventsFragment();
            loadFragment(fragment);

        } else if (id == R.id.nav_photos) {
            toolbar.setTitle(R.string.title_photos);
            fragment = new PhotosFragment();
            loadFragment(fragment);

        } else if (id == R.id.nav_msg) {

        } else if (id == R.id.nav_profile) {

            toolbar.setTitle(R.string.title_profile);
            fragment = new ProfileFragment();
            loadFragment(fragment);

        } else if (id == R.id.nav_logout) {

            PreferenceManager.removePref(MainActivity.this, "USER_ID");
            PreferenceManager.removePref(MainActivity.this, "USER_FIRSTNAME");
            PreferenceManager.removePref(MainActivity.this, "USER_LASTNAME");
            PreferenceManager.removePref(MainActivity.this, "USER_EMAIL");

            Intent i = new Intent(MainActivity.this, LoginScreen.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}