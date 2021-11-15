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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.hospitalapplication.Connect.connect;

public class NurseMessageListController implements Initializable{
    String staffID = "";
    Connection conn = null;

    private Scene scene;
    private Parent root;

    @FXML
    private TableView<StaffMessage> tableView;
    @FXML
    private TableColumn<StaffMessage, String> fromColumn;
    @FXML
    private TableColumn<StaffMessage, String> subColumn;
    @FXML
    private TableColumn<StaffMessage, String> timeColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try {
            loadMessageList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setTableViewData();
    }

    @FXML
    //set the nurses id
    public void setStaffID(String id) throws SQLException
    {
        staffID = id;
    }

    //load the nurse's messages
    public void loadMessageList() throws SQLException
    {
        conn = connect();
        System.out.println(staffID);
        String sql1 = "";
        PreparedStatement stmt1 = null;
        sql1 = "SELECT p.patient_profile_id, p.first_name, p.last_name, s.message_id, s.recipient_id, s.subject, s.content, s.created_at " +
                "FROM Staff_Messages s " +
                "INNER JOIN Patient_Profile p " +
                "ON s.sender_id = p.patient_profile_id " +
                "WHERE s.recipient_id = ?";
        stmt1 = conn.prepareStatement(sql1);
        stmt1.setString(1, staffID);
        ResultSet rs1 = stmt1.executeQuery();

        List<StaffMessage> staffMessageList = new ArrayList<>();
        Patient patient = null;
        StaffMessage staffMessage = null;
        while(rs1.next()) {
            patient = new Patient(rs1.getString(1), rs1.getString(2), rs1.getString(3));
            staffMessage = new StaffMessage(rs1.getString(4), rs1.getString(5), rs1.getString(6), rs1.getString(7), rs1.getString(8));
            staffMessageList.add(new StaffMessage(patient, staffMessage.getSubject(), staffMessage.getContent(), staffMessage.getCreatedAt()));
        }

        ObservableList<StaffMessage> staffMessages = FXCollections.observableArrayList(staffMessageList);
        System.out.println("STAFFMESSAGES: " + staffMessages);
        tableView.setItems(staffMessages);
    }

    private void setTableViewData()
    {
        fromColumn.setCellValueFactory(name -> new ReadOnlyStringWrapper(name.getValue().getSender().getFullName()));
        subColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    }

    @FXML
    //go back to appointments
    public void redirectToAppointments(ActionEvent event, String id) throws IOException, SQLException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("nurse-appointment-view.fxml"));
        root = loader.load();

        NurseAppointmentsController nurseController = loader.getController();
        nurseController.setStaffID(id);
        nurseController.setDate();
        nurseController.setName();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    //go to appointment
    public void onNurseAptClicked(ActionEvent event) throws IOException, SQLException
    {
        redirectToAppointments(event, staffID);
    }

    @FXML
    //create a new message
    public void redirectToCreateMessage(ActionEvent event, String id) throws IOException, SQLException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("nurse-create-messages-view.fxml"));
        root = loader.load();

        NurseCreateMessageController nurseCreateMessageController = loader.getController();
        nurseCreateMessageController.setStaffID(id);
        nurseCreateMessageController.loadRecipientList();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    //create message button clicked
    public void createMessageClicked(ActionEvent event) throws IOException, SQLException
    {
        redirectToCreateMessage(event, staffID);
    }

    @FXML
    //view a selected message
    public void redirectToViewMessage(ActionEvent event, StaffMessage message, int index) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("nurse-view-individual-message.fxml"));
        root = loader.load();

        NurseViewMessageController nurseViewMessageController = loader.getController();
        nurseViewMessageController.setStaffID(staffID);
        nurseViewMessageController.setPatientID(message.getSender().getId());
        nurseViewMessageController.setFromLabel(message.getSender().getFullName());
        nurseViewMessageController.setSubjectLabel(message.getSubject());
        nurseViewMessageController.setContentLabel(message.getContent());
        nurseViewMessageController.setIndex(index);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    @FXML
    //view message button clicked
    private void viewMessageClicked(ActionEvent event) throws SQLException, IOException
    {
        StaffMessage selectedStaffMessage = tableView.getSelectionModel().getSelectedItem();

        //check if a message was selected
        if(selectedStaffMessage == null)
        {
            MessageAlert.viewMsgError();
        }
        else
        {
            int index = tableView.getSelectionModel().getSelectedIndex();
            redirectToViewMessage(event, selectedStaffMessage, index);
        }
    }

}
