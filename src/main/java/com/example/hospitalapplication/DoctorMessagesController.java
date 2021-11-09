package com.example.hospitalapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.hospitalapplication.Connect.connect;

public class DoctorMessagesController implements Initializable {
    String staffID = "";
    Connection conn = null;

    private Scene scene;
    private Parent root;

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn fromColumn;
    @FXML
    private TableColumn roleColumn;
    @FXML
    private TableColumn subjectColumn;
    @FXML
    private TableColumn timeColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void setStaffID(String id) throws SQLException {
        staffID = id;
    }

    public void loadMessageList() throws SQLException {
        conn = connect();

        String sql1 = "";
        PreparedStatement stmt1 = null;
        sql1 = "SELECT message_id, sender_id, subject, content, created_at FROM Messages WHERE recipient_id = ?";
        stmt1 = conn.prepareStatement(sql1);
        stmt1.setString(1, staffID);
        ResultSet rs1 = stmt1.executeQuery();

        String sql2 = "";
        PreparedStatement stmt2 = null;
        sql2 = "SELECT first_name, last_name, role_id FROM Patient_Profile AND Staff WHERE recipient_id = ?";
        stmt2 = conn.prepareStatement(sql2);
        stmt2.setString(1, rs1.getString(2));
        ResultSet rs2 = stmt2.executeQuery();

        List<Message> messageList = new ArrayList<>();
        while(rs1.next() && rs2.next()) {
            String role = "";
            if(rs2.getString(3) == "1") {
                role = "Doctor";
            } else if(rs2.getString(3) == "2") {
                role = "Nurse";
            } else {
                role = "Patient";
            }
            messageList.add(new Message(rs1.getString(1), rs2.getString(1) + " " + rs2.getString(2), role, rs1.getString(3), rs1.getString(4), rs1.getString(5)));
        }

        ObservableList<Message> messages = FXCollections.observableArrayList(messageList);
        System.out.println(messages);
        tableView.itemsProperty().setValue(messages);

        /*recipientList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Patient Name: " + newValue.getFirstName() + " " + newValue.getLastName());
            System.out.println("Patient Id: " + newValue.getId());
        });*/
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
