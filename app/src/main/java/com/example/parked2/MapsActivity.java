package com.example.parked2;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Button homePage;
    private GoogleMap mMap;
    private Button logout;
    private Button myProfile, settings;

    List<Location> savedLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ListOfPlaces listOfPlaces = (ListOfPlaces)getApplicationContext();
        savedLocations = listOfPlaces.getMyLocations();

        settings = (Button) findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, MapsSettings.class));
            }
        });

        logout = (Button) findViewById(R.id.signOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MapsActivity.this, MainActivity.class));

            }
        });

        homePage = (Button) findViewById(R.id.homePage);

        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, ProfileActivity.class));

            }
        });


        myProfile = (Button) findViewById(R.id.myProfile);

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MapsActivity.this, MyProfile.class));

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Leicester Sydney and move the camera

        LatLng leicester = new LatLng(52, -1);

       /* mMap.addMarker(new MarkerOptions().position(Leicester).title("Marker in Leicester"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Leicester));*/

        LatLng lastLocationPlaced = leicester;
        for(Location location: savedLocations){
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Lat"+ location.getLatitude()+ "Lon"+ location.getLongitude());
            mMap.addMarker(markerOptions);
            lastLocationPlaced = latLng;

        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocationPlaced, 12.0f));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // counter
                Integer clicks = (Integer) marker.getTag();
                if(clicks == null){
                    clicks = 0;
                }
                clicks++;
                        marker.setTag(clicks);
                Toast.makeText(MapsActivity.this, "Marker" + marker.getTitle() + "was clicked"+ marker.getTag(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}