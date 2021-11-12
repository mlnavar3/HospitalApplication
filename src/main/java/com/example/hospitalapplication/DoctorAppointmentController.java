package com.example.hospitalapplication;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.Date;

import static com.example.hospitalapplication.Connect.connect;

public class DoctorAppointmentController {
    String appointmentID = "";
    String staffID = "";
    String patientID = "";

    private Scene scene;
    private Parent root;
    Connection conn = null;

    @FXML
    private ListView<MedicalHistory> diagnosisList;
    @FXML
    private TableView<Prescription> prescriptionList;

    @FXML
    private Label nameLbl;
    @FXML
    private Label weightLbl;
    @FXML
    private Label heightLbl;
    @FXML
    private Label bodyTempLbl;
    @FXML
    private Label bloodPressureLbl;

    @FXML
    private TextField diagnosisTxt;
    @FXML
    private TextField prescriptionTxt;
    @FXML
    private TextField quantityTxt;
    @FXML
    private TextArea summaryTxtArea;

    @FXML
    private TableColumn<Prescription, String> prescriptionColumn;
    @FXML
    private TableColumn<Prescription, String> quantityColumn;

    List<MedicalHistory> diagnosisArray = new ArrayList<>();
    List<Prescription> prescriptionArray = new ArrayList<>();

    public void setAppointmentID(String id) throws SQLException {
        appointmentID = id;
    }

    public void setStaffID(String id) throws SQLException {
        staffID = id;
    }

    public void setPatientID(String id) throws SQLException {
        patientID = id;
    }

    public void setName(String name) {nameLbl.setText(name);};

    public void setWeight(double weight) {weightLbl.setText(String.valueOf(weight) + " lbs");}

    public void setHeight(double height) {
        heightLbl.setText(String.valueOf(height) + " cm");
    }

    public void setBodyTemp(double bodyTemp) {
        bodyTempLbl.setText(String.valueOf(bodyTemp) + " F");
    }

    public void setBloodPressure(String bloodPressure) {
        bloodPressureLbl.setText(bloodPressure + " mmHg");
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public void removeDiagnosis() {
        int index = diagnosisList.getSelectionModel().getSelectedIndex();
        diagnosisArray.remove(index);
        updateDiagnosisList();
    }

    public void addDiagnosis() {
        String uniqueID = UUID.randomUUID().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        MedicalHistory medicalHistory = new MedicalHistory(uniqueID, diagnosisTxt.getText(), formatter.format(date));
        diagnosisArray.add(medicalHistory);
        diagnosisTxt.setText("");
        updateDiagnosisList();
    }

    public void updateDiagnosisList() {
        ObservableList<MedicalHistory> diagnosis = FXCollections.observableArrayList(diagnosisArray);
        diagnosisList.setItems(diagnosis);
    }

    public void addPrescription() {
        if(isNumeric(quantityTxt.getText())) {
            String uniqueID = UUID.randomUUID().toString();
            Prescription prescription = new Prescription(uniqueID, prescriptionTxt.getText(), Integer.parseInt(quantityTxt.getText()), true);
            prescriptionArray.add(prescription);
            prescriptionTxt.setText("");
            updatePrescriptionList();
        } else {
            MessageAlert.prescriptionErrorBox("Quantity need to be numeric");
        }
    }

    public void removePrescription() {
        int index = prescriptionList.getSelectionModel().getSelectedIndex();
        prescriptionArray.remove(index);
        updatePrescriptionList();
    }

    public void updatePrescriptionList() {
        ObservableList<Prescription> prescriptions = FXCollections.observableArrayList(prescriptionArray);
        prescriptionList.setItems(prescriptions);
        setTableViewData();
    }

    private void setTableViewData() {
        prescriptionColumn.setCellValueFactory(prescription -> new ReadOnlyStringWrapper(prescription.getValue().getName()));
        quantityColumn.setCellValueFactory(prescription -> new ReadOnlyStringWrapper(Integer.toString(prescription.getValue().getQuantity())));
    }

    @FXML
    private void onCompleteClick(ActionEvent event) throws SQLException, IOException {
        for (int i = 0; i < diagnosisArray.size(); i++) {
            MedicalHistory medicalHistory = diagnosisList.getItems().get(i);
            String sql1 = "INSERT INTO Medical_History VALUES(?,?,?)";
            try {
                conn = Connect.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql1);
                pstmt.setString(1, medicalHistory.getId());
                pstmt.setString(2, medicalHistory.getDiagnosis());
                pstmt.setString(3, medicalHistory.getDate());
                pstmt.executeUpdate();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            String sql2 = "INSERT INTO Patient_Medical_History VALUES(?,?)";
            try {
                conn = Connect.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql2);
                pstmt.setString(1, patientID);
                pstmt.setString(2, medicalHistory.getId());
                pstmt.executeUpdate();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int j = 0; j < prescriptionArray.size(); j++) {
            Prescription prescription = prescriptionList.getItems().get(j);
            String sql1 = "INSERT INTO Prescription VALUES(?,?,?,?)";
            try {
                conn = Connect.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql1);
                pstmt.setString(1, prescription.getId());
                pstmt.setString(2, prescription.getName());
                pstmt.setInt(3, prescription.getQuantity());
                pstmt.setBoolean(4, prescription.getActive());
                pstmt.executeUpdate();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            String sql2 = "INSERT INTO Patient_Prescription VALUES(?,?)";
            try {
                conn = Connect.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql2);
                System.out.println(patientID);
                System.out.println(prescription.getId());
                pstmt.setString(1, patientID);
                pstmt.setString(2, prescription.getId());
                pstmt.executeUpdate();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        String sql2 = "UPDATE [Appointment ] SET doctor_summary = ? WHERE appointment_id = ?";

        if(summaryTxtArea.getText() == null || summaryTxtArea.getText().trim().isEmpty()) {
            MessageAlert.summaryErrorBox();
        } else {
            try {
                conn = Connect.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql2);
                pstmt.setString(1, summaryTxtArea.getText());
                pstmt.setString(2, appointmentID);
                pstmt.executeUpdate();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        MessageAlert.appointmentSuccessfulBox();
        redirectToAppointments(event);
    }

    @FXML
    public void redirectToAppointments(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-appointment-list-view.fxml"));
        root = loader.load();

        DoctorAppointmentListController doctorController = loader.getController();
        doctorController.setStaffID(staffID);
        doctorController.setDate();
        doctorController.setName();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }
}
