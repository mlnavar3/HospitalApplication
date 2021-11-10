package com.example.hospitalapplication;

import java.util.Date;

public class Patient {

    private String id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String phoneNumber;
    private String emailAddress;
    private String insuranceName;
    private String pharmacy;

    public Patient(){

    }

    public Patient(String id, String firstName, String lastName, Date dateOfBirth, String phoneNumber, String emailAddress, String insuranceName, String pharmacy) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.insuranceName = insuranceName;
        this.pharmacy = pharmacy;
    }

    public Patient(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFullName() {return firstName + " " + lastName;}

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    @Override
    public String toString(){
        return firstName + " " + lastName;
    }
}
