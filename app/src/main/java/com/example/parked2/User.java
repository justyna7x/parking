package com.example.parked2;

public class User {
    public String fullName, regPlate, email;
    public float balance;
    //make user and ticket, both separate objects
   //public Ticket ticket;

    public User(){

    }

    public User(String fullName, String regPlate, String email, float balance /*Ticket ticket*/) {
        this.fullName = fullName;
        this.regPlate = regPlate;
        this.email = email;
        this.balance = balance;
        //this.ticket=ticket;

    }
}