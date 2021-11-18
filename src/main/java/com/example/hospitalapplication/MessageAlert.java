package com.example.hospitalapplication;

import javafx.scene.control.Alert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageAlert {

    public static void incorrectUsernameOrPassword(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid");
        alert.setContentText("Invalid username/password");

        alert.showAndWait();
    }

    public static void registrationInputError(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid");
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }

    public static void registrationSuccessfulBox(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.setContentText("User has been added successfully!");

        alert.showAndWait();
    }

    public static void sendMessageErrorBox(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }

    public static void sendMessageSuccessfulBox(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.setContentText("Message has been sent!");

        alert.showAndWait();
    }

    public static void viewMsgError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("View Message Error");
        alert.setContentText("Select a message to view");

        alert.showAndWait();
    }

    public static void prescriptionErrorBox(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }

    public static void prescriptionSuccessfulBox() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.setContentText("Prescription request has been sent!");

        alert.showAndWait();
    }

    public static void medicalHistoryErrorBox(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }

    public static void medicalHistorySuccessfulBox() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.setContentText("Prescription request has been sent!");

        alert.showAndWait();
    }

    public static void diagnosisErrorBox() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText("Appointment is not ready for diagnosis!");

        alert.showAndWait();
    }

    public static void diagnosisCompleteErrorBox() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText("Appointment has already been completed!");

        alert.showAndWait();
    }

    public static void appointmentSuccessfulBox() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.setContentText("Appointment is complete!");

        alert.showAndWait();
    }

    public static void summaryErrorBox() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText("Summary cannot be empty, if there is nothing to say type N/A");

        alert.showAndWait();
    }

    public static void sqlError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Connection Error");
        alert.setContentText("SQL Error");

        alert.showAndWait();
    }

    public static void requestPrescription() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Sent");
        alert.setHeaderText("Success");
        alert.setContentText("Request sent to pharmacy.");

        alert.showAndWait();
    }

    public static void appointmentCreated() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Created");
        alert.setHeaderText("Success");
        alert.setContentText("The appointment has been created.");

        alert.showAndWait();
    }

    public static void appointmentCreatedFail() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Problem");
        alert.setContentText("Please fill in the complaint text.");

        alert.showAndWait();
    }

    public static void nullDate() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Problem");
        alert.setContentText("Please pick a date.");

        alert.showAndWait();
    }

}
