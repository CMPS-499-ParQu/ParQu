package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;



public class HistoryActivity extends AppCompatActivity {

    Spinner spinner1;
     List<Property> zones;
     String day;
     List<historyInfoPerDay> info ;
    String zoneName;
    BarChart chart;
    String selectedItem;
   List<Reservation> reservations;
    DatabaseReference  databaseReservations;

    DatabaseReference databaseZones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        zones = new ArrayList<>();
      info = new ArrayList<>();
        databaseZones = FirebaseDatabase.getInstance().getReference("zones");
        Intent intent = getIntent();
         zoneName = intent.getStringExtra("zoneName");
        spinner1 = findViewById(R.id.SpotSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(HistoryActivity.this,R.array.days,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        day = spinner1.getSelectedItem().toString();




//        reservations = new ArrayList<>();
//        databaseReservations = FirebaseDatabase.getInstance().getReference("reservations");
//        databaseReservations.addValueEventListener(new ValueEventListener() {
//    public void onDataChange(DataSnapshot dataSnapshot) {
//
//        //clearing the previous artist list
//        // if(users != null)
//        reservations.clear();
//
//        //iterating through all the nodes
//        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//            //getting artist
//            Reservation reservation = postSnapshot.getValue(Reservation.class);
//            //adding artist to the list
//            reservations.add(reservation);
//        }
//
//
//    }
//    @Override
//    public void onCancelled(DatabaseError databaseError) {
//
//    }
//});







//          zones = (List<Property>) intent.getSerializableExtra("zones");
//          int size = zones.size();
//        reservations = (List<Reservation>) intent.getSerializableExtra("zones");
     //   reservations = ZonesList.reservations;
        zones= zoneForHistogram.zones;
        if (zones == null){
            zones= zoneForHistogram2.zones;
        }

        chart =(BarChart) findViewById(R.id.bargraph);
       // chart.setNoDataText("Click to View");


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                 selectedItem = parentView.getItemAtPosition(position).toString();

                List<BarEntry> percentage = new ArrayList<>();
                for (int i =0; i< zones.size(); i++){
                  //  percentage.clear();
                    if (zones.get(i).getZoneName().equals(zoneName)){
                        for(int j=0; j< zones.get(i).getStatistics().size(); j++){
                            if (zones.get(i).getStatistics().get(j).getDay().equals(selectedItem)){

                               // int size = zones.get(i).getHistory().get(j).getInfo().size();


                                for (int h=0; h< zones.get(i).getStatistics().get(j).getHoursInfo().size(); h++ ){

                                    int count =0 ;
                                    for (int k=0; k< zones.get(i).getStatistics().get(j).getHoursInfo().get(h).getCount().size(); k++ ){
                                        count = count + zones.get(i).getStatistics().get(j).getHoursInfo().get(h).getCount().get(k);
                                    }
                                    float percent = (float)count/16;
                                    percentage.add(new BarEntry(percent*100, h));

                                  /*  if(zones.get(i).getStatistics().get(j).getHoursInfo().get(h).getDate() != null){
                                   // int count = zones.get(i).getHistory().get(j).getInfo().get(h).getCount();
                                    int size = zones.get(i).getStatistics().get(j).getHoursInfo().get(h).getDate().size();
                                    Collections.sort(zones.get(i).getStatistics().get(j).getHoursInfo().get(h).getDate());
                                    if(size >=2) {
                                        String date1 = zones.get(i).getStatistics().get(j).getHoursInfo().get(h).getDate().get(size - 1);
                                        String date2 = zones.get(i).getStatistics().get(j).getHoursInfo().get(h).getDate().get(size - 2);
                                        int count=0 ;
                                        for (int l =0; l< reservations.size(); l++){
                                            if (reservations.get(l).getDate().equals(date1) || reservations.get(l).getDate().equals(date2)){
                                                for(int y=0; y<reservations.get(l).getTime().size(); y++){
                                                    if (Integer.toString(reservations.get(l).getTime().get(y)).equals(zones.get(i).getStatistics().get(j).getHoursInfo().get(h).getHour() )){
                                                        count++;
                                                    }
                                                }

                                            }
                                        }

                                        // int countDays = zones.get(i).getHistory().get(j).getInfo().get(h).getDate().size();
                                        float percent = (float)count/8;
                                        percentage.add(new BarEntry(percent*100, h));
                                    }
                                    else{
                                        String date1 = zones.get(i).getStatistics().get(j).getHoursInfo().get(h).getDate().get(size - 1);
                                        int count=0 ;
                                        for (int l =0; l< reservations.size(); l++){
                                            if (reservations.get(l).getDate().equals(date1) ){
                                                for(int y=0; y<reservations.get(l).getTime().size(); y++){
                                                    if (Integer.toString(reservations.get(l).getTime().get(y)).equals(zones.get(i).getStatistics().get(j).getHoursInfo().get(h).getHour() )){
                                                        count++;
                                                    }
                                                }

                                            }
                                        }

                                        // int countDays = zones.get(i).getHistory().get(j).getInfo().get(h).getDate().size();
                                        float percent = (float)count/4;
                                        percentage.add(new BarEntry(percent*100, h));
                                    }

                                    }
                                    else{
                                        percentage.add(new BarEntry(0, h));
                                    }

*/
                                }

                            }
                        }
                    }
                }

             //   BarDataSet set = new BarDataSet(percentage, "BarDataSet");

                ArrayList<String> hours = new ArrayList<>();
                hours.add("6 AM");
                hours.add("7 AM");
                hours.add("8 AM");
                hours.add("9 AM");
                hours.add("10 AM");
                hours.add("11 AM");
                hours.add("12 PM");
                hours.add("13 PM");
                hours.add("14 PM");
                hours.add("15 PM");
                hours.add("16 PM");
                hours.add("17 PM");
                hours.add("18 PM");
                hours.add("19 PM");
                hours.add("20 PM");
                hours.add("21 PM");
                hours.add("22 PM");





                BarDataSet bardataset = new BarDataSet(percentage, "Occupied Percentage");


                //  BarChart chart2 = new BarChart(this);
                //  setContentView(chart2);
                BarData data = new BarData(hours, bardataset);
                chart.setDescription("");
                chart.setData(data); // set the data and list of lables into chart
                chart.invalidate();
//                        chart.setScaleEnabled(true);
//                        chart.setDragEnabled(true);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });







//            percentage.add(new BarEntry(info.get(0).getCount()/(info.get(0).getDate().size()*4), 0));
//            percentage.add(new BarEntry(info.get(1).getCount()/(info.get(1).getDate().size()*4), 1));
//            percentage.add(new BarEntry(info.get(2).getCount()/(info.get(2).getDate().size()*4), 2));
//            percentage.add(new BarEntry(info.get(3).getCount()/(info.get(3).getDate().size()*4), 3));
//            percentage.add(new BarEntry(info.get(4).getCount()/(info.get(4).getDate().size()*4), 4));
//            percentage.add(new BarEntry(info.get(5).getCount()/(info.get(5).getDate().size()*4), 5));
//            percentage.add(new BarEntry(info.get(6).getCount()/(info.get(6).getDate().size()*4), 6));
//            percentage.add(new BarEntry(info.get(7).getCount()/(info.get(7).getDate().size()*4), 7));
//            percentage.add(new BarEntry(info.get(8).getCount()/(info.get(8).getDate().size()*4), 8));
//            percentage.add(new BarEntry(info.get(9).getCount()/(info.get(9).getDate().size()*4), 9));
//            percentage.add(new BarEntry(info.get(10).getCount()/(info.get(10).getDate().size()*4), 10));
//            percentage.add(new BarEntry(info.get(11).getCount()/(info.get(11).getDate().size()*4), 11));
//            percentage.add(new BarEntry(info.get(12).getCount()/(info.get(12).getDate().size()*4), 12));
//            percentage.add(new BarEntry(info.get(13).getCount()/(info.get(13).getDate().size()*4), 13));
//            percentage.add(new BarEntry(info.get(14).getCount()/(info.get(14).getDate().size()*4), 14));
//            percentage.add(new BarEntry(info.get(15).getCount()/(info.get(15).getDate().size()*4), 15));
//            percentage.add(new BarEntry(info.get(16).getCount()/(info.get(16).getDate().size()*4), 16));





    }







}
