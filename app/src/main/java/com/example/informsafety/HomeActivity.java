package com.example.informsafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.navigation_home);
        }
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        startActivity(new Intent(this,MainActivity.class));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.navigation_draft_forms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DraftsFragment()).commit();
//                Toast.makeText(this, "Sorry, this part of the application is still under production.", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(HomeActivity.this, DraftsActivity.class));
                break;
            case R.id.navigation_sent_forms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SentFormsFragment()).commit();
//                Toast.makeText(this, "Sorry, this part of the application is still under production.", Toast.LENGTH_LONG).show();
                break;
            case R.id.navigation_completed_forms:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationsFragment()).commit();
                Toast.makeText(this, "Sorry, this part of the application is still under production.", Toast.LENGTH_LONG).show();
                break;
            case R.id.navigation_notifications:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationsFragment()).commit();
                Toast.makeText(this, "Sorry, this part of the application is still under production.", Toast.LENGTH_LONG).show();
                break;
            case R.id.navigation_child_protection:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChildProtectionFragment()).commit();
                break;
            case R.id.navigation_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LegalFragment()).commit();
//                Toast.makeText(this, "Sorry, this part of the application is still under production.", Toast.LENGTH_LONG).show();
                break;
            case R.id.navigation_contact_us:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutUsFragment()).commit();
                Toast.makeText(this, "Sorry, this part of the application is still under production.", Toast.LENGTH_LONG).show();
                break;
            case R.id.navigation_privacy_policy:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LegalPrivacy()).commit();
//                Toast.makeText(this, "Sorry, this part of the application is still under production.", Toast.LENGTH_LONG).show();
                break;
            case R.id.navigation_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
//                Toast.makeText(this, "Sorry, this part of the application is still under production.", Toast.LENGTH_LONG).show();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

        public void onBackPressed () {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }