package com.example.hospitalapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DoctorCreateMessageController {

    String staffID = "";
    Connection conn = null;

    private Scene scene;
    private Parent root;

    @FXML
    public void setStaffID(String id) throws SQLException {
        staffID = id;
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
}
