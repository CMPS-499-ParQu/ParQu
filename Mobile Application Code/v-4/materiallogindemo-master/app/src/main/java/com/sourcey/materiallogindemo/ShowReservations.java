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
import android.view.WindowManager;
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

import java.text.DateFormat;
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
    List<Property> zones;
    DatabaseReference databaseZones;
    List<Integer> time = new ArrayList<>() ;

    User userLogged = Choices.user;
    int endTime;
    String uDate;
    int flage = 0;


    ArrayList<Statistics> history;
    ArrayList<historyInfoPerDay> info;

    String status;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    String resDate = reservations4.get(j).getDate();
                    String status = reservations4.get(j).getStatus();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date strDate = null;
                    Date currentDate = new Date();
                    try {
                        strDate = sdf.parse(resDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (reservations4.get(j).getCarPlateNo().equals(userLogged.getPlateNo()) && strDate.getDate() > currentDate.getDate() && strDate.getMonth() >= currentDate.getMonth() && ! (status.equals("cancel1ed")) && ! (status.equals("subcancel1ed"))) {
                        reservations5.add(reservations4.get(j));
                    }
                   else if (reservations4.get(j).getCarPlateNo().equals(userLogged.getPlateNo()) && strDate.getDate() == currentDate.getDate()&& currentDate.getHours() <= reservations4.get(j).getTime().get(reservations4.get(j).getTime().size()-1)+1 && strDate.getMonth() >= currentDate.getMonth() && ! (status.equals("cancelled")) && ! (status.equals("subcancelled"))) {
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Choices.class);
        startActivity(intent);
        super.onBackPressed();
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
            date.setText("Date: "+getItem(p).getDate());
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
                    endTime =( getItem(p).getTime().get(getItem(p).getTime().size()-1))+1;

                    uDate = getItem(p).getDate()+"";

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

                                fixedTimeCounter=0;

                                    for (int i = 0; i < reservations4.size(); i++) {
                                        if (reservations4.get(i).getDate().equals(getItem(p).getDate()) && reservations4.get(i).getZoneName().equals(getItem(p).getZoneName()) && !"cancelled".equals(reservations4.get(i).getStatus())) {
                                            for (int j = 0; j < reservations4.get(i).getTime().size(); j++) {
                                                if (reservations4.get(i).getTime().get(j).equals(endTime)) {
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

                                            zones = new ArrayList<>();
                                            databaseZones = FirebaseDatabase.getInstance().getReference("zones");

                                            databaseZones.addValueEventListener(new ValueEventListener() {


                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                     flage = flage + 1;
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
                                                    if(flage == 1){
                                                    //    statisticUpdate(getItem(p).getDate(), endTime, getItem(p).getZoneName());
                                                    }

                                                    else{
                                                        return;
                                                    }
                                                }


                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                            List<Integer> time = new ArrayList<>();
                                            for (int i = 0; i < getItem(p).getTime().size(); i++) {
                                                time.add(getItem(p).getTime().get(i));
                                            }
                                            time.add(endTime);

                                            int extendedHours = getItem(p).getExtendedHours();
                                            extendedHours = extendedHours +1;

                                            Reservation reservation = new Reservation(getItem(p).getResNo(), getItem(p).getCarPlateNo(), getItem(p).getZoneName(), getItem(p).getDate(), time, "extended", getItem(p).getPrice() + 5 , extendedHours, getItem(p).getCancelledHours(), getItem(p).getUid());

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
                                   // a.show();

                                    try {

                                            a.show();

                                    }
                                    catch (WindowManager.BadTokenException e) {
                                        //use a log message
                                    }


                                }
                                else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowReservations.this );
                                    builder.setMessage("No available parking spots so you are not able to extend ");
                                    builder.setTitle("Error");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            return;

                                        }
                                    });
                                    AlertDialog a = builder.create();
                                    try {

                                            a.show(); return;


                                    }
                                    catch (WindowManager.BadTokenException e) {
                                        //use a log message
                                    }





                                }








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
                        try {
                            a.show(); return;
                        }
                        catch (WindowManager.BadTokenException e) {
                            //use a log message
                        }

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

                    String uDate = getItem(p).getDate()+"";

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
                            final int canceledHours =   (getItem(p).getTime().get(getItem(p).getTime().size()-1)+1) - getItem(p).getTime().get(i);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ShowReservations.this);
                            builder.setMessage("Are you sure you want to cancel the time period from "+ getItem(p).getTime().get(i)+ " to " + (getItem(p).getTime().get(getItem(p).getTime().size()-1)+1));
                            builder.setTitle("Confirmation Message");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Reservation reservation = new Reservation(getItem(p).getResNo(), getItem(p).getCarPlateNo(), getItem(p).getZoneName(), getItem(p).getDate(), getItem(p).getTime(), "cancelled", ((getItem(p).getTime().size() - index) * 5 * 0.5 + (index - 0) * 5), getItem(p).getExtendedHours(), canceledHours,getItem(p).getUid() );

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
                            try {
                                a.show();
                            }
                            catch (WindowManager.BadTokenException e) {
                                //use a log message
                            }



                        }
                        else if(strDate.getDay() - currentDate.getDay() == 0 && getItem(p).getTime().get(i)- currentHour >= 1){

                            final int index = i;

                            final int canceledHours =   (getItem(p).getTime().get(getItem(p).getTime().size()-1)+1) - getItem(p).getTime().get(i);

                            if ( getItem(p).getTime().get(i) == getItem(p).getTime().get(0)){
                                status = "cancelled";
                            }
                            else
                            {
                                status = "subcancelled";

                            }


                            AlertDialog.Builder builder = new AlertDialog.Builder(ShowReservations.this);
                            builder.setMessage("Are you sure you want to cancel the time period from "+ getItem(p).getTime().get(i)+ " to " + (getItem(p).getTime().get(getItem(p).getTime().size()-1)+1));
                            builder.setTitle("Confirmation Message");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {




                                    Reservation reservation = new Reservation(getItem(p).getResNo(), getItem(p).getCarPlateNo(), getItem(p).getZoneName(), getItem(p).getDate(), getItem(p).getTime(), status, ((getItem(p).getTime().size() - index) * 5 * 0.5 + (index - 0) * 5), getItem(p).getExtendedHours(), canceledHours, getItem(p).getUid());

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
                                    finish();

                                }
                            });
                            AlertDialog a = builder.create();
                            a.show();

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
                            try {
                                a.show();
                            }
                            catch (WindowManager.BadTokenException e) {
                                //use a log message
                            }

                        }
                    }


                }

            });

            return v;

        }
    }


 /*   public void statisticUpdate (final String selectedDate, final int time, final String zoneName)
    {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final ArrayList<History> history = new ArrayList<>();
        final ArrayList<historyInfoPerDay> info = new ArrayList<>();
        history.clear();
        info.clear();

         for (int j = 0; j < zones.size(); j++) {
                if (zones.get(j).getZoneName().equals(zoneName)) {
                    for (int i = 0; i < zones.get(j).getHistory().size(); i++) {

                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1 = null;
                        try {
                            dt1 = format1.parse(selectedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat format2 = new SimpleDateFormat("EE");
                        String finalDay = format2.format(dt1);
                        if (zones.get(j).getHistory().get(i).getDay().equals(finalDay)) {
                            for (int k = 0; k < zones.get(j).getHistory().get(i).getInfo().size(); k++) {


                                if (time == Integer.parseInt(zones.get(j).getHistory().get(i).getInfo().get(k).getHour())) {
                                    int countNew = zones.get(j).getHistory().get(i).getInfo().get(k).getCount() + 1;



                                    if (zones.get(j).getHistory().get(i).getInfo().get(k).getDate() != null) {
                                        if (zones.get(j).getHistory().get(i).getInfo().get(k).getDate().contains(selectedDate)) {
                                            info.add(new historyInfoPerDay(zones.get(j).getHistory().get(i).getInfo().get(k).getHour(), countNew,zones.get(j).getHistory().get(i).getInfo().get(k).getDate() ));
                                        } else {
                                            ArrayList<String> dates = zones.get(j).getHistory().get(i).getInfo().get(k).getDate();
                                            dates.add(selectedDate);
                                            info.add(new historyInfoPerDay(zones.get(j).getHistory().get(i).getInfo().get(k).getHour(), countNew, dates));
                                        }
                                    } else {
                                        ArrayList<String> dates = new ArrayList<>();
                                        dates.add(selectedDate);
                                        info.add(new historyInfoPerDay(zones.get(j).getHistory().get(i).getInfo().get(k).getHour(), countNew, dates));
                                    }


                                   // info.add(new historyInfoPerDay(zones.get(j).getHistory().get(i).getInfo().get(k).getHour(), countNew, zones.get(j).getHistory().get(i).getInfo().get(k).getDate()));

                                } else {
                                    info.add(zones.get(j).getHistory().get(i).getInfo().get(k));
                                }


                            }

                            history.add(new History(finalDay, info));

                        } else {
                            history.add(zones.get(j).getHistory().get(i));
                        }
                    }
                }
            }
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

            String id = databaseZones.push().getKey();
                databaseZones.child(id).setValue(property);
            finish();



        }*/
 public List<Integer> arrayOfTime (int startTime , int hours){
     time.clear();
//     int startTime1 = Integer.parseInt(startTime) ;
//     int hours1 = Integer.parseInt(hours) ;

     for (int i= 0 ; i< hours ; i++){
         time.add(startTime + i);

     }
     return time ;
 }


    public void statisticUpdate (String selectedDate,int endTime, String zoneName){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        history = new ArrayList<>() ;
        info = new ArrayList<>();
        history.clear();
        info.clear();

        for (int j = 0; j <zones.size(); j++) {
            if (zones.get(j).getZoneName().equals(zoneName))
            {
                for (int i =0; i< zones.get(j).getStatistics().size(); i++){
                   // List<Integer> userHours = arrayOfTime(startTime,hours);
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

                                if (endTime == Integer.parseInt(zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getHour())) {

                                    //String[] parts = zones.get(j).getHistory().get(i).getInfo().get(k).getCount().split("-");
                                    flage = true;

                                        ArrayList<Integer> countNew = zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getCount();
                                            int index = zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getDate().indexOf(selectedDate);
                                            countNew.set(index, countNew.get(index)+1);
                                            info.add(new historyInfoPerDay(zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getHour(), countNew  ,zones.get(j).getStatistics().get(i).getHoursInfo().get(k).getDate() ));


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

        String id = databaseZones.push().getKey();
        databaseZones.child(id).setValue(property);
        finish();
        return;

    }



}