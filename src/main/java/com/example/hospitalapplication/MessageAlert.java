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

    public static void sqlError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Connection Error");
        alert.setContentText("SQL Error");

        alert.showAndWait();
    }
}
