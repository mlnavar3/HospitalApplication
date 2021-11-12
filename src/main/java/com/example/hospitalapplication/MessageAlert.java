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

    public static void sqlError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Connection Error");
        alert.setContentText("SQL Error");

        alert.showAndWait();
    }
}
