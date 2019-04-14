package com.sourcey.materiallogindemo;

/**
 * Created by Alaa on 3/9/2019.
 */

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by User on 10/2/2017.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            geoLocate();

        }
    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    String zoneName, spotNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        zoneName= getIntent().getStringExtra("zoneName");
        spotNum= getIntent().getStringExtra("spotNum");

        getLocationPermission();
    }



    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

            if (zoneName.equals("CAAS Female Zone")){
                if(spotNum.equals("01")){
                    moveCamera(new LatLng(25.370081, 51.489748), 11, "My Destination" );
                }
                if(spotNum.equals("02")){
                    moveCamera(new LatLng(25.370070, 51.489754), 11, "My Destination" );
                }
                if(spotNum.equals("03")){
                    moveCamera(new LatLng(25.370026, 51.489791), 11, "My Destination" );
                }
                if(spotNum.equals("04")){
                    moveCamera(new LatLng(25.369987, 51.489846), 11, "My Destination" );
                }
            }
            else if (zoneName.equals("CENG Female Zone")){
                if(spotNum.equals("01")){
                    moveCamera(new LatLng( 25.374990, 51.488152), 11, "My Destination" );
                }
                if(spotNum.equals("02")){
                    moveCamera(new LatLng(25.375023, 51.488142), 11, "My Destination" );
                }
                if(spotNum.equals("03")){
                    moveCamera(new LatLng( 25.375005, 51.488148), 11, "My Destination" );
                }
                if(spotNum.equals("04")){
                    moveCamera(new LatLng( 25.375057, 51.488127), 11, "My Destination" );
                }
            }

            else if (zoneName.equals("CENG Male Zone")){
                if(spotNum.equals("01")){
                    moveCamera(new LatLng( 25.375327, 51.492001), 11, "My Destination" );
                }
                if(spotNum.equals("02")){
                    moveCamera(new LatLng( 25.375347, 51.492032), 11, "My Destination" );
                }
                if(spotNum.equals("03")){
                    moveCamera(new LatLng(  25.375367, 51.492062), 11, "My Destination" );
                }
                if(spotNum.equals("04")){
                    moveCamera(new LatLng( 25.375388, 51.492108), 11, "My Destination" );
                }
            }

            else if (zoneName.equals("BNK Male Zone")){
                if(spotNum.equals("01")){
                    moveCamera(new LatLng( 25.377778, 51.490494), 11, "My Destination" );
                }
                if(spotNum.equals("02")){
                    moveCamera(new LatLng(  25.377828, 51.490464), 11, "My Destination" );
                }
                if(spotNum.equals("03")){
                    moveCamera(new LatLng(    25.377792, 51.490477), 11, "My Destination" );
                }
                if(spotNum.equals("04")){
                    moveCamera(new LatLng(   25.377846, 51.490450), 11, "My Destination" );
                }
            }
        }



    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    11, "My Location");

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());

        }}

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);

    }




        private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }


}



