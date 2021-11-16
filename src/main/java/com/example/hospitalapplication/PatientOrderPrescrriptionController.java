package com.example.hospitalapplication;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.hospitalapplication.Connect.connect;

import java.net.URL;

public class PatientOrderPrescrriptionController implements Initializable{
    private Scene scene;
    private Parent root;
    Connection conn = null;

    String patientID = "";

    @FXML
    public void setPatientID(String id) throws SQLException {
        patientID = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void redirectToPatientProfile(ActionEvent event, String patientID) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-profile-view.fxml"));
        root = loader.load();

        //get patient info from database
        conn = Connect.connect();
        String sql = "";
        PreparedStatement stmt = null;
        //get patient info from sql database
        sql = "SELECT first_name,last_name,date_of_birth,phone_number,patient_email,insurance_name,pharmacy FROM Patient_Profile WHERE patient_profile_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, patientID);
        ResultSet rs = stmt.executeQuery();

        String staffID = "", firstName = "", lastName  = "", phoneNumber = "", email = "", insurance = "", pharmacy = "";
        int dateOfBirth = 0;

        while(rs.next()) {
            firstName += rs.getString(1);
            lastName += rs.getString(2);
            dateOfBirth += rs.getInt(3);
            phoneNumber += rs.getString(4);
            email += rs.getString(5);
            insurance += rs.getString(6);
            pharmacy += rs.getString(7);
        }

        Date dob = new Date(dateOfBirth);

        DoctorPatientProfileDetailController patientProflieController = loader.getController();
        patientProflieController.setPatientID(patientID);
        patientProflieController.loadPrescriptionList();
        patientProflieController.loadMedicalHistoryList();
        patientProflieController.setData(patientID, firstName, lastName, dob, phoneNumber, email, insurance, pharmacy, staffID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onBackClicked(ActionEvent event) throws SQLException, IOException {
        redirectToPatientProfile(event, patientID);
    }
}
