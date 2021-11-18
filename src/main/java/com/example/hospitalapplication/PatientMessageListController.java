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

public class PatientMessageListController implements Initializable{
    String patientID = "";
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
    //set the Patients id
    public void setPatientID(String id) throws SQLException
    {
        patientID = id;
    }

    //load the Patient's messages
    public void loadMessageList() throws SQLException
    {
        conn = connect();
        System.out.println(patientID);
        String sql1 = "";
        PreparedStatement stmt1 = null;
        sql1 = "SELECT p.patient_profile_id, p.first_name, p.last_name, s.message_id, s.recipient_id, s.subject, s.content, s.created_at " +
                "FROM Patient_Messages s " +
                "INNER JOIN Patient_Profile p " +
                "ON s.sender_id = p.patient_profile_id " +
                "WHERE s.recipient_id = ?";
        stmt1 = conn.prepareStatement(sql1);
        stmt1.setString(1, patientID);
        ResultSet rs1 = stmt1.executeQuery();

        List<StaffMessage> patientMessageList = new ArrayList<>();
        Patient patient = null;
        StaffMessage patientMessage = null;
        while(rs1.next()) {
            patient = new Patient(rs1.getString(1), rs1.getString(2), rs1.getString(3));
            patientMessage = new StaffMessage(rs1.getString(4), rs1.getString(5), rs1.getString(6), rs1.getString(7), rs1.getString(8));
            patientMessageList.add(new StaffMessage(patient, patientMessage.getSubject(), patientMessage.getContent(), patientMessage.getCreatedAt()));
        }

        ObservableList<StaffMessage> patientMessages = FXCollections.observableArrayList(patientMessageList);
        System.out.println("PATIENT MESSAGES: " + patientMessages);
        tableView.setItems(patientMessages);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-appointment-view.fxml"));
        root = loader.load();

        PatientAppointmentViewController patientController = loader.getController();
        patientController.setPatientID(id);
        patientController.setDate();
        patientController.setName();
        patientController.loadAppointmentList();
        conn.close();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    //go to appointment
    public void onPatientAptClicked(ActionEvent event) throws IOException, SQLException
    {
        redirectToAppointments(event, patientID);
    }

    @FXML
    //create a new message
    public void redirectToCreateMessage(ActionEvent event, String id) throws IOException, SQLException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-create-messages-view.fxml"));
        root = loader.load();

        PatientCreateMessageController PatientCreateMessageController = loader.getController();
        PatientCreateMessageController.setPatientID(id);
        PatientCreateMessageController.loadRecipientList();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    //create message button clicked
    public void createMessageClicked(ActionEvent event) throws IOException, SQLException
    {
        redirectToCreateMessage(event, patientID);
    }

    @FXML
    //view a selected message
    public void redirectToViewMessage(ActionEvent event, StaffMessage message, int index) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-view-individual-message.fxml"));
        root = loader.load();

        PatientViewMessageController PatientViewMessageController = loader.getController();
        PatientViewMessageController.setPatientID(patientID);
        PatientViewMessageController.setPatientID(message.getSender().getId());
        PatientViewMessageController.setFromLabel(message.getSender().getFullName());
        PatientViewMessageController.setSubjectLabel(message.getSubject());
        PatientViewMessageController.setContentLabel(message.getContent());
        PatientViewMessageController.setIndex(index);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    @FXML
    //view message button clicked
    private void viewMessageClicked(ActionEvent event) throws SQLException, IOException
    {
        StaffMessage selectedPatientMessage = tableView.getSelectionModel().getSelectedItem();

        //check if a message was selected
        if(selectedPatientMessage == null)
        {
            MessageAlert.viewMsgError();
        }
        else
        {
            int index = tableView.getSelectionModel().getSelectedIndex();
            redirectToViewMessage(event, selectedPatientMessage, index);
        }
    }

}