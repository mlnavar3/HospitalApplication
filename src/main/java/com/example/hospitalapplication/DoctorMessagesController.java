package com.example.hospitalapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DoctorMessagesController implements Initializable {
    String staffID = "";
    Connection conn = null;

    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void setStaffID(String id) throws SQLException {
        staffID = id;
    }

    @FXML
    public void redirectToAppointments(ActionEvent event, String id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-appointment-view.fxml"));
        root = loader.load();

        DoctorAppointmentsController doctorController = loader.getController();
        doctorController.setStaffID(id);
        doctorController.setDate();
        doctorController.setName();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onAppointmentsClicked(ActionEvent event) throws IOException, SQLException {
        redirectToAppointments(event, staffID);
    }

    @FXML
    public void redirectToCreateMessage(ActionEvent event, String id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-create-messages-view.fxml"));
        root = loader.load();

        DoctorCreateMessageController doctorCreateMessageController = loader.getController();
        doctorCreateMessageController.setStaffID(id);
        doctorCreateMessageController.loadRecipientList();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onCreateMessagesClicked(ActionEvent event) throws IOException, SQLException {
        redirectToCreateMessage(event, staffID);
    }

}
