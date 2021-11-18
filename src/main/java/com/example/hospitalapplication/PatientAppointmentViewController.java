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
//import java.sql.Date;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.hospitalapplication.Connect.connect;

import java.net.URL;

public class PatientAppointmentViewController implements Initializable {
    String patientID = "", patientName = "";
    Connection conn = null;

    @FXML
    private TableView<Appointment> tableView;
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
        patientName = name;
        welcomelbl.setText("Welcome, " + name);

        conn.close();
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

        DoctorPatientProfileDetailController patientProflieController = loader.getController();
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
        conn = connect();
        String sql1 = "";
        PreparedStatement stmt1 = null;
        sql1 = "SELECT * FROM \"Appointment \" WHERE patient_profile_id=?";
        stmt1 = conn.prepareStatement(sql1);
        stmt1.setString(1, patientID);
        ResultSet rs1 = stmt1.executeQuery();

        List<Appointment> appointmentList = new ArrayList<>();
        while(rs1.next()) {
                int age = 0;
                String tempName= "temp name";
                appointmentList.add(new Appointment(rs1.getString(1), rs1.getDate(3), rs1.getInt(4), rs1.getInt(5), rs1.getDouble(6), rs1.getString(7), rs1.getString(8), rs1.getString(9), tempName, rs1.getString(10), age));
        }

        ObservableList<Appointment> appointments = FXCollections.observableArrayList(appointmentList);
        tableView.setItems(appointments);

        rs1.close();
        conn.close();
        setTableViewData();
    }

    public void setTableViewData() {
        diagnosisColumn.setCellValueFactory(name -> new ReadOnlyStringWrapper(name.getValue().getMedicalComplaint()));
        //dateColumn.setCellValueFactory(Diagnosis -> new ReadOnlyStringWrapper(Diagnosis.getValue().getDate().toString()));
        dateColumn.setCellValueFactory(Diagnosis -> new ReadOnlyStringWrapper(Diagnosis.getValue().getDateString()));
    }

    @FXML
    public void redirectToPatientCreateAppointment(ActionEvent event, String id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-create-appointment.fxml"));
        root = loader.load();

        PatientMakeAppointmentController patientMakeAppointmentController = loader.getController();
        patientMakeAppointmentController.setPatientID(patientID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onMakeAppointmentClicked(ActionEvent event) throws IOException, SQLException {
        redirectToPatientCreateAppointment(event, patientID);
    }

    @FXML
    public void redirectToMessages(ActionEvent event, String id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-messages-view.fxml"));
        root = loader.load();

        //PatientViewMessageController patientViewMessageController = loader.getController();
        //patientViewMessageController.setPatientID(patientID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void onMessagesClicked(ActionEvent event) throws IOException, SQLException {
        redirectToMessages(event, patientID);
    }

}
