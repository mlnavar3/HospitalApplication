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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.hospitalapplication.Connect.connect;

public class DoctorAppointmentListController implements Initializable {
    String staffID = "";
    Connection conn = null;

    @FXML
    private TableView<Appointment> tableView;
    @FXML
    private TableColumn<Appointment, String> patientColumn;
    @FXML
    private TableColumn<Appointment, String> statusColumn;

    @FXML
    private Label date;
    @FXML
    private Label welcomeLbl;

    private Scene scene;
    private Parent root;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void setDate() throws SQLException {
        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime());
        date.setText(timeStamp);
    }

    @FXML
    public void setStaffID(String id) throws SQLException {
        staffID = id;
    }

    @FXML
    public void setName() throws SQLException {
        conn = Connect.connect();
        String sql = "";
        PreparedStatement stmt = null;
        sql = "SELECT first_name,last_name FROM Staff WHERE staff_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, staffID);
        ResultSet rs = stmt.executeQuery();

        String name = "";

        while(rs.next()) {
            name += rs.getString(1) + " ";
            name += rs.getString(2).charAt(0) + ".";
        }

        welcomeLbl.setText(name);
    }

    public void loadAppointmentList() throws SQLException {
        conn = connect();
        String sql1 = "";
        PreparedStatement stmt1 = null;
        sql1 = "SELECT a.weight, a.height, a.body_temperature, a.blood_pressure, a.doctor_summary, a.appointment_date, p.patient_profile_id, p.first_name, p.last_name, a.appointment_id " +
                "FROM Shift s " +
                "INNER JOIN [Appointment ] a " +
                "ON a.appointment_id = s.appointment_id " +
                "INNER JOIN Patient_Profile p " +
                "ON a.patient_profile_id = p.patient_profile_id " +
                "WHERE s.staff_id = ?";
        stmt1 = conn.prepareStatement(sql1);
        stmt1.setString(1, staffID);
        ResultSet rs1 = stmt1.executeQuery();

        List<Appointment> appointmentList = new ArrayList<>();
        while(rs1.next()) {
            String dt = date.getText();
            String appDt = rs1.getString(6).substring(0,10);  // Start date

            if(dt.equals(appDt)) {
                appointmentList.add(new Appointment(rs1.getString(8) + " " + rs1.getString(9), rs1.getDouble(1), rs1.getDouble(2), rs1.getDouble(3), rs1.getString(4), rs1.getString(5), rs1.getString(7),rs1.getString(10)));
            }
        }

        ObservableList<Appointment> appointments = FXCollections.observableArrayList(appointmentList);
        tableView.setItems(appointments);

        rs1.close();
        conn.close();
        setTableViewData();
    }

    public void setTableViewData() {
        patientColumn.setCellValueFactory(name -> new ReadOnlyStringWrapper(name.getValue().getPatientName()));
        statusColumn.setCellValueFactory(status -> new ReadOnlyStringWrapper(status.getValue().getStatus()));
    }

    public void changeToNextDay() throws ParseException, SQLException {
        String dt = date.getText();  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));
        c.add(Calendar.DATE, 1);  // number of days to add
        dt = sdf.format(c.getTime());
        date.setText(dt);
        loadAppointmentList();
    }

    public void changeToPreviousDay() throws ParseException, SQLException {
        String dt = date.getText();  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));
        c.add(Calendar.DATE, -1);  // number of days to add
        dt = sdf.format(c.getTime());
        date.setText(dt);
        loadAppointmentList();
    }

    @FXML
    public void redirectToMessages(ActionEvent event, String id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-messages-view.fxml"));
        root = loader.load();

        DoctorMessageListController doctorMessagesController = loader.getController();
        doctorMessagesController.setStaffID(id);
        doctorMessagesController.loadMessageList();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onMessagesClicked(ActionEvent event) throws IOException, SQLException {
        redirectToMessages(event, staffID);
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

    public void onPatientListClicked(ActionEvent event) throws IOException, SQLException {
        redirectToPatientList(event, staffID);
    }

    @FXML
    public void redirectToDiagnosis(ActionEvent event, Appointment appointment) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-appointment-view.fxml"));
        root = loader.load();

        DoctorAppointmentController doctorAppointmentController = loader.getController();
        doctorAppointmentController.setAppointmentID(appointment.getId());
        doctorAppointmentController.setStaffID(staffID);
        doctorAppointmentController.setPatientID(appointment.getPatientID());
        doctorAppointmentController.setName(appointment.getPatientName());
        doctorAppointmentController.setWeight(appointment.getWeight());
        doctorAppointmentController.setHeight(appointment.getHeight());
        doctorAppointmentController.setBodyTemp(appointment.getBodyTemp());
        doctorAppointmentController.setBloodPressure(appointment.getBloodPressure());

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onDiagnosisClicked(ActionEvent event) throws IOException, SQLException {
        Appointment selectedAppointment = tableView.getSelectionModel().getSelectedItem();
        redirectToDiagnosis(event, selectedAppointment);
    }

}
