package com.example.hospitalapplication;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.sql.Connection;
import java.sql.SQLException;

public class DoctorAppointmentController {
    String staffID = "";
    String patientID = "";

    private Scene scene;
    private Parent root;
    Connection conn = null;

    @FXML
    public void setStaffID(String id) throws SQLException {
        staffID = id;
    }
    @FXML
    public void setPatientID(String id) throws SQLException {
        patientID = id;
    }
}
