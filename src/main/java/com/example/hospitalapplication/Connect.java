package com.example.hospitalapplication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    // SQLite
    public static Connection connect() {
        // replace "url" variable with your own database path
        String url = "jdbc:sqlite:/Users/mercedesnavarro/Desktop/sqlite/hospital.db";
        Connection conn = null;
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /*public static void main(String[] args) {
        connect();
    }*/
}
