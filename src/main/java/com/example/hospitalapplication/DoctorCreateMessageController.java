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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.Date;

import static com.example.hospitalapplication.Connect.*;

public class DoctorCreateMessageController implements Initializable {

    String staffID = "";
    Connection conn = null;

    private Scene scene;
    private Parent root;

    @FXML
    private ComboBox<Patient> recipientList;
    @FXML
    private TextField subjectTxt;
    @FXML
    private TextField contentTxt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void loadRecipientList() throws SQLException {
        conn = connect();

        String sql = "";
        PreparedStatement stmt = null;
        sql = "SELECT patient_profile_id,first_name,last_name FROM Patient_Profile WHERE doctor_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, staffID);
        ResultSet rs = stmt.executeQuery();

        List<Patient> comboboxData = new ArrayList<>();
        while(rs.next()) {
            comboboxData.add(new Patient(rs.getString(1), rs.getString(2), rs.getString(3)));
        }

        ObservableList<Patient> patients = FXCollections.observableArrayList(comboboxData);
        recipientList.itemsProperty().setValue(patients);

        recipientList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Patient Name: " + newValue.getFirstName() + " " + newValue.getLastName());
            System.out.println("Patient Id: " + newValue.getId());
        });
    }

    public void loadSelectedRecipient(int index) {
        recipientList.getSelectionModel().select(index);
    }

    public void setStaffID(String id) throws SQLException {
        staffID = id;
    }

    public String validateInput() {
        String errorMessage = "";

        // Validate recipient
        if (recipientList.getValue() == null) {
            System.out.println("Recipient cannot be empty");
            errorMessage += "Recipient cannot be empty\n";
        }

        // Validate subject
        if (subjectTxt.getText() == null || subjectTxt.getText().trim().isEmpty()) {
            System.out.println("Subject cannot be empty");
            errorMessage += "Subject cannot be empty\n";
        }

        // If error message is empty, return true. Otherwise, return false
        return errorMessage;
    }

    public void onSendClick(ActionEvent event) throws IOException, SQLException {
        String uniqueID = UUID.randomUUID().toString();
        String validRegistrationInput = validateInput();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        String sql = "INSERT INTO Patient_Messages VALUES(?,?,?,?,?,?)";

        if(validRegistrationInput == null || validRegistrationInput.trim().isEmpty()) {
            try {
                conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, uniqueID);
                pstmt.setString(2, recipientList.getValue().getId());
                pstmt.setString(3, staffID);
                pstmt.setString(4, subjectTxt.getText());
                pstmt.setString(5, contentTxt.getText());
                pstmt.setString(6, formatter.format(date));
                pstmt.executeUpdate();
                MessageAlert.sendMessageSuccessfulBox();
                //redirectToMessages(event, staffID);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            MessageAlert.sendMessageErrorBox(validRegistrationInput);
        }
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
}
