package com.sourcey.materiallogindemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class ZoneList2 extends AppCompatActivity {
    private ArrayList<Property> zoneProperties = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_list3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final List<Property> zones;
        zones = new ArrayList<>();
        final List<CurrentlyLooking> currentlyLooking;
        currentlyLooking = new ArrayList<>();

        final DatabaseReference databaseZones;
        databaseZones = FirebaseDatabase.getInstance().getReference("zones");

        final DatabaseReference  databaseCurrentlyLooking;
        databaseCurrentlyLooking = FirebaseDatabase.getInstance().getReference("currently looking");

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



        //create our property elements

        zoneProperties.add(new Property("CENG Female Zone", 0,"College of Engineering - Female (C07)","property_image_1", 4));
        zoneProperties.add(new Property("CAAS Female Zone", 0,"College of Art and Science - Female (C01)","property_image_2", 4));
        zoneProperties.add(new Property("CENG Male Zone", 0,"College of Engineering - Male (BCR)","property_image_3", 4));
        zoneProperties.add(new Property("BNK Male Zone", 0,"Bank/Ibn Khaldoon Hall - Male (B11)","property_image_4", 4));

        //create our new array adapter
        ArrayAdapter<Property> adapter = new propertyArrayAdapter(this, 0, zoneProperties);

        //Find list view and bind it with the custom adapter
        ListView listView = (ListView) findViewById(R.id.customListView);
        listView.setAdapter(adapter);


        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Property property = zoneProperties.get(position);






              /*  for (int j = 0; j <zones.size(); j++) {
                    if( zones.get(j).getZoneName().equals(property.getZoneName()) ){
                        int count = zones.get(j).getCurrentlyLooking();
                        count++;
                        Property zone = new Property(zones.get(j).getZoneName(), count ,zones.get(j).getTotalSpotsNo());

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                        Query applesQuery = ref.child("zones").orderByChild("zoneName").equalTo(property.getZoneName());

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
            /*    Random r;
                r = new Random();
                int ID = r.nextInt(1000);
                Calendar cal = Calendar.getInstance();

                cal.setTimeZone(TimeZone.getTimeZone("Asia/Qatar"));
                int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                int currentMinute = cal.get(Calendar.MINUTE);


                String id2 = databaseCurrentlyLooking.push().getKey();
                CurrentlyLooking currentlyLook = new CurrentlyLooking(ID,currentHour,currentMinute, property.getZoneName() );
                databaseCurrentlyLooking.child(id2).setValue(currentlyLook);*/

                Intent intent = new Intent(ZoneList2.this, Check_availability.class);
                intent.putExtra("zoneName", property.getZoneName());
             //   intent.putExtra("ID", ID);
                startActivity(intent);
             //   startActivityForResult(intent, 1000);
            }
        };
        //set the listener to the list view
        listView.setOnItemClickListener(adapterViewListener);

    }


    //custom ArrayAdapater
    static class propertyArrayAdapter extends ArrayAdapter<Property>{

        private Context context;
        private List<Property> zoneProperties;

        //constructor, call on creation
        public propertyArrayAdapter(Context context, int resource, ArrayList<Property> objects) {
            super(context, resource, objects);

            this.context = context;
            this.zoneProperties = objects;
        }

        //called when rendering the list
        public View getView(int position, View convertView, ViewGroup parent) {

            //get the property we are displaying
            final Property property = zoneProperties.get(position);

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            //conditionally inflate either standard or special template
            final View view;
            view = inflater.inflate(R.layout.property_layout_alt, null);


            final TextView availability = (TextView) view.findViewById(R.id.textView7);
            final TextView numOfVisit = (TextView) view.findViewById(R.id.textView8);
            TextView description = (TextView) view.findViewById(R.id.description);
            TextView address = (TextView) view.findViewById(R.id.address);
            final ImageView image = (ImageView) view.findViewById(R.id.image);
            final Button button17 = (Button) view.findViewById(R.id.button17) ;
            final Button button18 = (Button) view.findViewById(R.id.button18) ;
            final Button button20 = (Button) view.findViewById(R.id.button20) ;
            final Button button21 = (Button) view.findViewById(R.id.button21) ;
            final ImageView img = (ImageView) view.findViewById(R.id.img) ;

            //set address and description

            address.setText(property.getZoneName());

            //display trimmed excerpt for description
            int descriptionLength = property.getDescription().length();
            if(descriptionLength >= 100){
                String descriptionTrim = property.getDescription().substring(0, 100) + "...";
                description.setText(descriptionTrim);
            }else{
                description.setText(property.getDescription());
            }

            //get the image associated with this property
            int imageID = context.getResources().getIdentifier(property.getImage(), "drawable", context.getPackageName());
            image.setImageResource(imageID);



            final List<Spot> spots;
            spots = new ArrayList<>();

            final List<Property> zones;
            zones = new ArrayList<>();

            DatabaseReference databaseSpots;
            databaseSpots = FirebaseDatabase.getInstance().getReference("spots");

            DatabaseReference databaseZones;
            databaseZones = FirebaseDatabase.getInstance().getReference("zones");

            final List<CurrentlyLooking> currentlyLooking;
            currentlyLooking = new ArrayList<>();


            final DatabaseReference  databaseCurrentlyLooking;
            databaseCurrentlyLooking = FirebaseDatabase.getInstance().getReference("currently looking");



            databaseSpots.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //clearing the previous artist list
                    // if(users != null)
                    spots.clear();

                    //iterating through all the nodes
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //getting artist
                        Spot spot = postSnapshot.getValue(Spot.class);
                        //adding artist to the list
                        spots.add(spot);
                    }
                    int count =0;
                    for (int j = 0; j <spots.size(); j++) {
                        if( spots.get(j).getZoneName().equals(property.getZoneName()) ){  // property.getZoneName()
                            if(spots.get(j).getStatus().equals("available")){
                                count++;
                            }
                        }
                    }
                    if(count == 1){
                        availability.setText("Availability: 25%");
                        button17.setBackgroundColor(Color.RED);
                    }
                    else if (count == 2){
                        availability.setText("Availability: 50%");
                        button17.setBackgroundColor(0xFFFF8516);
                        button18.setBackgroundColor(0xFFFF8516);

                    }
                    else if(count == 3){
                        availability.setText("Availability: 75%");

                        button17.setBackgroundColor(Color.GREEN);
                        button18.setBackgroundColor(Color.GREEN);
                        button20.setBackgroundColor(Color.GREEN);
                    }
                    else if (count == 4){
                        availability.setText("Availability: 100%");
                       button17.setBackgroundColor(Color.GREEN);
                        button18.setBackgroundColor(Color.GREEN);
                        button20.setBackgroundColor(Color.GREEN);
                        button21.setBackgroundColor(Color.GREEN);

                    }
                    else {
                        availability.setText("Availability: 0%");
                    /*  button17.setVisibility(view.INVISIBLE);
                        button18.setVisibility(view.INVISIBLE);
                        button20.setVisibility(view.INVISIBLE);
                        button21.setVisibility(view.INVISIBLE);
                        img.setVisibility(view.VISIBLE);*/

                    }


                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


         /*   databaseZones.addValueEventListener(new ValueEventListener() {


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
                    int count =0;
                    for (int j = 0; j <zones.size(); j++) {
                        if( zones.get(j).getZoneName().equals(property.getZoneName()) )
                            if(zones.get(j).getCurrentlyLooking() == 0)
                            numOfVisit.setText("No one is currently viewing this zone");
                            else if (zones.get(j).getCurrentlyLooking() == 1)
                                numOfVisit.setText(zones.get(j).getCurrentlyLooking() + " person is currently viewing this zone");
                            else
                                numOfVisit.setText(zones.get(j).getCurrentlyLooking() + " people are currently viewing this zone");


                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
            databaseCurrentlyLooking.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //clearing the previous artist list
                    // if(users != null)
                    currentlyLooking.clear();

                    //iterating through all the nodes
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //getting artist
                        CurrentlyLooking current = postSnapshot.getValue(CurrentlyLooking.class);
                        //adding artist to the list
                        currentlyLooking.add(current);
                    }

                    for (int j = 0; j <currentlyLooking.size(); j++) {
                        if( currentlyLooking.get(j).getZoneName().equals(property.getZoneName()) ){
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeZone(TimeZone.getTimeZone("Asia/Qatar"));
                            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                            int currentMinute = cal.get(Calendar.MINUTE);

                            if(currentHour == currentlyLooking.get(j).getHour() && currentMinute - currentlyLooking.get(j).getMinutes()>=2 ){
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                                Query applesQuery = ref.child("currently looking").orderByChild("id").equalTo(currentlyLooking.get(j).getID());

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
                            else if (currentHour > currentlyLooking.get(j).getHour()){
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                                Query applesQuery = ref.child("currently looking").orderByChild("id").equalTo(currentlyLooking.get(j).getID());

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

                    }

                    int count =0;
                    for (int j = 0; j <currentlyLooking.size(); j++) {
                        if( currentlyLooking.get(j).getZoneName().equals(property.getZoneName()) ){
                            count++;
                        }

                    }
                    if(count == 0)
                        numOfVisit.setText("No one is currently viewing this zone");
                    else if (count  == 1)
                        numOfVisit.setText(count + " person is currently viewing this zone");
                    else
                        numOfVisit.setText(count + " people are currently viewing this zone");



                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




            return view;
        }
    }



}
