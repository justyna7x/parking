package com.example.parked2;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ShowSavedLocations extends AppCompatActivity {

    ListView lv_savedLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_locations);

        lv_savedLocations = findViewById(R.id.list_of_tickets);

        ListOfPlaces listOfPlaces = (ListOfPlaces)getApplicationContext();
        List<Location> savedLocations = listOfPlaces.getMyLocations();

        lv_savedLocations.setAdapter(new ArrayAdapter<Location>(this , android.R.layout.simple_list_item_1 ,savedLocations ));
    }
}