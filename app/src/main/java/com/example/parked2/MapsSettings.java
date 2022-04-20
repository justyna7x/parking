package com.example.parked2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MapsSettings extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSIONS_FINE_LOCATION = 99;

    TextView latitude, longitude, altitude, accuracy, speed, address, tv_sensor, tv_updates, tv_pinpointcounter;
    Switch sw_locationupdates, sw_gps;
    Button btn_newWaypoint, btn_showWayPointList, viewMap;

    //location services provided by Google
    FusedLocationProviderClient fusedLocationProviderClient;

    boolean updateOn = false;

    //current location
    Location currentLocation;
    //list of saved locations
    List<Location> savedLocations;
    //location request
    LocationRequest locationRequest;

    LocationCallback locationCallBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_settings);

        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        accuracy = findViewById(R.id.accuracy);
        altitude = findViewById(R.id.altitude);
        speed = findViewById(R.id.speed);
        address = findViewById(R.id.address);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationupdates = findViewById(R.id.sw_locationsupdates);
        btn_newWaypoint = findViewById(R.id.newPinpoint);
        btn_showWayPointList = findViewById(R.id.listOfWaypoints);
        tv_pinpointcounter = findViewById(R.id.mypinpoints);
        viewMap =findViewById(R.id.viewMap);

        //properties for location request
        locationRequest = new LocationRequest();
        // how often location check occurs in ms
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        //how often does the location check occur when set to the most frequent update?
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //triggered whenever update interval met
        locationCallBack = new LocationCallback() {

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                //save

                updateUIValues(locationResult.getLastLocation());
            }
        };

        btn_newWaypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add to list
                ListOfPlaces listOfPlaces = (ListOfPlaces)getApplicationContext();
                savedLocations = listOfPlaces.getMyLocations();
                savedLocations.add(currentLocation);

            }
        });

        btn_showWayPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsSettings.this, ShowSavedLocations.class);
                startActivity(i);

            }
        });
        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsSettings.this, MapsActivity.class);
                startActivity(i);

            }
        });


        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_gps.isChecked()) {
                    //most accurate - GPS
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using GPS sensors");
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using Towers +WiFi");
                }
            }
        });
        sw_locationupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_locationupdates.isChecked()) {
                    //turn on location tracking
                    startLocationUpdates();
                } else {//turn off location tracking
                    stopLocationUpdates();
                }
            }
        });

        updateGPS();
    }//end onCreate method

    private void stopLocationUpdates() {
        tv_updates.setText("Location is NOT being tracked");
        latitude.setText("Not tracking location");
        longitude.setText("Not tracking location");
        speed.setText("Not tracking location");
        altitude.setText("Not tracking location");
        address.setText("Not tracking location");
        accuracy.setText("Not tracking location");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }


    private void startLocationUpdates() {
        tv_updates.setText("Location is being tracked");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                updateGPS();
            }else{
                Toast.makeText(this, "Not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
        }
    }

    private void updateGPS(){
        //get permissions from the user to track GPS
        //get the current location from the fused client
        //update the UI - set all properties in the view

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsSettings.this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //user provided the permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                   //we got permission. values of location to the UI
                    updateUIValues(location);

                    currentLocation = location;
                }
            });
        }
        else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }

        }
    }

    private void updateUIValues(Location location) {
        //update all of the parametrs in activity
        latitude.setText(String.valueOf(location.getLatitude()));
        longitude.setText(String.valueOf(location.getLongitude()));
        accuracy.setText(String.valueOf(location.getAccuracy()));

        if (location.hasAltitude()) {
            altitude.setText(String.valueOf(location.getAltitude()));
        } else {
            altitude.setText("Not Available");
        }

        if (location.hasSpeed()) {
            speed.setText(String.valueOf(location.getSpeed()));
        } else {
            speed.setText("Not Available");
        }

        Geocoder geocoder = new Geocoder(MapsSettings.this);

        try{
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            address.setText(addresses.get(0).getAddressLine(0));
        }
        catch (Exception e){
            address.setText("Unable to get street address");
        }
        ListOfPlaces listOfPlaces = (ListOfPlaces)getApplicationContext();
        savedLocations = listOfPlaces.getMyLocations();

        //show number of waypoints saved
        tv_pinpointcounter.setText(Integer.toString(savedLocations.size()));
    }

}