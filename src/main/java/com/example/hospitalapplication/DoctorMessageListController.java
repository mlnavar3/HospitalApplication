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

public class DoctorMessageListController implements Initializable {
    String staffID = "";
    Connection conn = null;

    private Scene scene;
    private Parent root;

    @FXML
    private TableView<StaffMessage> tableView;
    @FXML
    private TableColumn<StaffMessage, String> fromColumn;
    @FXML
    private TableColumn<StaffMessage, String> subjectColumn;
    @FXML
    private TableColumn<StaffMessage, String> timeColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadMessageList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setTableViewData();
    }

    @FXML
    public void setStaffID(String id) throws SQLException {
        staffID = id;
    }

    public void loadMessageList() throws SQLException {
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
        tableView.setItems(staffMessages);
    }

    private void setTableViewData() {
        fromColumn.setCellValueFactory(name -> new ReadOnlyStringWrapper(name.getValue().getSender().getFullName()));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    }


    @FXML
    public void redirectToAppointments(ActionEvent event, String id) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-appointment-list-view.fxml"));
        root = loader.load();

        DoctorAppointmentListController doctorController = loader.getController();
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

    @FXML
    public void redirectToViewMessage(ActionEvent event, StaffMessage message, int index) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("message-view.fxml"));
        root = loader.load();

        DoctorViewMessageController doctorViewMessageController = loader.getController();
        doctorViewMessageController.setStaffID(staffID);
        doctorViewMessageController.setPatientID(message.getSender().getId());
        doctorViewMessageController.setFromLbl(message.getSender().getFullName());
        doctorViewMessageController.setSubjectLbl(message.getSubject());
        doctorViewMessageController.setContentLbl(message.getContent());
        doctorViewMessageController.setIndex(index);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void onViewMessage(ActionEvent event) throws SQLException, IOException {
        StaffMessage selectedStaffMessage = tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        redirectToViewMessage(event, selectedStaffMessage, index);
    }
}
