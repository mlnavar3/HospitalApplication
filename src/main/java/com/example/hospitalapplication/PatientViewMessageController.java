package com.example.hospitalapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class PatientViewMessageController {
    String staffID = "";
    String patientID = "";
    int tableViewIndex = 0;
    Connection conn = null;

    private Scene scene;
    private Parent root;

    @FXML
    private Label fromLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private Label contentLabel;

    public void setStaffID(String id)
    {
        staffID = id;
    }

    public void setPatientID(String id)
    {
        patientID = id;
    }

    @FXML
    public void setFromLabel(String from)
    {
        fromLabel.setText(from);
    }

    @FXML
    public void setSubjectLabel(String subject)
    {
        subjectLabel.setText(subject);
    }

    @FXML
    public void setContentLabel(String content)
    {
        contentLabel.setText(content);
    }

    @FXML
    public void setIndex(int index)
    {
        tableViewIndex = index;
    }

    @FXML
    public void redirectToCreateMessage(ActionEvent event, String id) throws IOException, SQLException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-create-messages-view.fxml"));
        root = loader.load();

        PatientCreateMessageController PatientCreateMessageController = loader.getController();
        PatientCreateMessageController.setPatientID(id);
        PatientCreateMessageController.loadRecipientList();
        PatientCreateMessageController.loadSelectedRecipient(tableViewIndex);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void clickReply(ActionEvent event) throws IOException, SQLException
    {
        redirectToCreateMessage(event, patientID);
    }

    @FXML
    //go back to message list
    public void redirectToMessages(ActionEvent event, String id) throws IOException, SQLException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-messages-view.fxml"));
        root = loader.load();

        System.out.println("REDIRECT ID: " + id);

        PatientMessageListController PatientMessagesController = loader.getController();
        PatientMessagesController.setPatientID(id);
        PatientMessagesController.loadMessageList();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void onBackClicked(ActionEvent event) throws IOException, SQLException
    {
        redirectToMessages(event, patientID);
    }

}