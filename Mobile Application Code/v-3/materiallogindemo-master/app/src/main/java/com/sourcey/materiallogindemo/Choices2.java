package com.sourcey.materiallogindemo;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Choices2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void Error (View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("This service is available for VIP users only");
        builder.setTitle("Error");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog a = builder.create();
        a.show();
    }

    public void checkAvailability(View view) {
        Intent checkIntent = new Intent(Choices2.this, ZoneList2.class);
        startActivity(checkIntent);
        //to be implemented
    }
    public void occupancyTrend(View view) {
        Intent checkIntent = new Intent(Choices2.this, zoneForHistogram2.class);
        startActivity(checkIntent);
    }
}
