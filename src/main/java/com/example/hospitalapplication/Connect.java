package com.example.hospitalapplication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    // SQLite
    public static Connection connect() {
        // replace "url" variable with your own database path
        String url = "jdbc:sqlite:/Users/Tommy/Documents/asu/CSE360/database/hospital.db";
        //String url = "jdbc:sqlite:/Users/samqu/Documents/Databases/hospital project/hospital.db";
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
}
