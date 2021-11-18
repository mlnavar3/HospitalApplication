package com.example.hospitalapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatientContactChangeController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private DatePicker myDatePicker;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField insuranceNameField;
    @FXML
    private TextField pharmacyField;
    @FXML
    private ComboBox<Doctor> doctorList;

    // Connection to database variable
    private Scene scene;
    private Parent root;

    String staffID = "";
    String patientID = "";

    public String id, firstName, lastName, phoneNumber,
            emailAddress, insuranceCompany, pharmacy;
    public java.util.Date dob;

    Connection conn = null;

    public String validateInput() {
        String errorMessage = "";

        // Validate first name
        if (firstNameField.getText() == null || firstNameField.getText().trim().isEmpty()) {
            System.out.println("First name cannot be empty");
            errorMessage += "First name cannot be empty\n";
        }

        // Validate last name
        if (lastNameField.getText() == null || lastNameField.getText().trim().isEmpty()) {
            System.out.println("Last name cannot be empty");
            errorMessage += "Last name cannot be empty\n";
        }

        // Validate date of birth
        if (String.valueOf(myDatePicker.getValue()) == null || String.valueOf(myDatePicker.getValue()).trim().isEmpty()) {
            System.out.println("Date of Birth cannot be empty");
            errorMessage += "Date of Birth cannot be empty\n";
        }

        // Validate email
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher match = pattern.matcher(emailField.getText());
        if (!match.matches()) {
            System.out.println("Invalid email address");
            errorMessage += "Invalid email address\n";
        }

        // Validate doctor list
        if (String.valueOf(doctorList.getValue().getId()) == null || String.valueOf(doctorList.getValue().getId()).trim().isEmpty()) {
            System.out.println("Doctor must be chosen");
            errorMessage += "Doctor must be chosen\n";
        }

        // If error message is empty, return true. Otherwise, return false
        return errorMessage;
    }

    public void ChangePatientInfo(ActionEvent event) throws IOException {

        String sql = "INSERT INTO Patient_Profile VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        String validRegistrationInput = validateInput();
        if(validRegistrationInput == null || validRegistrationInput.trim().isEmpty()) {
            try {
                conn = Connect.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(5, firstNameField.getText());
                pstmt.setString(6, lastNameField.getText());
                pstmt.setDate(7, Date.valueOf(myDatePicker.getValue()));
                pstmt.setString(8, phoneNumberField.getText());
                pstmt.setString(9, emailField.getText());
                pstmt.setString(10, insuranceNameField.getText());
                pstmt.setString(11, pharmacyField.getText());
                pstmt.executeUpdate();
                MessageAlert.registrationSuccessfulBox();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            MessageAlert.registrationInputError(validRegistrationInput);
        }
    }

}
