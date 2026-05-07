package com.dataimport;

import java.io.File;
import java.sql.Connection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StoreParser {

    public static void main(Connection con) {
        parseStores(con, "dresden.xml");
        parseStores(con, "leipzig_transformed.xml");
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
            e.printStackTrace();
        }
    }

    public static void parseItems(Connection con, String filePath) throws InvalidAttributeException {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList itemList = doc.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
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
                        throw new InvalidAttributeException("Produkt", "asin", "ASIN darf nicht leer sein");
                    }
                    if (pgroup == null || pgroup.isEmpty()) {
                        throw new InvalidAttributeException("Produkt", "pgroup", "Produktgruppe darf nicht leer sein");
                    }
                    if (title == null || title.isEmpty()) {
                        throw new InvalidAttributeException("Produkt", "title", "Titel darf nicht leer sein");
                    }

                    // Salesrank validieren (optional)
                    Integer salesrank = null;
                    if (salesrankStr != null && !salesrankStr.isEmpty()) {
                        try {
                            salesrank = Integer.parseInt(salesrankStr);
                            if (salesrank < 0) {
                                logError("Produkt", "salesrank", asin, "Salesrank muss positiv sein");
                                salesrank = null;
                            }
                        } catch (NumberFormatException e) {
                            logError("Produkt", "salesrank", asin, "Salesrank ist keine gültige Zahl");
                        }
                    }

                    //Bild wird je nach Filiale anders gespeichert, Dresden über "Details", Leipzig über "Picture"
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
            }
        } catch (InvalidAttributeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parseBook(Connection con, Element item, String asin) throws InvalidAttributeException {
        // TODO
    }

    public static void parseMusic(Connection con, Element item, String asin) throws InvalidAttributeException {
        // TODO
    }

    public static void parseDVD(Connection con, Element item, String asin) throws InvalidAttributeException {
        // TODO
    }
}