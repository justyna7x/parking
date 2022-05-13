package com.example.parked2;

import java.time.LocalDateTime;

public class Ticket {
    public String zone;
    public String duration;
    public String startTime;
    public String regPlate;
    public String endTime;
    Ticket(){

    }
    public Ticket(String zone,String regPlate, String duration, String startTime, String endTime) {

        this.zone=zone;
        this.regPlate=regPlate;
        this.duration=duration;
        this.startTime=startTime;
        this.endTime=endTime;


    }
}
