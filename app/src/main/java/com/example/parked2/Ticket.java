package com.example.parked2;

import java.time.LocalDateTime;

public class Ticket {
    public String regplate, zone;
    public int duration;
    public LocalDateTime startTime;
    Ticket(){

    }
    public Ticket(String regplate, String zone, int duration, LocalDateTime startTime) {
        this.regplate=regplate;
        this.zone=zone;
        this.duration=duration;
        this.startTime=startTime;


    }
}
