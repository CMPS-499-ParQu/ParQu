package com.sourcey.materiallogindemo;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements android.widget.AdapterView.OnItemSelectedListener{

    Spinner spinner2;
    List<Property> zones2;
    String day2;
    List<historyInfoPerDay> info2 ;

    BarChart chart;
    String selectedItem;

    ArrayList<Statistics> history;
    ArrayList<historyInfoPerDay> info;
    int ID;
    DatabaseReference  databaseCurrentlyLooking;
    int resNo;
    String startTime ;
     String hours ;
    List<User> users;
    List<Reservation> reservations;
    DatabaseReference databaseUsers, databaseReservations;

    String email, plateNo;
    TextView text;
    Button button0, button1, button2, button3, button4, button5 , button6, button7, button8 , button9, button10, button11 , button12, button13, button14 , button15, button29;
    User user;
    Random r;
    String selectedDate = " ";
   final int [] fixedTime =  {6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21};
    final int [] fixedTimeCounter =new int[fixedTime.length];
    Reservation reservation;
    int day;

    List<Integer> time = new ArrayList<>() ;

    private User userLogged = Choices.user;
    Spinner spinner1;
    public static final String TAG= "MainActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListner;

    String zoneName, uid ;

    List<Property> zones;
    DatabaseReference databaseZones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseCurrentlyLooking = FirebaseDatabase.getInstance().getReference("currently looking");
        zones = new ArrayList<>();
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

        zoneName= getIntent().getStringExtra("zoneName");

        button0 =(Button) findViewById(R.id.button0);
        button1 =(Button) findViewById(R.id.button1);
        button2 =(Button) findViewById(R.id.button2);
        button3 =(Button) findViewById(R.id.button3);
        button4 =(Button) findViewById(R.id.button4);
        button5 =(Button) findViewById(R.id.button5);
        button6 =(Button) findViewById(R.id.button6);
        button7 =(Button) findViewById(R.id.button7);
        button8 =(Button) findViewById(R.id.button8);
        button9 =(Button) findViewById(R.id.button9);
        button10 =(Button) findViewById(R.id.button10);
        button11=(Button) findViewById(R.id.button11);
        button12 =(Button) findViewById(R.id.button12);
        button13 =(Button) findViewById(R.id.button13);
        button14 =(Button) findViewById(R.id.button14);
        button29 =(Button) findViewById(R.id.button29);



        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseReservations = FirebaseDatabase.getInstance().getReference("reservations");
        users = new ArrayList<>();
        reservations = new ArrayList<>();

        Intent intent = getIntent();
        //text = (TextView) findViewById(R.id.t-1);
        zoneName = intent.getStringExtra("zoneName");



        databaseReservations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                // if(users != null)
                reservations.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Reservation reservation = postSnapshot.getValue(Reservation.class);
                    //adding artist to the list
                    reservations.add(reservation);
                }

                    for (int x=0; x< fixedTimeCounter.length; x++) {
                        fixedTimeCounter[x] = 0;
                    }
                    for (int k=0; k< fixedTime.length;  k++){
                        for(int i=0;  i< reservations.size(); i++)
                        {
                            if (reservations.get(i).getDate().equals(selectedDate) && reservations.get(i).getZoneName().equals(zoneName) && !"cancelled".equals(reservations.get(i).getStatus()) && !"subcancelled".equals(reservations.get(i).getStatus())){
                                for (int j=0; j< reservations.get(i).getTime().size() ;j++){
                                    if (reservations.get(i).getTime().get(j).equals(fixedTime[k]) ){
                                        fixedTimeCounter[k]= fixedTimeCounter[k]+1;
                                    }
                                }
                            }

                            if (reservations.get(i).getDate().equals(selectedDate) && reservations.get(i).getZoneName().equals(zoneName)  && "subcancelled".equals(reservations.get(i).getStatus())){
                                for (int j=0; j< reservations.get(i).getTime().size()-reservations.get(i).cancelledHours ;j++){
                                    if (reservations.get(i).getTime().get(j).equals(fixedTime[k]) ){
                                        fixedTimeCounter[k]= fixedTimeCounter[k]+1;
                                    }
                                }
                            }


                            }



                        if ((4 - fixedTimeCounter[0]) == 0) {
                            button29.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[0]) == 1) {
                            button29.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[0]) == 2){
                            button29.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button29.setBackgroundColor(0xFF00AA4A);// green
                        }



                        if ((4 - fixedTimeCounter[1]) == 0) {
                            button0.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[1]) == 1) {
                            button0.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[1]) == 2){
                            button0.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button0.setBackgroundColor(0xFF00AA4A);// green
                        }


                        if ((4 - fixedTimeCounter[2]) == 0) {
                            button1.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[2]) == 1) {
                            button1.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[2]) == 2){
                            button1.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button1.setBackgroundColor(0xFF00AA4A);// green
                        }



                        if ((4 - fixedTimeCounter[3]) == 0) {
                            button2.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[3]) == 1) {
                            button2.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[3]) == 2){
                            button2.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button2.setBackgroundColor(0xFF00AA4A);// green
                        }



                        if ((4 - fixedTimeCounter[4]) == 0) {
                            button3.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[4]) == 1) {
                            button3.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[4]) == 2){
                            button3.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button3.setBackgroundColor(0xFF00AA4A);// green
                        }


                        if ((4 - fixedTimeCounter[5]) == 0) {
                            button4.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[5]) == 1) {
                            button4.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[5]) == 2){
                            button4.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button4.setBackgroundColor(0xFF00AA4A);// green
                        }


                        if ((4 - fixedTimeCounter[6]) == 0) {
                            button5.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[6]) == 1) {
                            button5.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[6]) == 2){
                            button5.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button5.setBackgroundColor(0xFF00AA4A);// green
                        }


                        if ((4 - fixedTimeCounter[7]) == 0) {
                            button6.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[7]) == 1) {
                            button6.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[7]) == 2){
                            button6.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button6.setBackgroundColor(0xFF00AA4A);// green
                        }


                        if ((4 - fixedTimeCounter[8]) == 0) {
                            button7.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[8]) == 1) {
                            button7.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[8]) == 2){
                            button7.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button7.setBackgroundColor(0xFF00AA4A);// green
                        }


                        if ((4 - fixedTimeCounter[9]) == 0) {
                            button8.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[9]) == 1) {
                            button8.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[9]) == 2){
                            button8.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button8.setBackgroundColor(0xFF00AA4A);// green
                        }


                        if ((4 - fixedTimeCounter[10]) == 0) {
                            button9.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[10]) == 1) {
                            button9.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[10]) == 2){
                            button9.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button9.setBackgroundColor(0xFF00AA4A);// green
                        }



                        if ((4 - fixedTimeCounter[11]) == 0) {
                            button10.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[11]) == 1) {
                            button10.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[11]) == 2){
                            button10.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button10.setBackgroundColor(0xFF00AA4A);// green
                        }


                        if ((4 - fixedTimeCounter[12]) == 0) {
                            button11.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[12]) == 1) {
                            button11.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[12]) == 2){
                            button11.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button11.setBackgroundColor(0xFF00AA4A);// green
                        }


                        if ((4 - fixedTimeCounter[13]) == 0) {
                            button12.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[13]) == 1) {
                            button12.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[13]) == 2){
                            button12.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button12.setBackgroundColor(0xFF00AA4A);// green
                        }


                        if ((4 - fixedTimeCounter[14]) == 0) {
                            button13.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[14]) == 1) {
                            button13.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[14]) == 2){
                            button13.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button13.setBackgroundColor(0xFF00AA4A);// green
                        }

                        if ((4 - fixedTimeCounter[15]) == 0) {
                            button14.setBackgroundColor(0xffd6d7d7);//gray
                        }
                        else if ((4 - fixedTimeCounter[15]) == 1) {
                            button14.setBackgroundColor(0xFFDB0101);//red
                        }
                        else if ((4 - fixedTimeCounter[15]) == 2){
                            button14.setBackgroundColor(0xFFF77824);//orang
                        }
                        else
                        {
                            button14.setBackgroundColor(0xFF00AA4A);// green
                        }

                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


      /*  if (plateNo == null){
            email = intent.getStringExtra("email");
            databaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //clearing the previous artist list
                    // if(users != null)
                    users.clear();

                    //iterating through all the nodes
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //getting artist
                        User user = postSnapshot.getValue(User.class);
                        //adding artist to the list
                        users.add(user);
                    }

                    for(int i=0; i< users.size(); i++){
                        if (users.get(i).getEmail().equals(email)){
                            plateNo = users.get(i).getPlateNo();
                            // text.setText(plateNo);
                            // Log.d("plateNo","this is plateNo"+ plateNo);
                        }


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
*/




         r = new Random();


        Spinner spinner1 = findViewById(R.id.startingTimeSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.starting_time,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                startTime = "";
                 startTime = adapterView.getItemAtPosition(position).toString();

              if (startTime != null) {
                  Spinner spinner2 = findViewById(R.id.endingTimeSpinner);
                  ArrayAdapter<CharSequence> adapter2;
                  if (startTime.equals("18")){
                      adapter2 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ending_time18,android.R.layout.simple_spinner_item);

                  }
                  else if (startTime.equals("19")){
                      adapter2 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ending_time19,android.R.layout.simple_spinner_item);

                  }
                  else if (startTime.equals("20")){
                      adapter2 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ending_time20,android.R.layout.simple_spinner_item);

                  }
                  else if (startTime.equals("21")){
                      adapter2 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ending_time21,android.R.layout.simple_spinner_item);

                  }
                  else if (startTime.equals("17")){
                      adapter2 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ending_time22,android.R.layout.simple_spinner_item);

                  }
                  else
                  {
                      adapter2 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ending_time,android.R.layout.simple_spinner_item);

                  }
                  adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                  spinner2.setAdapter(adapter2);

                  spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                      @Override
                      public void onItemSelected(AdapterView<?> adapterView, View view,
                                                 int position, long id) {
                          hours = "";
                          hours = adapterView.getItemAtPosition(position).toString();
                          if (hours != null) {
                   /* Toast.makeText(MainActivity.this, hours,
                            Toast.LENGTH_SHORT).show();*/

                          }


                      }

                      @Override
                      public void onNothingSelected(AdapterView<?> adapterView) {
                          // TODO Auto-generated method stub

                      }
                  });

                  //  Toast.makeText(MainActivity.this, startTime,
                   //         Toast.LENGTH_SHORT).show();
               /*   if(startTime.equals("18")){
                      Spinner spinner2 = findViewById(R.id.endingTimeSpinner);
                      ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ending_time18,android.R.layout.simple_spinner_item);
                      adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                      spinner2.setAdapter(adapter2);

                      spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                          @Override
                          public void onItemSelected(AdapterView<?> adapterView, View view,
                                                     int position, long id) {
                              hours = "";
                              hours = adapterView.getItemAtPosition(position).toString();
                              if (hours != null) {
                   // Toast.makeText(MainActivity.this, hours,
                    //        Toast.LENGTH_SHORT).show();

                              }


                          }

                          @Override
                          public void onNothingSelected(AdapterView<?> adapterView) {
                              // TODO Auto-generated method stub

                          }
                      });

                  }
                  else if (startTime.equals("19")){
                      Spinner spinner2 = findViewById(R.id.endingTimeSpinner);
                      ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ending_time19,android.R.layout.simple_spinner_item);
                      adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                      spinner2.setAdapter(adapter2);

                      spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                          @Override
                          public void onItemSelected(AdapterView<?> adapterView, View view,
                                                     int position, long id) {
                              hours = "";
                              hours = adapterView.getItemAtPosition(position).toString();
                              if (hours != null) {
                   // Toast.makeText(MainActivity.this, hours,
                     //       Toast.LENGTH_SHORT).show();

                              }


                          }

                          @Override
                          public void onNothingSelected(AdapterView<?> adapterView) {
                              // TODO Auto-generated method stub

                          }
                      });
                  }

                  else if (startTime.equals("20")){
                      Spinner spinner2 = findViewById(R.id.endingTimeSpinner);
                      ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ending_time20,android.R.layout.simple_spinner_item);
                      adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                      spinner2.setAdapter(adapter2);

                      spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                          @Override
                          public void onItemSelected(AdapterView<?> adapterView, View view,
                                                     int position, long id) {
                              hours = "";
                              hours = adapterView.getItemAtPosition(position).toString();
                              if (hours != null) {
                   // Toast.makeText(MainActivity.this, hours,
                    //        Toast.LENGTH_SHORT).show();

                              }


                          }

                          @Override
                          public void onNothingSelected(AdapterView<?> adapterView) {
                              // TODO Auto-generated method stub

                          }
                      });
                  }

                  else if (startTime.equals("21")){
                      Spinner spinner2 = findViewById(R.id.endingTimeSpinner);
                      ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ending_time21,android.R.layout.simple_spinner_item);
                      adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                      spinner2.setAdapter(adapter2);

                      spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                          @Override
                          public void onItemSelected(AdapterView<?> adapterView, View view,
                                                     int position, long id) {
                              hours = "";
                              hours = adapterView.getItemAtPosition(position).toString();
                              if (hours != null) {
                   // Toast.makeText(MainActivity.this, hours,
                    //        Toast.LENGTH_SHORT).show();

                              }


                          }

                          @Override
                          public void onNothingSelected(AdapterView<?> adapterView) {
                              // TODO Auto-generated method stub

                          }
                      });
                  }

                  else if (startTime.equals("22")){
                      Spinner spinner2 = findViewById(R.id.endingTimeSpinner);
                      ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ending_time22,android.R.layout.simple_spinner_item);
                      adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                      spinner2.setAdapter(adapter2);

                      spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                          @Override
                          public void onItemSelected(AdapterView<?> adapterView, View view,
                                                     int position, long id) {
                              hours = "";
                              hours = adapterView.getItemAtPosition(position).toString();
                              if (hours != null) {
                   // Toast.makeText(MainActivity.this, hours,
                     //       Toast.LENGTH_SHORT).show();

                              }


                          }

                          @Override
                          public void onNothingSelected(AdapterView<?> adapterView) {
                              // TODO Auto-generated method stub

                          }
                      });
                  }*/
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });


        //Spinner 2 ---------------------------
