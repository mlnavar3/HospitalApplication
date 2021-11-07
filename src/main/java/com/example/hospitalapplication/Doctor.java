package com.example.hospitalapplication;

public class Doctor {
    private String name;
    private String id;

    public Doctor(String id, String firstName, String lastName) {
        this.name = "Dr. " + firstName + " " + lastName;
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }

}
