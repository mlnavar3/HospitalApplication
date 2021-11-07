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
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DoctorPatientsListController implements Initializable {
    String staffID = "";
    Connection conn = null;

    private Scene scene;
    private Parent root;

    @FXML
    private ListView patientList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void loadPatientList() throws SQLException {
        conn = Connect.connect();
        String sql = "";
        PreparedStatement stmt = null;
        sql = "SELECT patient_profile_id,first_name,last_name FROM Patient_Profile WHERE doctor_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, staffID);
        ResultSet rs = stmt.executeQuery();
        List<Patient> listViewData = new ArrayList<Patient>();
        while(rs.next()) {
            listViewData.add(new Patient(rs.getString(1), rs.getString(2), rs.getString(3)));
        }
        ObservableList<Patient> patients = FXCollections.observableArrayList(listViewData);
        patientList.itemsProperty().setValue(patients);
        System.out.println(patientList.getItems());

        // TODO: Fix convertComboDisplayList()
        // ISSUE: Cannot invoke "com.example.hospitalapplication.Doctor.getName()" because "doctor" is null
        // LINE: 98 and 95, basically in the convertComboDisplayList where doctor.getName() is
        // Uncomment convertComboDisplayList() below to get the error
        //convertComboDisplayList();

    }

    @FXML
    public void setStaffID(String id) throws SQLException {
        staffID = id;
    }

    @FXML
    public void redirectToAppointments(ActionEvent event, String id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-appointment-view.fxml"));
        root = loader.load();

        DoctorAppointmentsController doctorController = loader.getController();
        doctorController.setStaffID(id);
        doctorController.setDate();
        doctorController.setName();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onAppointmentsClicked(ActionEvent event) throws IOException, SQLException {
        redirectToAppointments(event, staffID);
    }

}
