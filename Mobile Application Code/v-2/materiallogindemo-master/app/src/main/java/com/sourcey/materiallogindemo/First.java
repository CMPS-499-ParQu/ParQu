package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class First extends AppCompatActivity {
private DrawerLayout mDrawerLayout;
private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // conitne a s vip or normal

        final Button normalBtn = findViewById(R.id.normal_btn);
        Button vipBtn = findViewById(R.id.vip_btn);


        normalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent normalIntent = new Intent(First.this,Choices2.class);
                startActivity(normalIntent);
            }
        });


        vipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vipIntent = new Intent(First.this,LoginActivity.class);
                startActivity(vipIntent);
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_aboutParQU:
                                Intent i = new Intent(First.this, AboutParQU.class);
                                startActivity(i);
                                break;

                            case R.id.nav_contactUs:
                                Intent i2 = new Intent(First.this, ContactUs.class);
                                startActivity(i2);
                                break;

                            case R.id.nav_shareThisApp:

                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBodyText = "Check it out. Your message goes here";
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                                startActivity(Intent.createChooser(sharingIntent, "Choose Sharing Method"));
                                return true;

                        }


                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;

        }
        return super.onOptionsItemSelected(item);
    }



    }
