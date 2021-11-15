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

public class PatientAppointmentViewController implements Initializable {
    String patientID = "";
    Connection conn = null;

    @FXML
    private TableView<Appointment> tableView = new TableView<>();
    @FXML
    private TableColumn<Appointment, String> dateColumn;
    @FXML
    private TableColumn<Appointment, String> diagnosisColumn;

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
        //Connect to sql database
        conn = Connect.connect();
        String sql = "";
        PreparedStatement stmt = null;
        //get patient name from sql database
        sql = "SELECT first_name,last_name FROM Patient_Profile WHERE patient_profile_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, patientID);
        ResultSet rs = stmt.executeQuery();

        String name = "";

        while(rs.next()) {
            name += rs.getString(1) + " ";
            name += rs.getString(2).charAt(0) + ".";
        }

        //update welcome label with patients name
        welcomelbl.setText("Welcome, " + name);
    }


    @FXML
    public void redirectToProfile(ActionEvent event, String id) throws IOException, SQLException {
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

        PatientProfileDetailController patientProflieController = loader.getController();
        patientProflieController.setPatientID(id);
        patientProflieController.loadPrescriptionList();
        patientProflieController.loadMedicalHistoryList();
        patientProflieController.setData(patientID, firstName, lastName, dob, phoneNumber, email, insurance, pharmacy, staffID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onProfileClicked(ActionEvent event) throws IOException, SQLException {
        redirectToProfile(event, patientID);
    }

    public void loadAppointmentList() throws SQLException {
        //get appointment info from sql database
        conn = Connect.connect();
        String sql = "";
        PreparedStatement stmt = null;
        //get patient info from sql database
        sql = "SELECT * FROM \"Appointment \" WHERE patient_profile_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, patientID);
        ResultSet rs = stmt.executeQuery();

        //list of appointment objects
        ArrayList<Appointment> appointmentList = new ArrayList<>();

        while(rs.next()) {
            appointmentList.add(new Appointment(rs.getString(8) + " " + rs.getString(9), rs.getDouble(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4), rs.getString(5), rs.getString(7),rs.getString(10)));
        };

        ObservableList<Appointment> appointments = FXCollections.observableArrayList(appointmentList);
        tableView.setItems(appointments);

        rs.close();
        conn.close();
        setTableViewData();

    }

    public void setTableViewData() {
        dateColumn.setCellValueFactory(appointment -> new ReadOnlyStringWrapper(appointment.getValue().getDate().toString()));
        diagnosisColumn.setCellValueFactory(appointment -> new ReadOnlyStringWrapper(appointment.getValue().getDoctorSummary()));
    }

}
