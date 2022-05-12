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

import java.util.Arrays;
import java.util.List;

public class MapsSettings extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSIONS_FINE_LOCATION = 99;

    TextView latitude, longitude, altitude, accuracy, speed, address, tv_sensor, tv_updates, tv_pinpointcounter, zone;
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
    //Zone A
    List<String> zone_a =Arrays.asList("Lancaster Road","Lower Hasting Street","New Bridge Street","Newtown Street","Princess Road West",
            "Rawson Street","Regent Road","Tower Street","Turner Street","Upper King Street","Welford Road","West Street");
    //Zone B
    List<String> zone_b =Arrays.asList("Aylestone Road","Burnmoor Street","Buttermere Street","Coniston Avenue","Filbert Street","Filbert Street East",
            "Hazel Street","Sawday Street","Walnut Street");
    //Zone C
    List<String> zone_c =Arrays.asList("Coriander Road","Mint Road","Sage Road","Tarragon Road","Thyme Close","Western Boulevard");
    //Zone D
    List<String> zone_d =Arrays.asList("Clarendon Street","Grasmere Street","Jarrom Street","Rydal Street","Thirlmere Street","Ullswater Street","Windermere Street");
    //Zone E
    List<String> zone_e =Arrays.asList("Abingdon Road","Abingdon Walk","Andover Street","Avon Street","Bartholomew Street","Beckingham Road","Briton Street"
            ,"Brookhouse Avenue","Brookhouse Street","Cedar Road","Chaucer Street","Churchill Street", "College Avenue","College Street","Conduit Street"
            ,"Conifer Close","Connaught Street","East Park Road","Evington Footway","Evington Place","Evington Road","Glebe Street","Gordon Avenue",
            "Gotham Street","Guilford Street","Hamilton Street","Herschell Street", "Highfield Street","Hobart Street","Lincoln Street",
            "London Road","Mandora Lane","Mayfield Road","Medway Street","Mere Road","Mill Hill Lane","Mundella Street","Myrtle Road",
            "Onslow Street","Oxford Avenue","Prebend Street","Ripon Street","Saxby Street","Severn Street","Seymour Street","Sparkenhoe Street",
            "St Albans Road","St James Road","St James Terrace","St Peters Road","St Stephens Road","Tennyson Street","Tichborne Street",
            "Upper Tichborne Street","Victoria Avenue", "Victoria Terrace","Welland Street","Woodbine Avenue");
    //Zone F
    List<String> zone_f = Arrays.asList("Harrow Road", "Ashleigh Road", "Barclay Street", "Beaconsfield Road", "Bede Street", "Braunstone Gate","Brazil Street",
            "Browning Street", "Bruce Street", "Cambridge Street", "Celt Street", "Cranmer Street", "Eastleigh Road", "Equity Road","Fosse Road South",
            "Gaul Street", "Harrow Road", "Hinckley Road", "Ivy Road", "Latimer Street", "Livingstone Street", "Luther Street", "Narborough Road",
            "Noel Street", "Norman Street", "Paton Street", "Ridley Street", "Roman Street", "Ruding Road", "Ruding Terrace", "Saxon Street","Westleigh Road",
            "Shaftesbury Road","Sheffield Street","Stuart Street","Sykefield Avenue","Tyndale Street","Upperton Road","Westcotes Drive","Western Road", "Wilberforce Road");
    //Zone G
    List<String> zone_g =Arrays.asList("Alderton Close");
    //Zone H
    List<String> zone_h =Arrays.asList("Coleman Road");
    //Zone J
    List<String> zone_j =Arrays.asList("Ariane Place", "Beagle Close", "Discovery Road", "Exploration Drive");
    //Zone K
    List<String> zone_k =Arrays.asList("1 The Avenue","Cecilia Road","Central Avenue","Clarendon Park Road","Cradock Road","East Avenue",
            "Edward Road","Howard Road","Montague Road","North Avenue","Orlando Road","Oxford Road","Queens Road","Seymour Road","St Leonards Road",
            "West Avenue","Adderley Road","Ashford Road","Avenue Rd Extension","Brentwood Road","Brookland Road","Bulwer Road","Clarendon Park Road",
            "Cross Road","Fleetwood Road","Fleetwood Court","Hartopp Road","Howard Road","Keble Road","Leopold Road","Lorne Road","Lytham Rd",
            "Lytton Rd","Portland Rd","Queens Road","St Leonards Road","Springfield Rd","Thurlow Rd","Victoria Park Road","Westbury Road","University Rd");
    //Zone L
    List<String> zone_l =Arrays.asList("Leicester Street","Nottingham Road", "Roseberry Street");
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
        zone = findViewById(R.id.zone);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationupdates = findViewById(R.id.sw_locationsupdates);
        btn_newWaypoint = findViewById(R.id.newPinpoint);
       // btn_showWayPointList = findViewById(R.id.listOfWaypoints);
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

       /* btn_showWayPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsSettings.this, ShowSavedLocations.class);
                startActivity(i);

            }
        });*/
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
        zone.setText("Not tracking location");

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

        /*if(location.getLatitude()>52.622294 && location.getLatitude()<52.622443 && location.getLongitude()>-1.122535
                && location.getLongitude()<-1.121541) {
            zone.setText("Zone A – Holy Trinity");
        }

        if(location.getLatitude()<=52.622294 && location.getLatitude()>52.622414 && location.getLongitude()>-1.122535
                && location.getLongitude()<-1.121541) {
            zone.setText("Zone B – Hazel");
        }
        if(location.getLatitude()<=52.630654 && location.getLatitude()>52.622294 && location.getLongitude()>-1.122535
                && location.getLongitude()<-1.121541) {
            zone.setText("Zone C – Bede Island");
        }
        if(location.getLatitude()<=52.622294 && location.getLatitude()>52.622414 && location.getLongitude()>-1.122535
                && location.getLongitude()<-1.121541) {
            zone.setText("Zone D – Riverside");
        }*/

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

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            address.setText(addresses.get(0).getAddressLine(0));
            String z=addresses.get(0).getThoroughfare();
            String a = "LE3 0JY";
            if(zone_a.contains(z)){
                zone.setText("Zone A");
            }
            else if(zone_b.contains(z)){
                zone.setText("Zone B");
            }
            else if(zone_c.contains(z)){
                zone.setText("Zone C");
            }
            else if(zone_d.contains(z)){
                zone.setText("Zone D");
            }
            else if(zone_e.contains(z)){
                zone.setText("Zone E");
            }
            else if(zone_f.contains(z)){
                zone.setText("Zone F");
            }
            else if(zone_g.contains(z)){
                zone.setText("Zone G");
            }
            else if(zone_h.contains(z)){
                zone.setText("Zone H");
            }
            else if(zone_j.contains(z)){
                zone.setText("Zone J");
            }
            else if(zone_k.contains(z)){
                zone.setText("Zone K");
            }
            else if(zone_l.contains(z)){
                zone.setText("Zone L");
            }
            else {
                zone.setText("Zone unavailable");
            }


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