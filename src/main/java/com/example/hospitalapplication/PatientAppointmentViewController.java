package com.example.hospitalapplication;

import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static com.example.hospitalapplication.Connect.connect;

import java.net.URL;

public class PatientAppointmentViewController implements Initializable {
    String patientID = "";
    Connection conn = null;

    @FXML
    private Label date;
    @FXML
    private Label welcomelbl;

    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void setDate() throws SQLException {
        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime());
        //date.setText(timeStamp);
    }

    @FXML
    public void setPatientID(String id) throws SQLException {
        patientID = id;
    }

    @FXML
    public void setName() throws SQLException {
        conn = Connect.connect();
        String sql = "";
        PreparedStatement stmt = null;
        sql = "SELECT first_name,last_name FROM Patient_Profile WHERE patient_profile_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, patientID);
        ResultSet rs = stmt.executeQuery();

        String name = "";

        while(rs.next()) {
            name += rs.getString(1) + " ";
            name += rs.getString(2).charAt(0) + ".";
        }

        System.out.println(name);
        welcomelbl.setText("Welcome, " + name);
    }


}
