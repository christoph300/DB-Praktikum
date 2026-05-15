package com.dataimport.Parser;

import java.io.File;
import java.sql.Connection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.dataimport.ErrorHandling.MissingAttributeException;
import com.dataimport.ErrorHandling.InvalidAttributeException;
import com.dataimport.ErrorHandling.ErrorData;


public class StoreParser {

    public static void main(Connection con) {
        parseStores(con, "DB-Praktikum/data/dresden.xml");
        parseStores(con, "DB-Praktikum/data/leipzig_transformed.xml");
        parseItems(con, "DB-Praktikum/data/dresden.xml");
        parseItems(con, "DB-Praktikum/data/leipzig_transformed.xml");
        parseSimiliars(con, "DB-Praktikum/data/dresden.xml");
        parseSimiliars(con, "DB-Praktikum/data/leipzig_transformed.xml");
    }
    public static void parseStores(Connection con, String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element shop = (Element) doc.getElementsByTagName("shop").item(0);

            String name   = shop.getAttribute("name");
            String street = shop.getAttribute("street");
            String zip    = shop.getAttribute("zip");
            insertFunctions.insertStore(con, name, street, zip);

        } catch (Exception e) {
            ErrorData.logError(con, "Filiale", "UNKNOWN", e);
        }
    }

    public static void parseItems(Connection con, String filePath) {
    //TODO SANITY CHECKS in 

        try {
        File xmlFile = new File(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList itemList = doc.getElementsByTagName("item");
        for (int i = 0; i < itemList.getLength(); i++) {
            try {
                Node node = itemList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element item = (Element) node;

                    // FILTER: nur Items mit Kindelementen
                    boolean hasChild = false;
                    NodeList children = item.getChildNodes();
                    for (int j = 0; j < children.getLength(); j++) {
                        if (children.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            hasChild = true;
                            break;
                        }
                    }
                    if (!hasChild) continue;

                    // Attribute auslesen
                    String asin         = item.getAttribute("asin");
                    String pgroup       = item.getAttribute("pgroup");
                    String salesrankStr = item.getAttribute("salesrank");

                    // Titel auslesen
                    Node titleNode = item.getElementsByTagName("title").item(0);
                    String title = (titleNode != null) ? titleNode.getTextContent() : null;

                    // Validierung Pflichtfelder
                    if (asin == null || asin.isEmpty()) {
                        throw new MissingAttributeException("Produkt", "asin");
                    }
                    if (pgroup == null || pgroup.isEmpty()) {
                        throw new MissingAttributeException("Produkt", "pgroup");
                    }
                    if (title == null || title.isEmpty()) {
                        throw new MissingAttributeException("Produkt", "title");
                    }

                    int salesrank = 0;
                    if (salesrankStr != null && !salesrankStr.isEmpty()) {
                        salesrank = Integer.parseInt(salesrankStr);
                    }

                    // Bild wird je nach Filiale anders gespeichert
                    String picture;
                    if (item.getElementsByTagName("details").getLength() > 0) {
                        picture = item.getElementsByTagName("details").item(0)
                                      .getAttributes().getNamedItem("img").getNodeValue();
                    } else {
                        picture = item.getAttribute("picture");
                    }

                    // Produkt in DB speichern
                    insertFunctions.insertProdukt(con, asin, title, salesrank, picture);

                    // Je nach pgroup Spezialisierung einlesen
                    switch (pgroup) {
                        case "Book"  -> parseBook(con, item, asin);
                        case "Music" -> parseMusic(con, item, asin);
                        case "DVD"   -> parseDVD(con, item, asin);
                        default      -> throw new InvalidAttributeException("Produkt", "pgroup", pgroup);
                    }
                }
            } catch (MissingAttributeException e) {
                ErrorData.logError(con, "Produkt", e.getAttribute(), e);
            } catch (InvalidAttributeException e) {
                ErrorData.logError(con, "Produkt", e.getAttribute(), e);
            } catch (Exception e) {
                ErrorData.logError(con, "Produkt", "UNKNOWN", e);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public static void parseBook(Connection con, Element item, String asin) throws Exception {
        try {

        NodeList bookspecAttribute = item.getElementsByTagName("bookspec");
        Element bookspec = (Element) bookspecAttribute.item(0);
        //Extract ISBN Pflichtfeld
        String isbn = getAttr(bookspec, "isbn", "val");
        if (isbn == null || isbn.isEmpty()) {
            throw new MissingAttributeException("Buch", "isbn");
        }
        //Extract optionals
        String pagesStr = getTextContent(bookspec, "pages");
        int pages = 0;
        if (pagesStr != null && !pagesStr.isEmpty()) {
            pages = Integer.parseInt(pagesStr);
        }
        //Extract publishDate
        String publishDate = getAttr(bookspec, "publication", "date");

        //Extract edition
        String edition = getAttr(bookspec, "edition", "val");

        NodeList autoren = item.getElementsByTagName("author");

        NodeList verlaege = item.getElementsByTagName("publisher");

        // Autoren Pflichtfeld validieren
        if (autoren == null || autoren.getLength() == 0) {
            throw new MissingAttributeException("Buch", "author");
        }
        
        insertFunctions.insertBook(con, asin, isbn, pages, publishDate, edition, verlaege, autoren);

        } catch (MissingAttributeException e) {
            ErrorData.logError(con, "Buch", e.getAttribute(), e);
        } 
        catch (InvalidAttributeException e) {
            ErrorData.logError(con, "Buch", e.getAttribute(), e);
        }
        catch (Exception e) {
            ErrorData.logError(con, "Buch", "UNKNOWN", e);
        }
    }

    public static void parseMusic(Connection con, Element item, String asin) throws Exception {
        try {
        NodeList musicspecAttribute = item.getElementsByTagName("musicspec");
        Element musicspec = (Element) musicspecAttribute.item(0);

        String publishDate = getTextContent(musicspec, "releasedate");

        NodeList kuenstler;
        
        if (item.getElementsByTagName("artist").getLength() > 0) {
             kuenstler = item.getElementsByTagName("artist"); 
        } 
        else {
             kuenstler = item.getElementsByTagName("creator"); // falls Artist nicht vorhanden, dann Creator in 
        }
        // Künstler Pflichtfeld validieren
        if (kuenstler == null || kuenstler.getLength() == 0) {
            throw new MissingAttributeException("MusikCD", "artist/creator");
        }

        NodeList label = item.getElementsByTagName("label");

        NodeList song = item.getElementsByTagName("tracks");

        insertFunctions.insertMusic(con, asin, publishDate, kuenstler, label, song);

        } catch (MissingAttributeException e) {
            ErrorData.logError(con, "MusikCD", e.getAttribute(), e);
        } 
        catch (InvalidAttributeException e) {
            ErrorData.logError(con, "MusikCD", e.getAttribute(), e);
        }
        catch (Exception e) {
            ErrorData.logError(con, "MusikCD", "UNKNOWN", e);
            throw e;
        }
    }

    public static void parseDVD(Connection con, Element item, String asin) throws Exception {
        try {
        NodeList dvdspecAttribute = item.getElementsByTagName("dvdspec");
        Element dvdspec = (Element) dvdspecAttribute.item(0);

        String format = getTextContent(dvdspec, "format");
        Integer runtime = null;
        String runtimeStr = getTextContent(dvdspec, "runningtime");
        if (runtimeStr != null && !runtimeStr.isEmpty()) {
            runtime = Integer.parseInt(runtimeStr);
        }
        Integer regioncode = null;
        String regioncodeStr = getTextContent(dvdspec, "regioncode");
        if (regioncodeStr != null && !regioncodeStr.isEmpty()) {
            regioncode = Integer.parseInt(regioncodeStr);
        }

        NodeList creatos = item.getElementsByTagName("creator");

        NodeList actors = item.getElementsByTagName("actors");

        NodeList directors = item.getElementsByTagName("director");


        insertFunctions.insertDVD(con, asin, format, runtime, regioncode, creatos, actors, directors);

        } catch (MissingAttributeException e) {
            ErrorData.logError(con, "DVD", e.getAttribute(), e);
        } 
        catch (InvalidAttributeException e) {
            ErrorData.logError(con, "DVD", e.getAttribute(), e);
        }
        catch (Exception e) {
            ErrorData.logError(con, "DVD", "UNKNOWN", e);
        }
        
    }

    public static void parseSimiliars(Connection con, String filepath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(filepath);
            doc.getDocumentElement().normalize();
            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Node node = items.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element item = (Element) node;

                    // ASIN des Hauptprodukts
                    String asin1 = item.getAttribute("asin");
                    if (asin1 == null || asin1.isEmpty()) continue;

                    //Check für Dresden, hier gespeichert unter Similars
                    NodeList similars = item.getElementsByTagName("similars");
                    if (similars.getLength() == 0) continue;

                    Element similarsElement = (Element) similars.item(0);

                    
                    NodeList similarItems = similarsElement.getElementsByTagName("item");
                    for (int j = 0; j < similarItems.getLength(); j++) {
                        Element similar = (Element) similarItems.item(j);
                        String asin2 = similar.getAttribute("asin");
                        if (asin2 == null || asin2.isEmpty()) continue;
                        insertFunctions.insertSimilar(con, asin1, asin2);
                    }
                    //TODO Check für Leipzig
                    NodeList similarItemsLeipzig = similarsElement.getElementsByTagName("sim_product");
                    for (int j = 0; j < similarItemsLeipzig.getLength(); j++) {
                        Element similar = (Element) similarItemsLeipzig.item(j);
                        String asin2 = getTextContent(similar, "asin");
                        if (asin2 == null || asin2.isEmpty()) continue;
                        insertFunctions.insertSimilar(con, asin1, asin2);

                }
                }
            }
        System.out.println("Similar products parsed successfully.");
        } catch (Exception e) {
            ErrorData.logError(con, "Similars", "UNKNOWN", e);
        }
    }


    // Utility function to get text content from a nested element
      private static String getTextContent(Element parent, String tag) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    // Utility function to get an attribute from a nested element
    private static String getAttr(Element parent, String tag, String attr) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() > 0) {
            Element el = (Element) nodes.item(0);
            return el.getAttribute(attr);
        }
        return null;
    }

    


    

}