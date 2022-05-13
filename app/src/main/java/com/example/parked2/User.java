package com.example.parked2;

public class User {
    public String fullName, email;
    public float balance;
    //make user and ticket, both separate objects
   public Ticket tickets;

    public User(){

    }

    public User(String fullName, String email, float balance, Ticket tickets) {
        this.fullName = fullName;
        this.email = email;
        this.balance = balance;
        this.tickets=tickets;

    }
}