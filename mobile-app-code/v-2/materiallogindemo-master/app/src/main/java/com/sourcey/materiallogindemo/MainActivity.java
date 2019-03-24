package com.sourcey.materiallogindemo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity implements android.widget.AdapterView.OnItemSelectedListener{

    String startTime ;
     String hours ;
    List<User> users;
    List<Reservation> reservations;
    DatabaseReference databaseUsers, databaseReservations;
    String email, plateNo;
    TextView text;
    Button button0, button1, button2, button3, button4, button5 , button6, button7, button8 , button9, button10, button11 , button12, button13, button14 , button15;
    User user;
    Random r;
    String selectedDate = " ";
   final int [] fixedTime =  {7,8,9,10,11,12,13,14,15,16,17,18,19,20,21};
    final int [] fixedTimeCounter =new int[fixedTime.length];
    Reservation reservation;
    int day;

    List<Integer> time = new ArrayList<>() ;

    private User userLogged = Choices.user;
    Spinner spinner1;
    public static final String TAG= "MainActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListner;

    String zoneName;

    List<Property> zones;
    DatabaseReference databaseZones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                            if (reservations.get(i).getDate().equals(selectedDate) && reservations.get(i).getZoneName().equals(zoneName) && !"canceled".equals(reservations.get(i).getStatus())){
                                for (int j=0; j< reservations.get(i).getTime().size() ;j++){
                                    if (reservations.get(i).getTime().get(j).equals(fixedTime[k]) ){
                                        fixedTimeCounter[k]= fixedTimeCounter[k]+1;
                                    }
                                }
                            }
                        }

                    if ((4 - fixedTimeCounter[0]) <= 0) {
                        button0.setBackgroundColor(Color.RED);
                    }
                    else
                    {
                        button0.setBackgroundColor(0xFF4383CC);
                    }
                    if ((4 - fixedTimeCounter[1]) <= 0) {
                        button1.setBackgroundColor(Color.RED);
                    }
                    else {
                        button1.setBackgroundColor(0xFF4383CC);
                    }
                    if ((4 - fixedTimeCounter[2]) <= 0) {
                        button2.setBackgroundColor(Color.RED);
                    }
                    else {
                        button2.setBackgroundColor(0xFF4383CC);
                    }
                        if ((4 - fixedTimeCounter[3]) <= 0) {
                            button3.setBackgroundColor(Color.RED);
                        }
                        else {
                            button3.setBackgroundColor(0xFF4383CC);
                        }
                        if ((4 - fixedTimeCounter[4]) <= 0) {
                            button4.setBackgroundColor(Color.RED);
                        }
                        else {
                            button4.setBackgroundColor(0xFF4383CC);
                        }
                        if ((4 - fixedTimeCounter[5]) <= 0) {
                            button5.setBackgroundColor(Color.RED);
                        }
                        else {
                            button5.setBackgroundColor(0xFF4383CC);
                        }
                        if ((4 - fixedTimeCounter[6]) <= 0) {
                            button6.setBackgroundColor(Color.RED);
                        }
                        else {
                            button6.setBackgroundColor(0xFF4383CC);
                        }
                        if ((4 - fixedTimeCounter[7]) <= 0) {
                            button7.setBackgroundColor(Color.RED);
                        }
                        else {
                            button7.setBackgroundColor(0xFF4383CC);
                        }

                        if ((4 - fixedTimeCounter[8]) <= 0) {
                            button8.setBackgroundColor(Color.RED);
                        }
                        else {
                            button8.setBackgroundColor(0xFF4383CC);
                        }
                        if ((4 - fixedTimeCounter[9]) <= 0) {
                            button9.setBackgroundColor(Color.RED);
                        }
                        else {
                            button9.setBackgroundColor(0xFF4383CC);
                        }
                        if ((4 - fixedTimeCounter[10]) <= 0) {
                            button10.setBackgroundColor(Color.RED);
                        }
                        else {
                            button10.setBackgroundColor(0xFF4383CC);
                        }
                        if ((4 - fixedTimeCounter[11]) <= 0) {
                            button11.setBackgroundColor(Color.RED);
                        }
                        else {
                            button11.setBackgroundColor(0xFF4383CC);
                        }
                        if ((4 - fixedTimeCounter[12]) <= 0) {
                            button12.setBackgroundColor(Color.RED);
                        }
                        else {
                            button12.setBackgroundColor(0xFF4383CC);
                        }
                        if ((4 - fixedTimeCounter[13]) <= 0) {
                            button13.setBackgroundColor(Color.RED);
                        }
                        else {
                            button13.setBackgroundColor(0xFF4383CC);
                        }
                        if ((4 - fixedTimeCounter[14]) <= 0) {
                            button14.setBackgroundColor(Color.RED);
                        }
                        else {
                            button14.setBackgroundColor(0xFF4383CC);
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
                  else if (startTime.equals("22")){
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
                        if (reservations.get(i).getDate().equals(selectedDate) && reservations.get(i).getZoneName().equals(zoneName) && !"canceled".equals(reservations.get(i).getStatus())) {
                            for (int j = 0; j < reservations.get(i).getTime().size(); j++) {
                                if (reservations.get(i).getTime().get(j).equals(fixedTime[k])) {
                                    fixedTimeCounter[k] = fixedTimeCounter[k] + 1;
                                }
                            }
                        }
                    }
                }

                if ((4 - fixedTimeCounter[0]) <= 0) {
                    button0.setBackgroundColor(Color.RED);
                }
                else
                {
                    button0.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[1]) <= 0) {
                    button1.setBackgroundColor(Color.RED);
                }
                else {
                    button1.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[2]) <= 0) {
                    button2.setBackgroundColor(Color.RED);
                }
                else {
                    button2.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[3]) <= 0) {
                    button3.setBackgroundColor(Color.RED);
                }
                else {
                    button3.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[4]) <= 0) {
                    button4.setBackgroundColor(Color.RED);
                }
                else {
                    button4.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[5]) <= 0) {
                    button5.setBackgroundColor(Color.RED);
                }
                else {
                    button5.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[6]) <= 0) {
                    button6.setBackgroundColor(Color.RED);
                }
                else {
                    button6.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[7]) <= 0) {
                    button7.setBackgroundColor(Color.RED);
                }
                else {
                    button7.setBackgroundColor(0xFF4383CC);
                }

                if ((4 - fixedTimeCounter[8]) <= 0) {
                    button8.setBackgroundColor(Color.RED);
                }
                else {
                    button8.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[9]) <= 0) {
                    button9.setBackgroundColor(Color.RED);
                }
                else {
                    button9.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[10]) <= 0) {
                    button10.setBackgroundColor(Color.RED);
                }
                else {
                    button10.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[11]) <= 0) {
                    button11.setBackgroundColor(Color.RED);
                }
                else {
                    button11.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[12]) <= 0) {
                    button12.setBackgroundColor(Color.RED);
                }
                else {
                    button12.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[13]) <= 0) {
                    button13.setBackgroundColor(Color.RED);
                }
                else {
                    button13.setBackgroundColor(0xFF4383CC);
                }
                if ((4 - fixedTimeCounter[14]) <= 0) {
                    button14.setBackgroundColor(Color.RED);
                }
                else {
                    button14.setBackgroundColor(0xFF4383CC);
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



    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener

      // text.setText(plateNo);

    }

    public void createReservation(View v ){

        int resNo =  r.nextInt(1000);
          reservation = new Reservation( resNo,  userLogged.getPlateNo(), zoneName, selectedDate,arrayOfTime(startTime,hours)  , "created",  arrayOfTime(startTime,hours).size()*5, 0, 0);

        List<Integer> count = new ArrayList<>();

        int [] counters = new int[ time.size()] ;
        for (int i=0; i< counters.length ;i++){
            counters[i]=0;
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

        if (strDate.getDay() - currentDate.getDay() == 1 ){

            for (int k=0; k< time.size(); k++){
                for(int i=0;  i< reservations.size(); i++)
                {
                    if (reservations.get(i).getDate().equals(selectedDate) && reservations.get(i).getZoneName().equals(zoneName) && !"canceled".equals(reservations.get(i).getStatus()) ){
                        for (int j=0; j< reservations.get(i).getTime().size() ;j++){
                            if (reservations.get(i).getTime().get(j).equals(time.get(k)) ){
                                counters[k]= counters[k]+1;
                            }
                        }
                    }
                }
            }

            String alertMessage = "No available parking at \n" ;
            boolean exist = true;

            for(int i=0; i<counters.length ; i++){

                if ((3-counters[i]) <= 0){
                    exist = false;
                    //  Toast.makeText(MainActivity.this, " no available parking at" + time.get(i),
                    //   Toast.LENGTH_LONG).show();
                    alertMessage =  alertMessage + time.get(i)+":00 \n";

                }
                if (i == counters.length-1 && exist == false){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this );
                    builder.setMessage(alertMessage+"");
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
        else if (strDate.getDay() - currentDate.getDay() == 0 && Integer.parseInt(startTime) - currentHour >=1){

            for (int k=0; k< time.size(); k++){
                for(int i=0;  i< reservations.size(); i++)
                {
                    if (reservations.get(i).getDate().equals(selectedDate) && reservations.get(i).getZoneName().equals(zoneName) && !"canceled".equals(reservations.get(i).getStatus()) ){
                        for (int j=0; j< reservations.get(i).getTime().size() ;j++){
                            if (reservations.get(i).getTime().get(j).equals(time.get(k)) ){
                                counters[k]= counters[k]+1;
                            }
                        }
                    }
                }
            }

            String alertMessage = "No available parking at \n" ;
            boolean exist = true;

            for(int i=0; i<counters.length ; i++){

                if ((4-counters[i]) <= 0){
                    exist = false;
                    //  Toast.makeText(MainActivity.this, " no available parking at" + time.get(i),
                    //   Toast.LENGTH_LONG).show();
                    alertMessage =  alertMessage + time.get(i)+":00 \n";

                }
                if (i == counters.length-1 && exist == false){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this );
                    builder.setMessage(alertMessage+"");
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
        for (int i =0; i< reservations.size(); i++) {
            if (reservations.get(i).getCarPlateNo().equals(userLogged.getPlateNo()) && reservations.get(i).getDate().equals(selectedDate)) {
                totalReservationHours +=( reservations.get(i).getTime().size() - reservations.get(i).getExtendedHours() - reservations.get(i).getCancelledHours());
            }
        }
        if( (totalReservationHours + Integer.parseInt(hours)) > 6){
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

    protected void onStop() {
        super.onStop();

        for (int j = 0; j <zones.size(); j++) {
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
        }
    }


}
