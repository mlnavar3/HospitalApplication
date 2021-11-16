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

public class DoctorPatientProfileDetailController implements Initializable {
    private Scene scene;
    private Parent root;
    Connection conn = null;

    String staffID = "";
    String patientID = "";

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

    @FXML
    private TableView<Prescription> prescriptionTable;
    @FXML
    private TableColumn<Prescription, String> prescriptionColumn;
    @FXML
    private TableColumn<Prescription, Integer> quantityColumn;
    @FXML
    private TableColumn<Prescription, Boolean> activeColumn;

    @FXML
    private TableView<MedicalHistory> diagnosisTable;
    @FXML
    private TableColumn<MedicalHistory, String> diagnosisColumn;
    @FXML
    private TableColumn<MedicalHistory, String> dateColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    public void setStaffID(String id) throws SQLException {
        staffID = id;
    }
    @FXML
    public void setPatientID(String id) throws SQLException {
        patientID = id;
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

    public void loadMedicalHistoryList() throws SQLException {
        conn = Connect.connect();

        String sql1 = "";
        PreparedStatement stmt1 = null;
        sql1 = "SELECT medical_history_id FROM Patient_Medical_History WHERE patient_profile_id = ?";
        stmt1 = conn.prepareStatement(sql1);
        stmt1.setString(1, patientID);
        ResultSet rs1 = stmt1.executeQuery();

        List<MedicalHistory> medicalHistoryList = new ArrayList<>();
        while(rs1.next()) {
            conn = Connect.connect();
            String sql2 = "SELECT diagnosis, diagnosis_date FROM Medical_History WHERE medical_history_id = ?";
            PreparedStatement stmt2 = null;
            stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, rs1.getString(1));
            ResultSet rs2 = stmt2.executeQuery();
            medicalHistoryList.add(new MedicalHistory(rs1.getString(1), rs2.getString(1), rs2.getString(2)));
            stmt2.close();
        }

        stmt1.close();
        conn.close();

        ObservableList<MedicalHistory> medicalHistories = FXCollections.observableArrayList(medicalHistoryList);
        diagnosisTable.setItems(medicalHistories);
        setMedicalHistoryData();
    }

    public void setMedicalHistoryData() {
        diagnosisColumn.setCellValueFactory(diagnosis -> new ReadOnlyStringWrapper(diagnosis.getValue().getDiagnosis()));
        dateColumn.setCellValueFactory(diagnosis -> new ReadOnlyStringWrapper(diagnosis.getValue().getDate()));
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


    @FXML
    public void redirectToOrderPrescription(ActionEvent event, String patientID) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-order-prescription.fxml"));
        root = loader.load();

        DoctorPrescriptionController doctorPrescriptionController = loader.getController();
        doctorPrescriptionController.setStaffID(staffID);
        doctorPrescriptionController.setPatientID(patientID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onOrderPrescriptionClick(ActionEvent event) throws SQLException, IOException {
        redirectToOrderPrescription(event, patientID);
    }

    @FXML
    public void redirectToAppointmentView(ActionEvent event, String id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-list-of-patients-view.fxml"));
        root = loader.load();

        DoctorPatientsListController doctorPatientsListController = loader.getController();
        doctorPatientsListController.setStaffID(staffID);
        doctorPatientsListController.loadPatientList();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onBackClicked(ActionEvent event) throws IOException, SQLException {
        redirectToAppointmentView(event, staffID);
    }

}
