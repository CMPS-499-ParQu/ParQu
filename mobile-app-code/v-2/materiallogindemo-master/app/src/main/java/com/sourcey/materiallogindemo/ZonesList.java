package com.sourcey.materiallogindemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ZonesList extends AppCompatActivity  {

    private ArrayList<Property> zoneProperties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoneslist_layout);


        final List<Property> zones;
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

        //create our property elements
        zoneProperties.add(new Property("CBAE Female & Male Zone", 0,"College of Business and Economics Building - (H08)","property_image_1", 4));
        zoneProperties.add(new Property("LIB Female & Male Zone", 0," Library Building - (B13)","property_image_2", 4));

        //create our new array adapter
        ArrayAdapter<Property> adapter = new propertyArrayAdapter(this, 0, zoneProperties);

        //Find list view and bind it with the custom adapter
        ListView listView = (ListView) findViewById(R.id.customListView);
        listView.setAdapter(adapter);


        //add event listener so we can handle clicks
        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Property property = zoneProperties.get(position);


                for (int j = 0; j <zones.size(); j++) {
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

                }

                Intent intent = new Intent(ZonesList.this, MainActivity.class);
                intent.putExtra("zoneName", property.getZoneName());
              //  intent.putExtra("image", property.getImage());
                intent.putExtra("plateNo", getIntent().getStringExtra("plateNo"));

                startActivity(intent);
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
            View view;

                view = inflater.inflate(R.layout.property_layout, null);


            TextView description = (TextView) view.findViewById(R.id.description);
            TextView address = (TextView) view.findViewById(R.id.address);
          /*  TextView totalSpots = (TextView) view.findViewById(R.id.totalAv);
            TextView reservedSpotsNo= (TextView) view.findViewById(R.id.carspot);
            TextView currentAvailable = (TextView) view.findViewById(R.id.cuurentAvNo);*/
            ImageView image = (ImageView) view.findViewById(R.id.image);

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

            //set availability statistics
          //  currentAvailable.setText( String.valueOf(property.getcurrentAvailableNo())+ " Parking Still available");
            //totalSpots.setText("Total Number of spots: " + String.valueOf(property.getAvailableSpotsNo()));
           // reservedSpotsNo.setText("Reserved parkings: " + String.valueOf(property.getEnteredCarsNo()));

            //get the image associated with this property
            int imageID = context.getResources().getIdentifier(property.getImage(), "drawable", context.getPackageName());
            image.setImageResource(imageID);

            final List<Property> zones;
            zones = new ArrayList<>();

            DatabaseReference databaseZones;
            databaseZones = FirebaseDatabase.getInstance().getReference("zones");

            final TextView numOfVisit = (TextView) view.findViewById(R.id.textView8);

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
            });


            return view;
        }
    }


}
