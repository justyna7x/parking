package com.example.parked2;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class ListOfPlaces extends Application {

    private static ListOfPlaces singleton;
    private List<Location> myLocations;

    public List<Location> getMyLocations() {
        return myLocations;
    }

    public void setMyLocations(List<Location> myLocations) {
        this.myLocations = myLocations;
    }
    public ListOfPlaces getInstance(){
        return singleton;
    }
    public void onCreate(){
        super.onCreate();

        singleton=this;

        myLocations = new ArrayList<>();


    }
}
