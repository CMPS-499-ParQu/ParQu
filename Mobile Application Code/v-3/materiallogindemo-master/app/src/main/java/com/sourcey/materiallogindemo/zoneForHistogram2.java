package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class zoneForHistogram2 extends AppCompatActivity {
    static List<Property> zones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_for_histogram2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        zones = new ArrayList<>();
        final DatabaseReference databaseZones;
        databaseZones = FirebaseDatabase.getInstance().getReference("zones");

        databaseZones.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                // if(users != null)
                zones.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Property zone = postSnapshot.getValue(Property.class);
                    //adding artist to the list
                    zones.add(zone);
                }
                // int count =0;

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void buttonOnClick(View view)

    {
        Intent intent = new Intent(zoneForHistogram2.this, HistoryActivity.class);
        switch(view.getId())
        {
            case R.id.CENG_btn:
                // CENG Female Zone

                intent.putExtra("zoneName", "CENG Female Zone");
                startActivity(intent);
                break;

            case R.id.CAAS_btn:
                // Code for button 2 click
                intent.putExtra("zoneName", "CAAS Female Zone");
                startActivity(intent);
                break;

        }
    }
}
