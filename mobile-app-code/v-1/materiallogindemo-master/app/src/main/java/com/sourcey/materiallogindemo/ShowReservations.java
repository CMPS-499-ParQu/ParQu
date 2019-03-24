package com.sourcey.materiallogindemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by Alaa on 1/10/2019.
 */

public class ShowReservations extends AppCompatActivity {
    ListView list;
    List<Reservation> reservations;

     User userLogged = Choices.user;
    int endTime;
    String uDate;

    List<Reservation> reservations2;
    DatabaseReference  databaseReservations;
    int  fixedTimeCounter = 0;
    List<Reservation> reservations3;

    List<Reservation> reservations4;
    List<Reservation> reservations5;
    boolean before = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_list);
        reservations = new ArrayList<>();
        reservations2 = new ArrayList<>();

      //  if(before == false ) {
       /*     Intent i = getIntent();
            reservations = (List<Reservation>) i.getSerializableExtra("LIST");
            reservations2.clear();
            for (int j = 0; j < reservations.size(); j++) {
                if (reservations.get(j).getCarPlateNo().equals(userLogged.getPlateNo())) {
                    reservations2.add(reservations.get(j));
                }
            }
        //    before = true ;
            list = (ListView) findViewById(R.id.listMenu);
            list.setAdapter(new ReservationAdapter(this, reservations2));*/

      //  }
       //if (before == true ) {
            reservations5 = new ArrayList<>();
            reservations4 = new ArrayList<>();
             databaseReservations = FirebaseDatabase.getInstance().getReference("reservations");
             databaseReservations.addValueEventListener(new ValueEventListener() {


                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {

                     //clearing the previous artist list
                     // if(users != null)
                     reservations4.clear();

                     //iterating through all the nodes
                     for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                         //getting artist
                         Reservation reservation = postSnapshot.getValue(Reservation.class);
                         //adding artist to the list
                         reservations4.add(reservation);
                     }

                     for (int j = 0; j < reservations4.size(); j++) {
                         String resDate = reservations4.get(j).getData();
                         String status = reservations4.get(j).getStatus();
                         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                         Date strDate = null;
                         Date currentDate = new Date();
                         try {
                             strDate = sdf.parse(resDate);
                         } catch (ParseException e) {
                             e.printStackTrace();
                         }
                         if (reservations4.get(j).getCarPlateNo().equals(userLogged.getPlateNo()) && strDate.getDate() >= currentDate.getDate() && strDate.getMonth() >= currentDate.getMonth() && ! (status.equals("canceled")) ) {
                             reservations5.add(reservations4.get(j));
                         }
                     }
                     list = (ListView) findViewById(R.id.listMenu);
                     list.setAdapter(new ReservationAdapter(getApplicationContext(), reservations5));


                 }
                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });



         }


    class ReservationAdapter extends ArrayAdapter<Reservation>
    {
        public ReservationAdapter(Context c, List<Reservation>  reservations2)
        {
            super( c,R.layout.menu_list_item,reservations2);

        }
        public View getView (final int p, View v, final ViewGroup parent)
        {

            if (v==null)
            {
                LayoutInflater in=LayoutInflater.from(getContext());
                v=in.inflate(R.layout.menu_list_item,parent,false);

            }
            Button extend =(Button) v.findViewById(R.id.button15);
            Button cancel =(Button) v.findViewById(R.id.button16);
            TextView date =v.findViewById(R.id.date);
            TextView ResNo =v.findViewById(R.id.ResNo);
            TextView time =v.findViewById(R.id.time);
            TextView zoneName =v.findViewById(R.id.zoneName);
            ResNo.setText("Reservation No: "+getItem(p).getResNo()+"");
            date.setText("Date: "+getItem(p).getData());
            zoneName.setText("Zone Name: "+getItem(p).getZoneName());
             endTime = getItem(p).getTime().get(getItem(p).getTime().size()-1)+1;
            time.setText("Time: "+getItem(p).getTime().get(0)+ " to " + endTime );

            extend.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeZone(TimeZone.getTimeZone("Asia/Qatar"));
                    int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                    int currentMinute = cal.get(Calendar.MINUTE);
                    endTime = getItem(p).getTime().get(getItem(p).getTime().size()-1)+1;

                    uDate = getItem(p).getData()+"";

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date strDate = null;
                    try {
                        strDate = sdf.parse(uDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                        Date currentDate = new Date();
                    // if (  currentHour == endTime-1 && currentMinute >= 30){
                    if ( endTime - currentHour == 1 && strDate.getDate() == currentDate.getDate() && strDate.getMonth() == currentDate.getMonth()){
                        reservations3 = new ArrayList<>();
                        databaseReservations = FirebaseDatabase.getInstance().getReference("reservations");
                        databaseReservations.addValueEventListener(new ValueEventListener() {


                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //clearing the previous artist list
                                // if(users != null)
                                reservations3.clear();

                                //iterating through all the nodes
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    //getting artist
                                    Reservation reservation = postSnapshot.getValue(Reservation.class);
                                    //adding artist to the list
                                    reservations3.add(reservation);
                                }


                                for (int i = 0; i < reservations3.size(); i++) {
                                    if (reservations3.get(i).getData().equals(getItem(p).getData()) && reservations3.get(i).getZoneName().equals(getItem(p).getZoneName())) {
                                        for (int j = 0; j < reservations3.get(i).getTime().size(); j++) {
                                            if (reservations3.get(i).getTime().get(j).equals(endTime-1)) {
                                                fixedTimeCounter++;
                                            }
                                        }
                                    }
                                }

                                if (fixedTimeCounter < 2) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowReservations.this);
                                    builder.setMessage("Are you sure you want to extend this reservation to one hour");
                                    builder.setTitle("Confirmation Message");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            List<Integer> time = new ArrayList<>();
                                            for (int i = 0; i < getItem(p).getTime().size(); i++) {
                                                time.add(getItem(p).getTime().get(i));
                                            }
                                            time.add(endTime);

                                            Reservation reservation = new Reservation(getItem(p).getResNo(), getItem(p).getCarPlateNo(), getItem(p).getZoneName(), getItem(p).getData(), time, "extended", getItem(p).getPrice() + 5);

                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                                            Query applesQuery = ref.child("reservations").orderByChild("resNo").equalTo(getItem(p).getResNo());

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

                                            String id = databaseReservations.push().getKey();
                                            databaseReservations.child(id).setValue(reservation);
                                            finish();
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
                                else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowReservations.this );
                                    builder.setMessage("No available parking spots so you are not able to extend ");
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
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });





                  /*      AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setMessage("good" );
                        builder.setTitle("Error");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                return;
                            }
                        });
                        AlertDialog a = builder.create();
                        a.show();*/

                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowReservations.this);

                        builder.setMessage("You can not extend at this time " );
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
            });

            cancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {



                    Calendar cal = Calendar.getInstance();
                    cal.setTimeZone(TimeZone.getTimeZone("Asia/Qatar"));
                    int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                    int currentMinute = cal.get(Calendar.MINUTE);
                    int startTime = getItem(p).getTime().get(0);

                    String uDate = getItem(p).getData()+"";

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date strDate = null;
                    try {
                        strDate = sdf.parse(uDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date currentDate = new Date();
                    // if (  currentHour == endTime-1 && currentMinute >= 30){
               //     if ( startTime - currentHour == 1 && strDate.getDate() == currentDate.getDate() && strDate.getMonth() == currentDate.getMonth()){
                    int i;
                    for ( i=0; i< getItem(p).getTime().size(); i++) {
                        if ( (strDate.getDay()-currentDate.getDay() == 1 )) {

                                final int index = i;
                                AlertDialog.Builder builder = new AlertDialog.Builder(ShowReservations.this);
                            builder.setMessage("Are you sure you want to cancel the time period from "+ getItem(p).getTime().get(i)+ " to " + (getItem(p).getTime().get(getItem(p).getTime().size()-1)+1));
                            builder.setTitle("Confirmation Message");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        Reservation reservation = new Reservation(getItem(p).getResNo(), getItem(p).getCarPlateNo(), getItem(p).getZoneName(), getItem(p).getData(), getItem(p).getTime(), "canceled", ((getItem(p).getTime().size() - index) * 5 * 0.5 + (index - 0) * 5));

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                                        Query applesQuery = ref.child("reservations").orderByChild("resNo").equalTo(getItem(p).getResNo());

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

                                        String id = databaseReservations.push().getKey();
                                        databaseReservations.child(id).setValue(reservation);
                                        finish();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        return;

                                    }
                                });
                                AlertDialog a = builder.create();
                                a.show();
                                return;


                        }
                        else if(strDate.getDay() - currentDate.getDay() == 0 && getItem(p).getTime().get(i)- currentHour >= 1){

                            final int index = i;
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShowReservations.this);
                            builder.setMessage("Are you sure you want to cancel the time period from "+ getItem(p).getTime().get(i)+ " to " + (getItem(p).getTime().get(getItem(p).getTime().size()-1)+1));
                            builder.setTitle("Confirmation Message");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Reservation reservation = new Reservation(getItem(p).getResNo(), getItem(p).getCarPlateNo(), getItem(p).getZoneName(), getItem(p).getData(), getItem(p).getTime(), "canceled", ((getItem(p).getTime().size() - index) * 5 * 0.5 + (index - 0) * 5));

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                                    Query applesQuery = ref.child("reservations").orderByChild("resNo").equalTo(getItem(p).getResNo());

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

                                    String id = databaseReservations.push().getKey();
                                    databaseReservations.child(id).setValue(reservation);
                                    finish();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    return;

                                }
                            });
                            AlertDialog a = builder.create();
                            a.show();
                            return;

                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShowReservations.this);

                            builder.setMessage("You can not cancel at this time ");
                            builder.setTitle("Error");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    finish();

                                }

                            });
                            AlertDialog a = builder.create();
                            a.show();

                        }
                    }


                }

            });

            return v;

        }
    }


}