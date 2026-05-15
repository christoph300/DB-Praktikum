package com.dataimport.ErrorHandling;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ErrorData {
    public static void logError(Connection con, String entityName, String attributeName, Exception e) {

        String message = e.getMessage();

        if (e instanceof MissingAttributeException) {
            String entity = ((MissingAttributeException) e).getEntity();
            String attribute = ((MissingAttributeException) e).getAttribute();
            insertError(con, entity, attribute, message);
        } else if (e instanceof InvalidAttributeException) {
            String entity = ((InvalidAttributeException) e).getEntity();
            String attribute = ((InvalidAttributeException) e).getAttribute();
            insertError(con, entity, attribute, message);
        } else {
            insertError(con, entityName, attributeName, message);       
        }
    }

    public static void insertError(Connection con, String entity, String attribute, String message) {
        try {
    
            String sql = "INSERT INTO Errordaten (Entityname, ErrorAttribute, Fehlerbeschreibung) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, entity);
            pstmt.setString(2, attribute);
            pstmt.setString(3, message);
            pstmt.executeUpdate();
       
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
