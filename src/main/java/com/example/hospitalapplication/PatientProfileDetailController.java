package com.example.hospitalapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class PatientProfileDetailController {

    private Scene scene;
    private Parent root;

    String staffID = "";

    public String id, firstName, lastName, phoneNumber,
            emailAddress, insuranceCompany, pharmacy;
    public Date dob;

    @FXML
    private Label nameLabel,
            ageLabel,
            phoneNumberLabel,
            emailLabel,
            insuranceLabel,
            pharmacyLabel;

    @FXML
    private Button orderPrescriptionButton;


    public PatientProfileDetailController(){

    }

    @FXML
    public void setStaffID(String id) throws SQLException {
        staffID = id;
    }

    public void setData(String id, String firstName, String lastName, Date dob, String phoneNumber,
                        String emailAddress, String insuranceCompany, String pharmacy, String staffId){

        nameLabel.setText(firstName + " " + lastName);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
        int birthYear = Integer.parseInt(sdf2.format(dob));
        String currentYearString = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        int currentYearInt = Integer.parseInt(currentYearString);
        String age = String.valueOf(currentYearInt - birthYear);
        ageLabel.setText(age);

        phoneNumberLabel.setText(phoneNumber);
        emailLabel.setText(emailAddress);
        insuranceLabel.setText(insuranceCompany);
        pharmacyLabel.setText(pharmacy);

        //Checks if doctor is looking at patient or if patient is viewing self
        if(staffId != null)
            orderPrescriptionButton.setVisible(true);
        else
            orderPrescriptionButton.setVisible(false);

    }

    @FXML
    public void redirectToPatientList(ActionEvent event, String id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-list-of-patients-view.fxml"));
        root = loader.load();

        DoctorPatientsListController doctorPatientsListController = loader.getController();
        doctorPatientsListController.setStaffID(id);
        doctorPatientsListController.loadPatientList();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onBackClicked(ActionEvent event) throws IOException, SQLException {
        redirectToPatientList(event, staffID);
    }

}
