package com.example.pharma_gest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection databaseLink;
    public static Connection getConnection() {
        String databaseName = "pharmagest";
        String databaseUser = "postgres";
        String databasePassword = "0000";
        String url = "jdbc:postgresql://localhost:5432/pharmagest";

        try {
            Class.forName("org.postgresql.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            return databaseLink;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }}
