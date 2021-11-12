package com.example.hospitalapplication;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.SQLException;

public class DoctorAppointmentController {
    String patientID = "";

    private Scene scene;
    private Parent root;
    Connection conn = null;

    @FXML
    private Label nameLbl;
    @FXML
    private Label weightLbl;
    @FXML
    private Label heightLbl;
    @FXML
    private Label bodyTempLbl;
    @FXML
    private Label bloodPressureLbl;



    public void setPatientID(String id) throws SQLException {
        patientID = id;
    }

    public void setWeight(double weight) {
        System.out.println();
        weightLbl.setText(String.valueOf(weight) + " lbs");
    }

    public void setHeight(double height) {
        heightLbl.setText(String.valueOf(height) + " cm");
    }

    public void setBodyTemp(double bodyTemp) {
        bodyTempLbl.setText(String.valueOf(bodyTemp) + " F");
    }

    public void setBloodPressure(String bloodPressure) {
        bloodPressureLbl.setText(bloodPressure + "mmHg");
    }


}
