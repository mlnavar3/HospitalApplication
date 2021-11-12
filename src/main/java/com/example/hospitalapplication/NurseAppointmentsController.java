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

public class NurseAppointmentsController implements Initializable
{
    String staffID = "";
    Connection conn = null;

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

    //increment date
    public void onNextDay() throws ParseException
    {
        String dt = aptDate.getText();  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));
        c.add(Calendar.DATE, 1);  // number of days to add
        dt = sdf.format(c.getTime());
        aptDate.setText(dt);
    }

    //go to previous day
    public void onPrevDay() throws ParseException
    {
        String dt = aptDate.getText();  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));
        c.add(Calendar.DATE, -1);  // number of days to add
        dt = sdf.format(c.getTime());
        aptDate.setText(dt);
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

}
