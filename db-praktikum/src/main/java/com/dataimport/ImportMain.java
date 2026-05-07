package com.dataimport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ImportMain 
{
    public static void main(String[] args ){
        try {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "admin";
            String password = "passwort";

            Connection con = DriverManager.getConnection(url, user, password);
            if  (con != null) {
                System.out.println("Connected to the database!");
            } else {
                System.err.println("Failed to establish connection to the database!");
            }

            try {
                initializeDatabase(con, "CreateTables.sql");
            } catch (Exception e) {
                System.err.println("Error initializing database: " + e.getMessage());
                e.printStackTrace();
            }
            StoreParser.main(con);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initializeDatabase(Connection con, String sqlFile) throws Exception {
        String sql = new StringBuilder();


        // Lesen der SQL-Datei
        try (BufferedReader br = new BufferedReader(new FileReader(sqlFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new Exception("Error reading SQL file: " + e.getMessage(), e);
        }

        // Ausführen der SQL-Befehle
        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql.toString());
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            throw new SQLException("Error executing SQL: " + e.getMessage(), e);
        }
    }
}
