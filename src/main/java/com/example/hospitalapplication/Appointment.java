package com.example.hospitalapplication;

import java.util.Date;

public class Appointment {
    private String id;
    private Date date;
    private double weight;
    private double height;
    private double bodyTemp;
    private String bloodPressure;
    private String status;
    private String medicalComplaint;
    private String doctorSummary;
    private String patientID;
    private String patientName;
    private int age;

    public Appointment(String id, Date date, int weight, int height, double bodyTemp, String bloodPressure, String medicalComplaint, String doctorSummary, String patientID, String patientName, int age) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.height = height;
        this.bodyTemp = bodyTemp;
        this.bloodPressure = bloodPressure;
        this.medicalComplaint = medicalComplaint;
        this.doctorSummary = doctorSummary;
        this.patientID = patientID;
        this.patientName = patientName;
        this.age = age;

        if(weight == 0 || height == 0 || bodyTemp == 0) {
            this.status = "Incomplete";
        } else if (doctorSummary == null || doctorSummary.trim().isEmpty()) {
            this.status = "Ready";
        } else {
            this.status = "Complete";
        }
    }

    public Appointment(String patientName, double weight, double height, double bodyTemp, String bloodPressure, String doctorSummary, String id, String appID) {
        this.id = appID;
        this.patientID = id;
        this.patientName = patientName;
        this.weight = weight;
        this.height = height;
        this.bodyTemp = bodyTemp;
        this.bloodPressure = bloodPressure;
        if(weight == 0 || height == 0 || bodyTemp == 0) {
            this.status = "Incomplete";
        } else if (doctorSummary == null || doctorSummary.trim().isEmpty()) {
            this.status = "Ready";
        } else {
            this.status = "Complete";
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getBodyTemp() {
        return bodyTemp;
    }

    public void setBodyTemp(double bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMedicalComplaint() {
        return medicalComplaint;
    }

    public void setMedicalComplaint(String medicalComplaint) {
        this.medicalComplaint = medicalComplaint;
    }

    public String getDoctorSummary() {
        return doctorSummary;
    }

    public void setDoctorSummary(String doctorSummary) {
        this.doctorSummary = doctorSummary;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}
