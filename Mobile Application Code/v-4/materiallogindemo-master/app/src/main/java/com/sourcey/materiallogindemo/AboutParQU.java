package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AboutParQU extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_par_qu);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public void facebook(View v){
        Uri uri = Uri.parse("http://www.facebook.com/parQU2019"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void twitter(View v){
        Uri uri = Uri.parse("http://www.twitter.com/parQU2019"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void instagram(View v){
        Uri uri = Uri.parse("http://www.instagram.com/parQU2019"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
