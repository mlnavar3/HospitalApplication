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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.hospitalapplication.Connect.connect;

public class PatientPrescriptionController implements Initializable {
    private Scene scene;
    private Parent root;
    Connection conn = null;


    String patientID = "";


    @FXML
    private TableView<Prescription> prescriptionTable;
    @FXML
    private TableColumn<Prescription, String> prescriptionColumn;
    @FXML
    private TableColumn<Prescription, Integer> quantityColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    public void setPatientID(String id) throws SQLException {
        patientID = id;
    }


    public void loadPrescriptionList() throws SQLException {
        conn = Connect.connect();

        String sql1 = "";
        PreparedStatement stmt1 = null;
        sql1 = "SELECT prescription_id FROM Patient_Prescription WHERE patient_profile_id = ?";
        stmt1 = conn.prepareStatement(sql1);
        stmt1.setString(1, patientID);
        ResultSet rs1 = stmt1.executeQuery();

        List<Prescription> prescriptionList = new ArrayList<>();
        while(rs1.next()) {
            conn = Connect.connect();
            String sql2 = "SELECT prescription_id, prescription_name, quantity, active FROM Prescription WHERE prescription_id = ?";
            PreparedStatement stmt2 = null;
            stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, rs1.getString(1));
            ResultSet rs2 = stmt2.executeQuery();
            prescriptionList.add(new Prescription(rs2.getString(1),  rs2.getString(2), rs2.getInt(3), rs2.getBoolean(4)));
            stmt2.close();
        }

        stmt1.close();
        conn.close();

        ObservableList<Prescription> prescriptions = FXCollections.observableArrayList(prescriptionList);
        prescriptionTable.setItems(prescriptions);
        setPrescriptionData();
    }


    public void setPrescriptionData() {
        prescriptionColumn.setCellValueFactory(name -> new ReadOnlyStringWrapper(name.getValue().getName()));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }


    public void onRequestClicked() {

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


    public void onBackClicked(ActionEvent event) throws IOException, SQLException {
        redirectToProfile(event, patientID);
    }


    public void onRequestClicked(ActionEvent event) throws IOException, SQLException {
        MessageAlert.requestPrescription();
    }

}
