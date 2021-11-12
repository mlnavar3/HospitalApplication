package com.example.hospitalapplication;

import java.util.Date;

public class MedicalHistory {
    private String id;
    private String diagnosis;
    private String date;

    public MedicalHistory(String id, String diagnosis, String date) {
        this.id = id;
        this.diagnosis = diagnosis;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString(){
        return diagnosis;
    }
}
