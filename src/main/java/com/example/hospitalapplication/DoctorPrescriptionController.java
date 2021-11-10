package com.example.hospitalapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorPrescriptionController {
    String staffID = "";
    String patientID = "";
    Connection conn = null;

    private Scene scene;
    private Parent root;

    public void setStaffID(String id) {
        staffID = id;
    }

    public void setPatientID(String id) {
        patientID = id;
    }

    @FXML
    private TextField prescriptionNameTxt;
    @FXML
    private TextField quantityTxt;

    @FXML
    public void onOrderClick(ActionEvent event) throws IOException, SQLException {
        System.out.println("ORDER SENT!");
    }

    @FXML
    public void redirectToPatientProfile(ActionEvent event, String patientId) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-profile-view.fxml"));
        root = loader.load();

        conn = Connect.connect();
        String sql = "";
        PreparedStatement stmt = null;
        sql = "SELECT patient_profile_id, first_name, last_name, date_of_birth, " +
                "phone_number, patient_email, insurance_name, pharmacy FROM Patient_Profile WHERE doctor_id = ? AND patient_profile_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, staffID);
        stmt.setString(2, patientID);
        ResultSet rs = stmt.executeQuery();
        Patient selectedPatient = null;
        while(rs.next()) {
            selectedPatient = new Patient(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4),
                    rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
        }

        PatientProfileDetailController patientController = loader.getController();
        patientController.setStaffID(staffID);
        patientController.setPatientID(patientID);
        patientController.setData(selectedPatient.getId(), selectedPatient.getFirstName(), selectedPatient.getLastName(), selectedPatient.getDateOfBirth(),
                selectedPatient.getPhoneNumber(), selectedPatient.getEmailAddress(), selectedPatient.getInsuranceName(), selectedPatient.getPharmacy(), staffID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void onCancelClick(ActionEvent event) throws IOException, SQLException {
        redirectToPatientProfile(event, patientID);
    }





}
