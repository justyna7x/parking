package com.example.parked2;

public class User {
    public String fullName, regPlate, email;
    public float balance;
    public User(){

    }

    public User(String fullName, String regPlate, String email, float balance) {
        this.fullName = fullName;
        this.regPlate = regPlate;
        this.email = email;
        this.balance = balance;

    }
}