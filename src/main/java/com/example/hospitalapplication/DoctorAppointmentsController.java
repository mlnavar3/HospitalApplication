package com.example.hospitalapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class DoctorAppointmentsController implements Initializable {
    String staffID = "";
    Connection conn = null;

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
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
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
        sql = "SELECT firstName,lastName FROM Staff WHERE staff_id = ?";
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

    public void changeToNextDay() throws ParseException {
        String dt = date.getText();  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));
        c.add(Calendar.DATE, 1);  // number of days to add
        dt = sdf.format(c.getTime());
        date.setText(dt);
    }

    public void changeToPreviousDay() throws ParseException {
        String dt = date.getText();  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));
        c.add(Calendar.DATE, -1);  // number of days to add
        dt = sdf.format(c.getTime());
        date.setText(dt);
    }

    public void onStartDiagnosisClick() {
        System.out.println("Start Diagnosis button clicked!");
    }

    @FXML
    public void redirectToMessages(ActionEvent event, String id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-messages-view.fxml"));
        root = loader.load();

        DoctorMessagesController doctorMessagesController = loader.getController();
        doctorMessagesController.setStaffID(id);

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

}
