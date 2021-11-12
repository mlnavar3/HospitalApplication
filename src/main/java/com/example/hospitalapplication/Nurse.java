package com.example.hospitalapplication;

public class Nurse {

    private String name;
    private String id;

    //Nurse constructor
    public Nurse(String id, String firstName, String lastName)
    {
        this.name = "Nurse " + firstName + " " + lastName;
        this.id = id;
    }

    //Nurse constructor without id
    public Nurse(String firstName, String lastName)
    {
        this.name = firstName + " " + lastName;
    }

    //getter methods
    public String getName()
    {
        return this.name;
    }

    public String getID()
    {
        return id;
    }

    public String toString()
    {
        return this.name;
    }
}
