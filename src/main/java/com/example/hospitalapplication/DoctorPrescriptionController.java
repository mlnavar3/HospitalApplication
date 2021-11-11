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
import java.sql.*;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public String validateInput() {
        String errorMessage = "";

        if(prescriptionNameTxt.getText() == null || prescriptionNameTxt.getText().trim().isEmpty()) {
            System.out.println("Prescription name cannot be empty");
            errorMessage += "Prescription name cannot be empty\n";
        }

        if(quantityTxt.getText() == null || quantityTxt.getText().trim().isEmpty()) {
            System.out.println("Prescription quantity cannot be empty");
            errorMessage += "Prescription quantity cannot be empty\n";
        }

        if(!isNumeric(quantityTxt.getText())) {
            System.out.println("Prescription quantity needs to be numeric");
            errorMessage += "Prescription quantity needs to be numeric\n";
        }

        // If error message is empty, return true. Otherwise, return false
        return errorMessage;
    }

    @FXML
    public void onOrderClick(ActionEvent event) throws IOException, SQLException {
        String uniqueID = UUID.randomUUID().toString();

        String sql1 = "INSERT INTO Prescription VALUES(?,?,?,?)";
        String sql2 = "INSERT INTO Patient_Prescription VALUES(?,?)";
        String validPrescriptionInput = validateInput();
        if(validPrescriptionInput == null || validPrescriptionInput.trim().isEmpty()) {
            try {
                conn = Connect.connect();
                PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                pstmt1.setString(1, uniqueID);
                pstmt1.setString(2, prescriptionNameTxt.getText());
                pstmt1.setInt(3, Integer.parseInt(quantityTxt.getText()));
                pstmt1.setBoolean(4, true);
                pstmt1.executeUpdate();
                pstmt1.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            try {
                conn = Connect.connect();
                PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                pstmt2.setString(1, patientID);
                pstmt2.setString(2, uniqueID);
                pstmt2.executeUpdate();
                MessageAlert.prescriptionSuccessfulBox();
                redirectToPatientProfile(event);
                pstmt2.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            MessageAlert.prescriptionErrorBox(validPrescriptionInput);
        }
    }

    @FXML
    public void redirectToPatientProfile(ActionEvent event) throws IOException, SQLException {
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
        patientController.loadPrescriptionList();
        patientController.setData(selectedPatient.getId(), selectedPatient.getFirstName(), selectedPatient.getLastName(), selectedPatient.getDateOfBirth(),
                selectedPatient.getPhoneNumber(), selectedPatient.getEmailAddress(), selectedPatient.getInsuranceName(), selectedPatient.getPharmacy(), staffID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void onCancelClick(ActionEvent event) throws IOException, SQLException {
        redirectToPatientProfile(event);
    }


}
