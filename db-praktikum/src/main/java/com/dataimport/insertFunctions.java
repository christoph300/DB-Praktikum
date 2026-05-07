package com.dataimport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class insertFunctions {

    public static void insertStore(Connection con, String name, String street, String zip) {
        try {
            String sql = "INSERT INTO Filiale (filialname, strasse, plz) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, street);
            pstmt.setString(3, zip);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            ErrorData.logError(con, "Filiale","UNKNOWN", e);
        }
    }

    public static void insertProdukt(Connection con, String asin, String title, int salesrank, String picture) {
        try {
            String sql = "INSERT INTO Produkt (pnr, titel, verkaufsrang, bild) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, asin);
            pstmt.setString(2, title);
            pstmt.setInt(3, salesrank);
            pstmt.setString(4, picture);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            ErrorData.logError(con, "Produkt","UNKNOWN", e);
        }
    }
}
