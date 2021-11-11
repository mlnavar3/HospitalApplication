package com.example.hospitalapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterController implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
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
    Connection conn = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadDoctorList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDoctorList() throws SQLException {
        conn = Connect.connect();

        String sql = "";
        PreparedStatement stmt = null;
        sql = "SELECT staff_id,first_name,last_name FROM Staff WHERE role_id = 1";
        stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        List<Doctor> comboboxData = new ArrayList<Doctor>();
        while(rs.next()) {
            comboboxData.add(new Doctor(rs.getString(1), rs.getString(2), rs.getString(3)));
        }

        ObservableList<Doctor> doctors = FXCollections.observableArrayList(comboboxData);
        doctorList.setItems(doctors);

    }


    public String validateInput() {
        String errorMessage = "";

        // Validate password and confirm password
        if(!passwordField.getText().equals(confirmPasswordField.getText())) {
            System.out.println(passwordField.getText());
            System.out.println(confirmPasswordField.getText());
            System.out.println("Password and confirm password does not match");
            errorMessage += "Password and confirm password does not match\n";
        }

        // Validate username
        if (usernameField.getText() == null || usernameField.getText().trim().isEmpty()) {
            System.out.println("Username cannot be empty");
            errorMessage += "Username cannot be empty\n";
        }

        // Validate password
        if (passwordField.getText() == null || passwordField.getText().trim().isEmpty()) {
            System.out.println("Password cannot be empty");
            errorMessage += "Password cannot be empty\n";
        }

        // Validate confirm password
        if (confirmPasswordField.getText() == null || confirmPasswordField.getText().trim().isEmpty()) {
            System.out.println("Confirm password cannot be empty");
            errorMessage += "Confirm password cannot be empty\n";
        }

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


        // TODO: Validate if date of birth is legal. That is, make sure the date is not higher than the current date

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

    @FXML
    public void redirectToLogin(ActionEvent event) throws IOException {
        Parent loginViewParent = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Scene loginViewScene = new Scene(loginViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene((loginViewScene));
        window.show();
    }

    public void registerPatient(ActionEvent event) throws IOException {
        String uniqueID = UUID.randomUUID().toString();

        String sql = "INSERT INTO Patient_Profile VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        String validRegistrationInput = validateInput();
        if(validRegistrationInput == null || validRegistrationInput.trim().isEmpty()) {
            try {
                conn = Connect.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, uniqueID);
                pstmt.setString(2, doctorList.getValue().getId());
                pstmt.setString(3, usernameField.getText());
                pstmt.setString(4, passwordField.getText());
                pstmt.setString(5, firstNameField.getText());
                pstmt.setString(6, lastNameField.getText());
                pstmt.setDate(7, Date.valueOf(myDatePicker.getValue()));
                pstmt.setString(8, phoneNumberField.getText());
                pstmt.setString(9, emailField.getText());
                pstmt.setString(10, insuranceNameField.getText());
                pstmt.setString(11, pharmacyField.getText());
                pstmt.executeUpdate();
                MessageAlert.registrationSuccessfulBox();
                redirectToLogin(event);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            MessageAlert.registrationInputError(validRegistrationInput);
        }
    }

    @FXML
    public void onLoginHereClick(ActionEvent event) throws IOException {
        redirectToLogin(event);
    }

}
