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

public class NurseAppointmentsController implements Initializable
{
    String staffID = "";
    Connection conn = null;

    @FXML
    private TableView<Appointment> tableView;

    @FXML
    private TableColumn<Appointment, String> patientCol;

    @FXML
    private TableColumn<Appointment, String> doctorCol;

    @FXML
    private TableColumn<Appointment, String> statusCol;

    @FXML
    private Label aptDate;
    @FXML
    private Label nurseWlcm;

    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    @FXML
    //date of appointments to be displayed
    public void setDate() throws SQLException
    {
        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime());
        aptDate.setText(timeStamp);
    }

    @FXML
    //nurse id
    public void setStaffID(String id) throws SQLException
    {
        staffID = id;
    }

    @FXML
    //get the name of the nurse from the database
    public void setName() throws SQLException
    {
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

        nurseWlcm.setText(name);
    }

    //get the doctors name for an appointment by passing in the appointment id
    public String getDoctorName(String aptID) throws SQLException {
        conn = connect();
        String sql1 = "";
        PreparedStatement stmt1 = null;
        sql1 = "SELECT st.first_name, st.last_name FROM Staff st "
                + "INNER JOIN Shift sh ON sh.staff_id = st.staff_id WHERE sh.appointment_id = ? AND role_id=1";
        stmt1 = conn.prepareStatement(sql1);
        stmt1.setString(1, aptID);
        ResultSet rs1 = stmt1.executeQuery();

        String docName = "";

        while(rs1.next())
        {
            docName += rs1.getString(1) + " ";
            docName += rs1.getString(2).charAt(0) + ".";
        }

        return docName;
    }

    //show appointments on a given date
    public void loadAppointmentList() throws SQLException
    {
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
            String dt = aptDate.getText();
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
        patientCol.setCellValueFactory(name -> new ReadOnlyStringWrapper(name.getValue().getPatientName()));
        doctorCol.setCellValueFactory(appointment -> {
            try {
                return new ReadOnlyStringWrapper(getDoctorName(appointment.getValue().getId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
        statusCol.setCellValueFactory(status -> new ReadOnlyStringWrapper(status.getValue().getStatus()));
    }

    //increment date
    public void onNextDay() throws ParseException, SQLException
    {
        String dt = aptDate.getText();  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));
        c.add(Calendar.DATE, 1);  // number of days to add
        dt = sdf.format(c.getTime());
        aptDate.setText(dt);
        loadAppointmentList();
    }

    //go to previous day
    public void onPrevDay() throws ParseException, SQLException
    {
        String dt = aptDate.getText();  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));
        c.add(Calendar.DATE, -1);  // number of days to add
        dt = sdf.format(c.getTime());
        aptDate.setText(dt);
        loadAppointmentList();
    }

    //appointment notes button clicked
    public void onStartAptNotes()
    {
        System.out.println("Start Appointment Notes button clicked!");
    }

    @FXML
    //go to messages
    public void redirectToMessages(ActionEvent event, String id) throws IOException, SQLException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("nurse-messages-view.fxml"));
        root = loader.load();

        NurseMessageListController nurseMessagesController = loader.getController();
        nurseMessagesController.setStaffID(id);
        nurseMessagesController.loadMessageList();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    //go to messages
    public void onNurseClickMsg(ActionEvent event) throws IOException, SQLException
    {
        redirectToMessages(event, staffID);
    }

    //go to new apt form
    public void onNewApt(ActionEvent event) throws IOException, SQLException
    {
        System.out.println("New appointment selected");
       // redirectToNewApt(event, staffID);
    }
/*
    //got to new appointment form
    public void redirectToNewApt(ActionEvent event, String id) throws IOException, SQLException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("nurse-new-apt-view.fxml"));
        root = loader.load();

        NurseMessageListController nurseMessagesController = loader.getController();
        nurseMessagesController.setStaffID(id);
        nurseMessagesController.loadMessageList();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }
    */


}