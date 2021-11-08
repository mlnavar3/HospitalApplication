package com.example.hospitalapplication;

public class Doctor {
    private String name;
    private String id;

    public Doctor(String id, String firstName, String lastName) {
        this.name = "Dr. " + firstName + " " + lastName;
        this.id = id;
    }

    public Doctor(String firstName, String lastName){
        name = firstName + " " + lastName;
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
