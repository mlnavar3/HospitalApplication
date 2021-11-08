package com.example.hospitalapplication;

public class Patient {
    private String name;
    private String id;

    public Patient(String id, String firstName, String lastName) {
        this.name = firstName + " " + lastName;
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }

    @Override
    public String toString(){
        return name;
    }
}
