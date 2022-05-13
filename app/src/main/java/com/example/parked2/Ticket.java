package com.example.parked2;

import java.time.LocalDateTime;

public class Ticket {
    public String zone;
    public String duration;
    public String startTime;
    Ticket(){

    }
    public Ticket(String zone, String duration, String startTime) {

        this.zone=zone;
        this.duration=duration;
        this.startTime=startTime;


    }
}
