package com.sourcey.materiallogindemo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Check_availability extends AppCompatActivity {
    List<Spot> spots;
    DatabaseReference databaseSpot;
    Button btn1, btn2, btn3, btn4;
   ImageView img4, img5, img8, img9, img7;
    String zoneName, spotNum;
    int ID;
   DatabaseReference  databaseCurrentlyLooking;

     List<Property> zones;
    DatabaseReference databaseZones;
    Spinner spinner1;

    private static final String TAG = "Check_availability";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_availability_2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        zones = new ArrayList<>();
        databaseZones = FirebaseDatabase.getInstance().getReference("zones");


         spinner1 = findViewById(R.id.SpotSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.spot_numbers,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

       // spotNum = spinner1.getSelectedItem().toString();




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
        final List<CurrentlyLooking> currentlyLooking;
        currentlyLooking = new ArrayList<>();


        databaseCurrentlyLooking = FirebaseDatabase.getInstance().getReference("currently looking");
       zoneName= getIntent().getStringExtra("zoneName");



      // ID = getIntent().getIntExtra("ID",0);

        btn1 =(Button) findViewById(R.id.btn1);
        btn2 =(Button) findViewById(R.id.btn2);
        btn3 =(Button) findViewById(R.id.btn3);
        btn4 =(Button) findViewById(R.id.btn4);

        img4 =(ImageView) findViewById(R.id.imageView4);
        img5 =(ImageView) findViewById(R.id.imageView5);
        img8 =(ImageView) findViewById(R.id.imageView8);
        img9 =(ImageView) findViewById(R.id.imageView9);

        img7 =(ImageView) findViewById(R.id.imageView7);
        PhotoViewAttacher photoView = new PhotoViewAttacher(img7);
        photoView.update();

        databaseSpot = FirebaseDatabase.getInstance().getReference("spots");
         spots = new ArrayList<>();
      /*  Spot spot = new Spot("A", 0, 4);
        String id = databaseSpot.push().getKey();
        databaseSpot.child(id).setValue(spot);*/
        databaseSpot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // if(users != null)
                spots.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Spot spot = postSnapshot.getValue(Spot.class);
                    spots.add(spot);
                }


                for(int i=0;  i< spots.size(); i++){
                    if(spots.get(i).getZoneName().equals(zoneName)){
                        if(spots.get(i).getStatus().equals("available"))
                        {
                       if (spots.get(i).getSpotNo() == 1){
                          //  btn1.setBackgroundColor(Color.WHITE);
                           img4.setVisibility(View.INVISIBLE);

                        }
                        if (spots.get(i).getSpotNo() == 2){
                          //  btn2.setBackgroundColor(Color.WHITE);
                            img5.setVisibility(View.INVISIBLE);
                        }
                        if (spots.get(i).getSpotNo() == 3){
                           // btn3.setBackgroundColor(Color.WHITE);
                            img8.setVisibility(View.INVISIBLE);

                        }
                            if (spots.get(i).getSpotNo() == 4){
                                //  btn4.setBackgroundColor(Color.WHITE);
                                img9.setVisibility(View.INVISIBLE);
                            }
                        }
                        else
                        {
                        if (spots.get(i).getSpotNo() == 1){
                         //   btn1.setBackgroundColor(Color.RED);
                            img4.setVisibility(View.VISIBLE);
                        }
                        if (spots.get(i).getSpotNo() == 2){
                          //  btn2.setBackgroundColor(Color.RED);
                            img5.setVisibility(View.VISIBLE);
                        }
                        if (spots.get(i).getSpotNo() == 3){
                          //  btn3.setBackgroundColor(Color.RED);
                            img8.setVisibility(View.VISIBLE);
                        }
                            if (spots.get(i).getSpotNo() == 4){
                                //  btn4.setBackgroundColor(Color.RED);
                               img9.setVisibility(View.VISIBLE);
                            }
                        }
                    }




                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(isServicesOK()){
            init();
        }

    }

    private void init(){
        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spotNum = spinner1.getSelectedItem().toString();
                Intent intent = new Intent(Check_availability.this, MapActivity.class);
                intent.putExtra("zoneName", zoneName);
                intent.putExtra("spotNum",spotNum);
                startActivity(intent);
            }
        });
    }



    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Check_availability.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Check_availability.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }




    protected void onStart() {

        super.onStart();
        Random r;
        r = new Random();
        ID = r.nextInt(1000);
        Calendar cal = Calendar.getInstance();

        cal.setTimeZone(TimeZone.getTimeZone("Asia/Qatar"));
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);


        String id2 = databaseCurrentlyLooking.push().getKey();
        CurrentlyLooking currentlyLook = new CurrentlyLooking(ID,currentHour,currentMinute, zoneName );
        databaseCurrentlyLooking.child(id2).setValue(currentlyLook);

    }

    protected void onStop() {
        super.onStop();

       /* for (int j = 0; j < zones.size(); j++) {
            if (zones.get(j).getZoneName().equals(zoneName)) {
                int count = zones.get(j).getCurrentlyLooking();
                count--;
                Property zone = new Property(zones.get(j).getZoneName(), count, zones.get(j).getTotalSpotsNo());

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                Query applesQuery = ref.child("zones").orderByChild("zoneName").equalTo(zoneName);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                String id2 = databaseZones.push().getKey();
                databaseZones.child(id2).setValue(zone);
                finish();

            }
        }*/

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        Query applesQuery = ref.child("currently looking").orderByChild("id").equalTo(ID);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