/*        Spinner spinner2 = findViewById(R.id.endingTimeSpinner);
        ArrayAdapter<CharSequence> adapter2;
        if (startTime.equals("18")){
          adapter2 = ArrayAdapter.createFromResource(this,R.array.ending_time18,android.R.layout.simple_spinner_item);

        }
        else if (startTime.equals("19")){
           adapter2 = ArrayAdapter.createFromResource(this,R.array.ending_time19,android.R.layout.simple_spinner_item);

        }
        else if (startTime.equals("20")){
        adapter2 = ArrayAdapter.createFromResource(this,R.array.ending_time20,android.R.layout.simple_spinner_item);

        }
       else if (startTime.equals("21")){
      adapter2 = ArrayAdapter.createFromResource(this,R.array.ending_time21,android.R.layout.simple_spinner_item);

        }
        else if (startTime.equals("22")){
       adapter2 = ArrayAdapter.createFromResource(this,R.array.ending_time22,android.R.layout.simple_spinner_item);

        }
        else
        {
           adapter2 = ArrayAdapter.createFromResource(this,R.array.ending_time,android.R.layout.simple_spinner_item);

        }
       adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                hours = "";
                hours = adapterView.getItemAtPosition(position).toString();
                if (hours != null) {
                   // Toast.makeText(MainActivity.this, hours,
                    //        Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

*/
        //------------------DATE---------------
        mDisplayDate = findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);


        mDateSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, " onDateSet: dd/mm/yy: " + year + "/" + month + "/" + dayOfMonth);
                month = month + 1;
               String month2 = Integer.toString(month);
               String day = Integer.toString(dayOfMonth);

                if(month < 10){

                    month2 = "0" + month2;
                }
                if(dayOfMonth < 10){

                    day  = "0" + day ;
                }
                selectedDate = year + "-" + month2 + "-" + day;
                mDisplayDate.setText(selectedDate);

                for (int x = 0; x < fixedTimeCounter.length; x++) {
                    fixedTimeCounter[x] = 0;
                }

                for (int k = 0; k < fixedTime.length; k++) {
                    for (int i = 0; i < reservations.size(); i++) {

//                        if (reservations.get(i).getDate().equals(selectedDate) && reservations.get(i).getZoneName().equals(zoneName) && !"canceled".equals(reservations.get(i).getStatus())) {
//                            for (int j = 0; j < reservations.get(i).getTime().size(); j++) {
//                                if (reservations.get(i).getTime().get(j).equals(fixedTime[k])) {
//                                    fixedTimeCounter[k] = fixedTimeCounter[k] + 1;
//                                }
//                            }
//                        }

                        if (reservations.get(i).getDate().equals(selectedDate) && reservations.get(i).getZoneName().equals(zoneName) && !"cancelled".equals(reservations.get(i).getStatus()) && !"subcancelled".equals(reservations.get(i).getStatus())){
                            for (int j=0; j< reservations.get(i).getTime().size() ;j++){
                                if (reservations.get(i).getTime().get(j).equals(fixedTime[k]) ){
                                    fixedTimeCounter[k]= fixedTimeCounter[k]+1;
                                }
                            }
                        }

                        if (reservations.get(i).getDate().equals(selectedDate) && reservations.get(i).getZoneName().equals(zoneName)  && "subcancelled".equals(reservations.get(i).getStatus())){
                            for (int j=0; j< reservations.get(i).getTime().size()-reservations.get(i).cancelledHours ;j++){
                                if (reservations.get(i).getTime().get(j).equals(fixedTime[k]) ){
                                    fixedTimeCounter[k]= fixedTimeCounter[k]+1;
                                }
                            }
                        }
                    }
                }

                if ((4 - fixedTimeCounter[0]) == 0) {
                    button29.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[0]) == 1) {
                    button29.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[0]) == 2){
                    button29.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button29.setBackgroundColor(0xFF00AA4A);// green
                }



                if ((4 - fixedTimeCounter[1]) == 0) {
                    button0.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[1]) == 1) {
                    button0.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[1]) == 2){
                    button0.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button0.setBackgroundColor(0xFF00AA4A);// green
                }


                if ((4 - fixedTimeCounter[2]) == 0) {
                    button1.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[2]) == 1) {
                    button1.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[2]) == 2){
                    button1.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button1.setBackgroundColor(0xFF00AA4A);// green
                }



                if ((4 - fixedTimeCounter[3]) == 0) {
                    button2.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[3]) == 1) {
                    button2.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[3]) == 2){
                    button2.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button2.setBackgroundColor(0xFF00AA4A);// green
                }



                if ((4 - fixedTimeCounter[4]) == 0) {
                    button3.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[4]) == 1) {
                    button3.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[4]) == 2){
                    button3.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button3.setBackgroundColor(0xFF00AA4A);// green
                }


                if ((4 - fixedTimeCounter[5]) == 0) {
                    button4.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[5]) == 1) {
                    button4.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[5]) == 2){
                    button4.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button4.setBackgroundColor(0xFF00AA4A);// green
                }


                if ((4 - fixedTimeCounter[6]) == 0) {
                    button5.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[6]) == 1) {
                    button5.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[6]) == 2){
                    button5.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button5.setBackgroundColor(0xFF00AA4A);// green
                }


                if ((4 - fixedTimeCounter[7]) == 0) {
                    button6.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[7]) == 1) {
                    button6.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[7]) == 2){
                    button6.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button6.setBackgroundColor(0xFF00AA4A);// green
                }


                if ((4 - fixedTimeCounter[8]) == 0) {
                    button7.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[8]) == 1) {
                    button7.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[8]) == 2){
                    button7.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button7.setBackgroundColor(0xFF00AA4A);// green
                }


                if ((4 - fixedTimeCounter[9]) == 0) {
                    button8.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[9]) == 1) {
                    button8.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[9]) == 2){
                    button8.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button8.setBackgroundColor(0xFF00AA4A);// green
                }


                if ((4 - fixedTimeCounter[10]) == 0) {
                    button9.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[10]) == 1) {
                    button9.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[10]) == 2){
                    button9.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button9.setBackgroundColor(0xFF00AA4A);// green
                }



                if ((4 - fixedTimeCounter[11]) == 0) {
                    button10.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[11]) == 1) {
                    button10.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[11]) == 2){
                    button10.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button10.setBackgroundColor(0xFF00AA4A);// green
                }


                if ((4 - fixedTimeCounter[12]) == 0) {
                    button11.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[12]) == 1) {
                    button11.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[12]) == 2){
                    button11.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button11.setBackgroundColor(0xFF00AA4A);// green
                }


                if ((4 - fixedTimeCounter[13]) == 0) {
                    button12.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[13]) == 1) {
                    button12.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[13]) == 2){
                    button12.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button12.setBackgroundColor(0xFF00AA4A);// green
                }


                if ((4 - fixedTimeCounter[14]) == 0) {
                    button13.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[14]) == 1) {
                    button13.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[14]) == 2){
                    button13.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button13.setBackgroundColor(0xFF00AA4A);// green
                }

                if ((4 - fixedTimeCounter[15]) == 0) {
                    button14.setBackgroundColor(0xffd6d7d7);//gray
                }
                else if ((4 - fixedTimeCounter[15]) == 1) {
                    button14.setBackgroundColor(0xFFDB0101);//red
                }
                else if ((4 - fixedTimeCounter[15]) == 2){
                    button14.setBackgroundColor(0xFFF77824);//orang
                }
                else
                {
                    button14.setBackgroundColor(0xFF00AA4A);// green
                }
            }


        };

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListner,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });





        zones2 = new ArrayList<>();
        info2 = new ArrayList<>();
        databaseZones = FirebaseDatabase.getInstance().getReference("zones");

        spinner2 = findViewById(R.id.SpotSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(MainActivity.this,R.array.days,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        day2 = spinner2.getSelectedItem().toString();




        zones2= ZonesList.zones;
        if (zones2 == null){
          //  zones2= zoneForHistogram2.zones;
        }

        chart =(BarChart) findViewById(R.id.bargraph);
        // chart.setNoDataText("Click to View");


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedItem = parentView.getItemAtPosition(position).toString();

                List<BarEntry> percentage = new ArrayList<>();
                for (int i =0; i< zones2.size(); i++){
                    //  percentage.clear();
                    if (zones2.get(i).getZoneName().equals(zoneName)){
                        for(int j=0; j< zones2.get(i).getStatistics().size(); j++){
                            if (zones2.get(i).getStatistics().get(j).getDay().equals(selectedItem)){

                                // int size = zones.get(i).getHistory().get(j).getInfo().size();


                                for (int h=0; h< zones2.get(i).getStatistics().get(j).getHoursInfo().size(); h++ ){

                                    int count =0 ;
                                    for (int k=0; k< zones2.get(i).getStatistics().get(j).getHoursInfo().get(h).getCount().size(); k++ ){
                                        count = count + zones2.get(i).getStatistics().get(j).getHoursInfo().get(h).getCount().get(k);
                                    }
                                    float percent = (float)count/16;
                                    int result = (int) (percent*100);
                                    percentage.add(new BarEntry(result, h));

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
                int color = ContextCompat.getColor(MainActivity.this, R.color.chart);
                bardataset.setColor(color);

                //  BarChart chart2 = new BarChart(this);
                //  setContentView(chart2);
                BarData data = new BarData(hours, bardataset);

                chart.setDescription("");
                chart.getAxisRight().setDrawGridLines(false);
                chart.getAxisLeft().setDrawGridLines(false);
                chart.getXAxis().setDrawGridLines(false);
                YAxis rightYAxis = chart.getAxisRight();
                rightYAxis.setEnabled(false);
                YAxis leftYAxis = chart.getAxisLeft();
                leftYAxis.setEnabled(false);

                XAxis topXAxis = chart.getXAxis();
               // topXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                chart.setData(data); // set the data and list of lables into chart
                chart.invalidate();
//                        chart.setScaleEnabled(true);
//                        chart.setDragEnabled(true);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });





    }







    public void createReservation(View v ){

        if (!selectedDate.equals("") && !startTime.equals("-- Select --")  && !hours.equals("-- Select --")) {

            resNo = r.nextInt(1000);

            reservation = new Reservation(resNo, userLogged.getPlateNo(), zoneName, selectedDate, arrayOfTime(startTime, hours), "created", arrayOfTime(startTime, hours).size() * 5, 0, 0, userLogged.getUid());

            List<Integer> count = new ArrayList<>();

            int[] counters = new int[time.size()];
            for (int i = 0; i < counters.length; i++) {
                counters[i] = 0;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date strDate = null;
            try {
                strDate = sdf.parse(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date currentDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("Asia/Qatar"));
            int currentHour = cal.get(Calendar.HOUR_OF_DAY);

            if (strDate.getDay() - currentDate.getDay() == 1) {

                for (int k = 0; k < time.size(); k++) {
                    for (int i = 0; i < reservations.size(); i++) {
                        if (reservations.get(i).getDate().equals(selectedDate) && reservations.get(i).getZoneName().equals(zoneName) && !"canceled".equals(reservations.get(i).getStatus())) {
                            for (int j = 0; j < reservations.get(i).getTime().size(); j++) {
                                if (reservations.get(i).getTime().get(j).equals(time.get(k))) {
                                    counters[k] = counters[k] + 1;
                                }
                            }
                        }
                    }
                }

                String alertMessage = "No available parking at \n";
                boolean exist = true;

                for (int i = 0; i < counters.length; i++) {

                    if ((4 - counters[i]) <= 0) {
                        exist = false;
                        //  Toast.makeText(MainActivity.this, " no available parking at" + time.get(i),
                        //   Toast.LENGTH_LONG).show();
                        alertMessage = alertMessage + time.get(i) + ":00 \n";

                    }
                    if (i == counters.length - 1 && exist == false) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(alertMessage + "");
                        builder.setTitle("Error");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog a = builder.create();
                        a.show();

                        return;

                    }

                }
            } else if (strDate.getDay() - currentDate.getDay() == 0 && Integer.parseInt(startTime) - currentHour >= 1) {

                for (int k = 0; k < time.size(); k++) {
                    for (int i = 0; i < reservations.size(); i++) {
                        if (reservations.get(i).getDate().equals(selectedDate) && reservations.get(i).getZoneName().equals(zoneName) && !"canceled".equals(reservations.get(i).getStatus())) {
                            for (int j = 0; j < reservations.get(i).getTime().size(); j++) {
                                if (reservations.get(i).getTime().get(j).equals(time.get(k))) {
                                    counters[k] = counters[k] + 1;
                                }
                            }
                        }
                    }
                }

                String alertMessage = "No available parking at \n";
                boolean exist = true;

                for (int i = 0; i < counters.length; i++) {

                    if ((4 - counters[i]) <= 0) {
                        exist = false;
                        //  Toast.makeText(MainActivity.this, " no available parking at" + time.get(i),
                        //   Toast.LENGTH_LONG).show();
                        alertMessage = alertMessage + time.get(i) + ":00 \n";

                    }
                    if (i == counters.length - 1 && exist == false) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(alertMessage + "");
                        builder.setTitle("Error");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog a = builder.create();
                        a.show();

                        return;

                    }

                }
            }
  /*      else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("You can not register at this time");
            builder.setTitle("Error");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog a = builder.create();
            a.show();

            return;
        }*/



   /*   boolean registeredBefore = false;
        String alertMessage2 = "You already has a reservation  at \n" ;
        for (int i =0; i< reservations.size(); i++){
            if (reservations.get(i).getCarPlateNo().equals(userLogged.getPlateNo()) && reservations.get(i).getData().equals(selectedDate)){
                for (int j=0; j< reservations.get(i).getTime().size() ;j++){
                    for (int k=0; k< time.size(); k++){
                        if (reservations.get(i).getTime().get(j).equals(time.get(k)) ){
                            registeredBefore = true;
                            alertMessage2 =  alertMessage2 + time.get(k)+":00 \n";
                        }
                    }
                }
            }
        }

        if (registeredBefore == true){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(alertMessage2+"");
            builder.setTitle("Error");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog a = builder.create();
            a.show();

            return;
        }
*/

            int totalReservationHours = 0;
            for (int i = 0; i < reservations.size(); i++) {
                if (reservations.get(i).getCarPlateNo().equals(userLogged.getPlateNo()) && reservations.get(i).getDate().equals(selectedDate)) {
                    totalReservationHours += (reservations.get(i).getTime().size() - reservations.get(i).getExtendedHours() - reservations.get(i).getCancelledHours());
                }
            }
            if ((totalReservationHours + Integer.parseInt(hours)) > 6) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("You exceed the total allowed number of hours which is 6 hours per day.\n" + "Total reservation hours for you are: " + totalReservationHours);
                builder.setTitle("Error");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog a = builder.create();
                a.show();

                return;

            }


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to reserve");
            builder.setTitle("Confirmation Message");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Notification notification = buildNotification(reservation);
                    scheduleNotification(notification, selectedDate, startTime, hours, reservation);


                    statisticUpdate(selectedDate, startTime, hours);
                    //deleteNotification(reservation);

                    String id = databaseReservations.push().getKey();
                    databaseReservations.child(id).setValue(reservation);
                    finish();

                    Intent toChoices = new Intent(MainActivity.this, Choices.class);
                    //   toLoginIntent.putExtra("plateNo", plateNo);
                    startActivity(toChoices);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog a = builder.create();
            a.show();


        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Make sure that all the filed are not empty");
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
    }

    public List<Integer> arrayOfTime (String startTime , String hours){
        time.clear();
        int startTime1 = Integer.parseInt(startTime) ;
        int hours1 = Integer.parseInt(hours) ;

        for (int i= 0 ; i< hours1 ; i++){
            time.add(startTime1 + i);

        }
        return time ;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    protected void onStop() {
//        super.onStop();
//
//        for (int j = 0; j <zones.size(); j++) {
//            if (zones.get(j).getZoneName().equals(zoneName)) {
//                int count = zones.get(j).getCurrentlyLooking();
//                count--;
//                Property zone = new Property(zones.get(j).getZoneName(), count, zones.get(j).getTotalSpotsNo());
//
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//
//                Query applesQuery = ref.child("zones").orderByChild("zoneName").equalTo(zoneName);
//
//                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
//                            appleSnapshot.getRef().removeValue();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//
//                String id2 = databaseZones.push().getKey();
//                databaseZones.child(id2).setValue(zone);
//                finish();
//
//            }
//        }
//    }
    public void statisticUpdate (String selectedDate,String startTime,String hours){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        history = new ArrayList<>() ;
        info = new ArrayList<>();
        history.clear();
        info.clear();

        for (int j = 0; j <zones.size(); j++) {
            if (zones.get(j).getZoneName().equals(zoneName))
            {
                for (int i =0; i< zones.get(j).getStatistics().size(); i++){
                    List<Integer> userHours = arrayOfTime(startTime,hours);
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(selectedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat format2=new SimpleDateFormat("EE");
                    String finalDay=format2.format(dt1);
                    if (zones.get(j).getStatistics().get(i).getDay().equals(finalDay) )
                    {
                        for (int k =0 ; k < zones.get(j).getStatistics().get(i).getHoursInfo().size(); k++) {
                            boolean flage = false ;
                            for(int l=0; l< userHours.size(); l++) {
                                if (userHours.get(l) == Integer.parseInt(zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getHour())) {

                                    //String[] parts = zones.get(j).getHistory().get(i).getInfo().get(k).getCount().split("-");
                                    flage = true;
                                    if (zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getDate() != null && zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getCount()!= null) {
                                        ArrayList<Integer> countNew = zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getCount();
                                        if (zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getDate().contains(selectedDate)) {
                                            int index = zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getDate().indexOf(selectedDate);
                                            countNew.set(index, countNew.get(index)+1);
                                            info.add(new historyInfoPerDay(zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getHour(), countNew  ,zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getDate() ));
                                        } else {
                                            ArrayList<String> dates = zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getDate();
                                            //Collections.sort(dates);

                                            dates.set(0,dates.get(1));
                                            dates.set(1,dates.get(2));
                                            dates.set(2,dates.get(3));
                                            dates.set(3, selectedDate);

                                            countNew.set(0, countNew.get(1));
                                            countNew.set(1, countNew.get(2));
                                            countNew.set(2, countNew.get(3));
                                            countNew.set(3, 1);
                                            /*if (dates.size()<4){
                                                dates.add(selectedDate);
                                                int index = dates.indexOf(selectedDate);
                                                countNew.add(1);
                                            }
                                            else{
                                                dates.set(0, selectedDate);
                                                countNew.set(0,1);
                                            }*/

                                            info.add(new historyInfoPerDay(zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getHour(), countNew, dates));
                                        }
                                    } else {
                                        ArrayList<Integer> countNew = new ArrayList<>();
                                        ArrayList<String> dates = new ArrayList<>();
                                        dates.add(selectedDate);
                                        countNew.add(1);
                                        info.add(new historyInfoPerDay(zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getHour(), countNew, dates));
                                    }
                                }
                            }

                            if (flage == false){
                                info.add(zones.get(j).getStatistics().get(i).getHoursInfo().get(k));
                            }

                        }

                        history.add(new Statistics(finalDay, info));

                    }
                    else{
                        history.add(zones.get(j).getStatistics().get(i));
                    }
                }
            }
        }

        long startTime3 = System.nanoTime();
        Property property= new Property(zoneName,4,history);

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
        long endTime3 = System.nanoTime();
        Log.i("Time", "Delete zone took "+(endTime3 - startTime3) + " ns" );
        long startTime2 = System.nanoTime();
        String id = databaseZones.push().getKey();
        databaseZones.child(id).setValue(property);
        long endTime2 = System.nanoTime();
        Log.i("Time", "Post zone took "+(endTime2 - startTime2) + " ns" );
        finish();
        return;

    }



@Override
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



    private void scheduleNotification(Notification notification, String date, String startTime, String hours, Reservation reservation) {

        Intent intent = new Intent(this, AlarmNotificationReceiver.class);
        intent.putExtra(AlarmNotificationReceiver.NOTIF_ID, 1);
        intent.putExtra(AlarmNotificationReceiver.NOTIF, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reservation.getResNo(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //convert assignment date string, subtract 1 day to be used in notification
        String[] dueDate = reservation.getDate().split("-");
        Calendar calendar = Calendar.getInstance();
        int hour = (Integer.parseInt(startTime)+ Integer.parseInt(hours))-1;
        calendar.set(Integer.valueOf(dueDate[0]), (Integer.valueOf(dueDate[1]) - 1), Integer.valueOf(dueDate[2]), (Integer.parseInt(startTime)+ Integer.parseInt(hours))-1,30, 0);

        //subtract one day


        long futureInMillis = calendar.getTimeInMillis();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = null;
        try {
            strDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);

    }

    private Notification buildNotification(Reservation reservation) {
         final long[] mVibratePattern = { 0, 200, 200, 300 };
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "Studyr",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Studyr app notification channel");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
               .setSmallIcon(R.drawable.ic_notifications) // notification icon
                .setContentTitle("Warning") // title for notification
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("Your reservation No "+ reservation.getResNo() + " in zone "+ reservation.getZoneName()+ " is due after 30 minutes")// message for notification
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Your reservation No "+ reservation.getResNo() + " in zone "+ reservation.getZoneName()+ " is due after 30 minutes"))
                .setVibrate(mVibratePattern); // clear notification after click


        //to open activity when clicking on notification
        Intent intent = new Intent(getApplicationContext(), First.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, reservation.getResNo(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setShowWhen(false);
        //mNotificationManager.notify(0, mBuilder.build());
        return mBuilder.build();
    }

    private void deleteNotification(Reservation reservation) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reservation.getResNo(), intent, 0);
        alarmManager.cancel(pendingIntent);
    }










}
