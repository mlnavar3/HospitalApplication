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
//import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.hospitalapplication.Connect.connect;

import java.net.URL;

public class PatientMakeAppointmentController implements Initializable {
    String patientID = "", complaint = "", appointmentID = "";
    Date appointmentDate;
    java.sql.Date currentDate;
    Connection conn = null;

    private Scene scene;
    private Parent root;

    @FXML
    private DatePicker myDatePicker;

    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setPatientID(String id) throws SQLException{
        patientID = id;
    }

    //get appointment date
    public void getAppointmentDate() {
        appointmentDate = Date.valueOf(myDatePicker.getValue());
    }

    //get current date
    public void getCurrentDate() {
        currentDate = new java.sql.Date(System.currentTimeMillis());
    }

    //get appointment id
    public void getAppointmentID() {
        appointmentID = UUID.randomUUID().toString();
    }

    //get medical complaint
    public void getComplaint() {
        complaint = textArea.getText();
    }

    //create appointment and add to database
    @FXML
    public void createAppointment() throws IOException, SQLException {
        //add empty values
        int intZero = 0;
        double doubleZero = 0;
        String emptyString = "empty";

        //get additional appointment info
        try {
            getAppointmentDate();
        } catch (NullPointerException e) {
            System.out.println(e);
            MessageAlert.nullDate();
        }
        getCurrentDate();
        getAppointmentID();
        getComplaint();

        String sql1 = "INSERT INTO [Appointment ] VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        if(complaint == null || complaint.isEmpty()) {
            MessageAlert.appointmentCreatedFail();
        }else {
            try {
                conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql1);
                pstmt.setString(1, appointmentID);
                pstmt.setDate(2, currentDate);
                pstmt.setDate(3, appointmentDate);
                pstmt.setInt(4, intZero);//weight
                pstmt.setInt(5, intZero);//height
                pstmt.setDouble(6, doubleZero);//bodyTemp
                pstmt.setString(7, emptyString);//bloodPressure
                pstmt.setString(8, complaint);
                pstmt.setString(9, emptyString);//doctorSummary
                pstmt.setString(10, patientID);
                pstmt.setString(11, null);
                pstmt.executeUpdate();
                MessageAlert.appointmentCreated();
                pstmt.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

        conn.close();
    }

    @FXML
    public void onMakeAppointmentClicked(ActionEvent event) throws IOException, SQLException {
        createAppointment();
    }


    @FXML
    public void redirectToPatientAppointmentView(ActionEvent event, String id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-appointment-view.fxml"));
        root = loader.load();

        PatientAppointmentViewController patientController = loader.getController();
        patientController.setPatientID(id);
        patientController.setDate();
        patientController.setName();
        patientController.loadAppointmentList();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onBackClicked(ActionEvent event) throws IOException, SQLException {
        redirectToPatientAppointmentView(event, patientID);
    }

}
