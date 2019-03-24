package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Choices extends AppCompatActivity {

    DatabaseReference databaseUsers;
    DatabaseReference databaseReservations;
    List<Reservation> reservations;
    String email, plateNo;
    List<User> users;
    public static User user;
    ProgressBar progressBar;
    TextView progressBarText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        users = new ArrayList<>();
        Button checkBtn = findViewById(R.id.check_btn);
        final Button reserveBtn = findViewById(R.id.reserve_btn);
        Button showReservationBtn = findViewById(R.id.show_res_btn);
        Button requestCarCare = findViewById(R.id.car_wash_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressbar1);
         progressBarText = (TextView) findViewById(R.id.progressBarinsideText1);

        Intent intent = getIntent();
        //text = (TextView) findViewById(R.id.t-1);


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
                        user  = users.get(i);
                        plateNo = user.getPlateNo();
                        break;
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReservations = FirebaseDatabase.getInstance().getReference("reservations");
        reservations = new ArrayList<>();
        databaseReservations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Reservation reservation = postSnapshot.getValue(Reservation.class);
                    //adding artist to the list
                    reservations.add(reservation);
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });




        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkIntent = new Intent(Choices.this, ZoneList2.class);
                startActivity(checkIntent);

             //to be implemented
            }
        });


        requestCarCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkIntent = new Intent(Choices.this, CarCare.class);
                startActivity(checkIntent);

                //to be implemented
            }
        });


        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarText.setVisibility(View.VISIBLE);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
                progressBarText.setVisibility(View.GONE);
                Intent toLoginIntent = new Intent(Choices.this, ZonesList.class);
             //   toLoginIntent.putExtra("plateNo", plateNo);
                startActivity(toLoginIntent);

            }
        });



        showReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReservations = FirebaseDatabase.getInstance().getReference("reservations");
                reservations = new ArrayList<>();
                databaseReservations.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //iterating through all the nodes
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //getting artist
                            Reservation reservation = postSnapshot.getValue(Reservation.class);
                            //adding artist to the list
                            reservations.add(reservation);
                        }
                        Intent intent = new Intent(Choices.this, ShowReservations.class);
                        intent.putExtra("LIST", (Serializable) reservations);
                        startActivity(intent);

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });


            }
        });


    }
    public void prograss() throws InterruptedException {

        Thread.sleep(100);
        progressBar.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);

    }
}
