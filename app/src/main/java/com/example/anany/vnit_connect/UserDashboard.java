package com.example.anany.vnit_connect;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.content.pm.PackageManager;


import com.example.anany.vnit_connect.adapters.FragmentAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager vp_pages;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(UserDashboard.this, LoginActivity.class));
                    finish();
                }
            }
        };


        auth = FirebaseAuth.getInstance();

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(user != null)
        {
            if(user.getDisplayName()!=null ) {
                toolbar.setTitle("Hi " + user.getDisplayName().toString());
            }
            else
            {
                toolbar.setTitle("Hi " + user.getEmail().toString());
            }
        }
        else
        {
            toolbar.setTitle("Hi ");
        }
        setSupportActionBar(toolbar);

        //firebase connection
        /*auth = FirebaseAuth.getInstance();
        final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user1 = firebaseAuth.getCurrentUser();
                if (user1 == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(UserDashboard.this, LoginActivity.class));
                    finish();
                }
            }
        };*/

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        /*progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //
        // Tab layout
        //
        // Find the view pager that will allow the user to swipe between fragments
        vp_pages= (ViewPager) findViewById(R.id.vp_pages);
        PagerAdapter adapter = new FragmentAdapter(this, getSupportFragmentManager());
        vp_pages.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tbl_pages);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(vp_pages);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("tab_index");
            if(value.equals("1")){
                vp_pages.setCurrentItem(1);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }
    /*@Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }*/

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //progressBar.setVisibility(View.GONE);
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

    //sign out method
    public void signOut() {
        auth.signOut();
        // this listener will be called when there is change in firebase user session
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(UserDashboard.this, LoginActivity.class));
                    finish();
                }
            }
        };

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chatbot) {
            vp_pages.setCurrentItem(0);
        }
        else if (id == R.id.nav_forum) {
            vp_pages.setCurrentItem(1);
        }
        else if (id == R.id.nav_profile) {
            startActivity(new Intent(UserDashboard.this, EditProfileActivity.class));
        }
        else if (id == R.id.nav_logout) {

            auth.signOut();
            // this listener will be called when there is change in firebase user session
            FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        // user auth state is changed - user is null
                        // launch login activity
                        System.out.println("Logged out!");
                        startActivity(new Intent(UserDashboard.this, LoginActivity.class));
                        finish();
                    }
                }
            };
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}