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

import java.util.ArrayList;
import java.util.List;

public class ZonesList extends AppCompatActivity  {

    private ArrayList<Property> zoneProperties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoneslist_layout);

        //create our property elements
        zoneProperties.add(new Property(10, "CENG FEMALE ZONE ", "Beside college of Engineering ( C07) Female building.",156,"property_image_1", 25,  131,false));
        zoneProperties.add(new Property(66, "CAAS FEMALE ZONE ", "Beside college of Art & Science (C01) Female building.", 3, "property_image_2",112,109, false));
        zoneProperties.add(new Property(1, "CENG MALE ZONE", "Beside college of Engineering ( C07) male building..", 12, "property_image_3",50,38, false));
        zoneProperties.add(new Property(56, "CAAS FEMALE ZONE ", "Infront of college of Art & Science (C01) Female building.,", 88, "property_image_4",112,24, false));

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

                Intent intent = new Intent(ZonesList.this, MainActivity.class);
                intent.putExtra("zoneNumber", property.getZoneNumber());
                intent.putExtra("zoneName", property.getZoneName());
                intent.putExtra("image", property.getImage());
                intent.putExtra("plateNo", getIntent().getStringExtra("plateNo"));

                startActivityForResult(intent, 1000);
            }
        };
        //set the listener to the list view
        listView.setOnItemClickListener(adapterViewListener);


    }

    //custom ArrayAdapater
    class propertyArrayAdapter extends ArrayAdapter<Property>{

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
            Property property = zoneProperties.get(position);

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            //conditionally inflate either standard or special template
            View view;
            if(property.getFeatured() == true){
                view = inflater.inflate(R.layout.property_layout_alt, null);
            }else{
                view = inflater.inflate(R.layout.property_layout, null);
            }


            TextView description = (TextView) view.findViewById(R.id.description);
            TextView address = (TextView) view.findViewById(R.id.address);
            TextView totalSpots = (TextView) view.findViewById(R.id.totalAv);
            TextView reservedSpotsNo= (TextView) view.findViewById(R.id.carspot);
            TextView currentAvailable = (TextView) view.findViewById(R.id.cuurentAvNo);
            ImageView image = (ImageView) view.findViewById(R.id.image);

            //set address and description
            String completeAddress =  property.getZoneName() + " (" +property.getZoneNumber() + ") " ;
            address.setText(completeAddress);

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

            return view;
        }
    }


}
