package com.dataimport.Parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dataimport.ErrorHandling.ErrorData;
import com.dataimport.ErrorHandling.InvalidAttributeException;

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

    public static void insertProdukt(Connection con, String asin, String title, Integer salesrank, String picture) {
        //TODO Check if ASIN already exists in database 
        
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

    public static void insertBook(Connection con, String asin, String isbn, Integer pages, String publishDate, String edition, NodeList verlaege, NodeList autoren) throws Exception {
    String BookSql = "INSERT INTO Buch (pnr, isbn, seitenzahl, erscheinungsdatum, auflage) VALUES (?, ?, ?, ?, ?)";

    // Datum validieren
    LocalDate date = null;
    if (publishDate != null && !publishDate.isEmpty()) {
        date = LocalDate.parse(publishDate);
        if (date.isAfter(LocalDate.now()) || date.isBefore(LocalDate.of(1800, 1, 1))) {
            throw new InvalidAttributeException("Buch", "Erscheinungsdatum", publishDate);
        }
    }

    PreparedStatement pstmt = con.prepareStatement(BookSql);
    pstmt.setString(1, asin);
    pstmt.setString(2, isbn);
    if (pages > 0) {
        pstmt.setInt(3, pages);
    } else {
        pstmt.setNull(3, java.sql.Types.INTEGER);
    }
    if (date != null) {
        pstmt.setDate(4, java.sql.Date.valueOf(date));
    } else {
        pstmt.setNull(4, java.sql.Types.DATE);
    }
    pstmt.setString(5, edition);
    pstmt.executeUpdate();

    // Verlage einfügen (optional)
    if (verlaege != null && verlaege.getLength() > 0) {
        for (int i = 0; i < verlaege.getLength(); i++) {
            Node verlagNode = verlaege.item(i);
            if (verlagNode.getNodeType() == Node.ELEMENT_NODE) {
                Element verlagElement = (Element) verlagNode;
                String verlagName = verlagElement.getTextContent(); //Dresden
                
                if (verlagName == null || verlagName.isEmpty()) { 
                    verlagName = verlagElement.getAttribute("name"); //Leipzig
                }
                if (verlagName != null && !verlagName.isEmpty()) {
                    insertVerlagBuch(con, asin, verlagName);
                }
            }
        }
    }

    // Autoren einfügen (Pflicht - bereits im Parser geprüft)
    for (int i = 0; i < autoren.getLength(); i++) {
        Node autorNode = autoren.item(i);
        if (autorNode.getNodeType() == Node.ELEMENT_NODE) {
            Element autorElement = (Element) autorNode;
            String autorName = autorElement.getTextContent();
            if (autorName == null || autorName.isEmpty()) {
                autorName = autorElement.getAttribute("name");
            }
            if (autorName != null && !autorName.isEmpty()) {
                insertAutorBuch(con, asin, autorName);
            }
        }
    }
}

            public static void insertVerlagBuch(Connection con, String asin, String verlagName) throws Exception {
                try {
                    String sql = "INSERT INTO Verlag (pnr, verlag) VALUES (?, ?)";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, asin);
                    pstmt.setString(2, verlagName);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    throw e;
                }
            }

            public static void insertAutorBuch(Connection con, String asin, String autorName) throws Exception {
                try {
                    String sql = "INSERT INTO Autor (pnr, name) VALUES (?, ?)";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, asin);
                    pstmt.setString(2, autorName);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    throw e;
                }
            }

            public static void insertMusic(Connection con, String asin, String publishDate, NodeList kuenstler, NodeList label, NodeList song) throws Exception {
                String MusicSql = "INSERT INTO MusikCD (pnr, Erscheinungsdatum) VALUES (?, ?)";

                    LocalDate date = null;
                    if (publishDate != null && !publishDate.isEmpty()) {
                         date = LocalDate.parse(publishDate); // Annahme, das es im richtigen Format ist, sonst würde eine Exception geworfen werden
                        if (date.isAfter(LocalDate.now()) || date.isBefore(LocalDate.of(1800, 1, 1))) { // Das Jahr 1800 wird hier als realistisches Minimum für die Veröffentlichung eines Buches angenommen
                            throw new InvalidAttributeException("MusikCD", "Erscheinungsdatum", publishDate);
                        }  
                    }

                    PreparedStatement pstmt = con.prepareStatement(MusicSql);
                    pstmt.setString(1, asin);
                    if (date != null) {
                        pstmt.setDate(2, java.sql.Date.valueOf(date));
                    } else {
                        pstmt.setNull(2, java.sql.Types.DATE);
                    }
                    pstmt.executeUpdate();

                    // Künstler einfügen Pflicht, bereits im Parser geprüft
                        for (int i = 0; i < kuenstler.getLength(); i++) {
                                Node kuenstlerNode = kuenstler.item(i); 
                                if (kuenstlerNode.getNodeType() == Node.ELEMENT_NODE) { // Sicherstellen, dass es sich um ein Element handelt
                                    Element kuenstlerElement = (Element) kuenstlerNode; //cast to Element
                                
                                    String kuenstlerName = kuenstlerElement.getTextContent(); //Dresden
                                    if (kuenstlerName == null || kuenstlerName.isEmpty()) {
                                        kuenstlerName = kuenstlerElement.getAttribute("name"); //Leipzig
                                    }
                                    if (kuenstlerName != null && !kuenstlerName.isEmpty()) {
                                        insertFunctions.insertKuenstlerMusic(con, asin, kuenstlerName);
                                    }
                                }  
                        }

                    // Label einfügen optional
                        for (int i = 0; i < label.getLength(); i++) {
                                Node labelNode = label.item(i);
                                if (labelNode.getNodeType() == Node.ELEMENT_NODE) { // Sicherstellen, dass es sich um ein Element handelt
                                    Element labelElement = (Element) labelNode; //cast to Element
                                    String labelName = labelElement.getTextContent(); //Dresden
                                    if (labelName == null || labelName.isEmpty()) {
                                        labelName = labelElement.getAttribute("name"); //Leipzig
                                    }
                                    if (labelName != null && !labelName.isEmpty()) {
                                        insertFunctions.insertLabelMusic(con, asin, labelName);
                                    }
                                }
                        }

                    // Songs einfügen optional
                        for (int i = 0; i < song.getLength(); i++) {
                                Node songNode = song.item(i);
                                if (songNode.getNodeType() == Node.ELEMENT_NODE) { // Sicherstellen, dass es sich um ein Element handelt
                                    Element songElement = (Element) songNode; //cast to Element
                                    String songName = songElement.getTextContent(); //Dresden
                                    if (songName == null || songName.isEmpty()) {
                                        songName = songElement.getAttribute("name"); //Leipzig
                                    }
                                    if (songName != null && !songName.isEmpty()) {
                                        insertFunctions.insertSongMusic(con, asin, songName);
                                    }
                                }
                        }
            }
        
            public static void insertKuenstlerMusic(Connection con, String asin, String kuenstlerName) throws Exception {
                try {
                    String sql = "INSERT INTO Kuenstler (pnr, name) VALUES (?, ?)";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, asin);
                    pstmt.setString(2, kuenstlerName);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    throw e;
                }
            }

            public static void insertLabelMusic(Connection con, String asin, String labelName) throws Exception {
                try {
                    String sql = "INSERT INTO Label (pnr, labelname) VALUES (?, ?)";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, asin);
                    pstmt.setString(2, labelName);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    throw e;
                }
            }

            public static void insertSongMusic(Connection con, String asin, String songName) throws Exception {
                try {
                    String sql = "INSERT INTO Song (pnr, songtitel) VALUES (?, ?)";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, asin);
                    pstmt.setString(2, songName);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    throw e;
                }
            }

            public static void insertDVD(Connection con, String asin, String format, Integer runtime, Integer regioncode, NodeList creators, NodeList actors, NodeList directors) throws Exception {
                    String DvdSql = "INSERT INTO DVD (pnr, Format, laufzeit, RegionCode) VALUES (?, ?, ?, ?)";  
                    PreparedStatement pstmt = con.prepareStatement(DvdSql);
                    pstmt.setString(1, asin);
                    pstmt.setString(2, format);
                    // Optionales Einfügen von Laufzeit und RegionCode, falls vorhanden
                    if (runtime != null) {
                        pstmt.setInt(3, runtime);
                    } else {
                        pstmt.setNull(3, java.sql.Types.INTEGER);
                    }
                    if (regioncode != null) {
                        pstmt.setInt(4, regioncode);
                    } 
                    else {
                        pstmt.setNull(4, java.sql.Types.INTEGER);
                    }
                    
                    pstmt.executeUpdate();
                
                    // Creators einfügen
                    if (creators != null && creators.getLength() > 0) {
                        for (int i = 0; i < creators.getLength(); i++) {
                                Node creatorNode = creators.item(i); 
                                if (creatorNode.getNodeType() == Node.ELEMENT_NODE) { // Sicherstellen, dass es sich um ein Element handelt
                                    Element creatorElement = (Element) creatorNode; //cast to Element
                                
                                    String creatorName = creatorElement.getTextContent(); //Dresden
                                    if (creatorName == null || creatorName.isEmpty()) {
                                        creatorName = creatorElement.getAttribute("name"); //Leipzig
                                    }
                                    if (creatorName != null && !creatorName.isEmpty()) {
                                        insertFunctions.insertCreatorDVD(con, asin, creatorName);
                                    }
                                }
                        }
                    }

                    // Actors einfügen
                    if (actors != null && actors.getLength() > 0) {
                        for (int i = 0; i < actors.getLength(); i++) {
                                Node actorNode = actors.item(i);
                                if (actorNode.getNodeType() == Node.ELEMENT_NODE) { // Sicherstellen, dass es sich um ein Element handelt
                                    Element actorElement = (Element) actorNode; //cast to Element
                                    String actorName = actorElement.getTextContent(); //Dresden
                                    if (actorName == null || actorName.isEmpty()) {
                                        actorName = actorElement.getAttribute("name"); //Leipzig
                                    }
                                    if (actorName != null && !actorName.isEmpty()) {
                                        insertFunctions.insertActorDVD(con, asin, actorName);
                                    }
                                }
                        }
                    }

                    // Directors einfügen
                    if (directors != null && directors.getLength() > 0) {
                        for (int i = 0; i < directors.getLength(); i++) {
                                Node directorNode = directors.item(i);
                                if (directorNode.getNodeType() == Node.ELEMENT_NODE) { // Sicherstellen, dass es sich um ein Element handelt
                                    Element directorElement = (Element) directorNode; //cast to Element
                                    String directorName = directorElement.getTextContent(); //Dresden
                                    if (directorName == null || directorName.isEmpty()) {
                                        directorName = directorElement.getAttribute("name"); //Leipzig
                                    }
                                    if (directorName != null && !directorName.isEmpty()) {
                                        insertFunctions.insertDirectorDVD(con, asin, directorName);
                                    }
                                }
                        }
                    }
            }

            public static void insertActorDVD(Connection con, String asin, String actorName) throws Exception {
                try {
                    String sql = "INSERT INTO Actor (pnr, name) VALUES (?, ?)";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, asin);
                    pstmt.setString(2, actorName);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    throw e;
                }
            }

            public static void insertDirectorDVD(Connection con, String asin, String directorName) throws Exception {
                try {
                    String sql = "INSERT INTO Director (pnr, name) VALUES (?, ?)";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, asin);
                    pstmt.setString(2, directorName);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    throw e;
                }
            }

            public static void insertCreatorDVD(Connection con, String asin, String creatorName) throws Exception {
                try {
                    String sql = "INSERT INTO Creator (pnr, name) VALUES (?, ?)";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, asin);
                    pstmt.setString(2, creatorName);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    throw e;
                }
            }

            public static void insertSimilar(Connection con, String asin, String similarAsin) throws Exception {
                try {
                    String sql = "INSERT INTO produkt_aehnlich (pnr1, pnr2) VALUES (?, ?)";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, asin);
                    pstmt.setString(2, similarAsin);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    throw e;
                }
            }
} 
        
