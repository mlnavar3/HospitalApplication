package com.example.hospitalapplication;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField loginUsernameField;
    @FXML
    private TextField loginPasswordField;
    @FXML
    private RadioButton doctorNurseRadioButton;
    @FXML
    private RadioButton patientRadioButton;

    final ToggleGroup doctorNursePatientGroup = new ToggleGroup();

    Connection conn = null;
    private String usernameDb, passwordDb;
    static RadioButton role;
    static String loginRole;

    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        doctorNurseRadioButton.setToggleGroup(doctorNursePatientGroup);
        patientRadioButton.setToggleGroup(doctorNursePatientGroup);

        patientRadioButton.setSelected(true);
    }

    // login-view
    @FXML
    public void onRegisterHereClick(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register-view.fxml"));
        root = loader.load();

        RegisterController registerController = loader.getController();

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public void redirectToDoctorPortal(ActionEvent event, String id) throws IOException, SQLException {
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

    public void redirectToNursePortal(ActionEvent event) throws IOException {
        Parent registerViewParent = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        Scene registerViewScene = new Scene(registerViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene((registerViewScene));
        window.show();
    }

    public void redirectToPatientPortal(ActionEvent event) throws IOException {
        Parent registerViewParent = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        Scene registerViewScene = new Scene(registerViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene((registerViewScene));
        window.show();
    }

    private void redirectPortal(ActionEvent event, String role, String id) throws IOException, SQLException {
        switch (role){
            case "Doctor":
                System.out.println("Doctor Redirect");
                redirectToDoctorPortal(event, id);
                break;
            case "Nurse":
                System.out.println("Nurse Redirect");
                break;
            case "Patient":
                System.out.println("Patient Redirect");
                break;
        }
    }

    @FXML
    public void login(ActionEvent event){
        role = (RadioButton) doctorNursePatientGroup.getSelectedToggle();
        checkLogin(event);
    }

    private void checkLogin(ActionEvent event){
        conn = Connect.connect();
        String sql = "";
        PreparedStatement stmt = null;

        switch (role.getText()) {
            case "Doctor/Nurse":
                sql = "SELECT staff_id,role_id FROM Staff WHERE username = ? AND password = ?";
                try {

                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, loginUsernameField.getText());
                    stmt.setString(2, loginPasswordField.getText());

                    ResultSet rs = stmt.executeQuery();

                    String staffID = "";
                    int roleID = 0;
                    while(rs.next()) {
                        staffID = rs.getString(1);
                        roleID = rs.getInt(2);
                    }

                    if (roleID == 1){
                        redirectPortal(event, "Doctor", staffID);
                    } else if (roleID == 2) {
                        redirectPortal(event, "Nurse", staffID);
                    } else {
                        MessageAlert.incorrectUsernameOrPassword();
                    }
                } catch (Exception ex) {
                    MessageAlert.incorrectUsernameOrPassword();
                }
                break;
            case "Patient":
                sql = "SELECT patient_profile_id FROM Patient_Profile WHERE username = ? AND password = ?";
                try {
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, loginUsernameField.getText());
                    stmt.setString(2, loginPasswordField.getText());

                    ResultSet rs = stmt.executeQuery();

                    String patientID = rs.getString(1);
                    if (patientID == null || patientID.trim().isEmpty())
                        MessageAlert.incorrectUsernameOrPassword();
                    else {
                        redirectPortal(event, "Patient", patientID);
                    }
                } catch (Exception ex) {
                    MessageAlert.incorrectUsernameOrPassword();
                }
                break;
        }
    }
}