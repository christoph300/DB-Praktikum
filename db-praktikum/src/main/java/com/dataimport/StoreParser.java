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

    public static void main(String[] args) {

    }

    public static void parseStores(Connection con,String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element shop = doc.getElementsByTagName("shop").item(0);
            
            String name = shop.getAttribute("name").item(0).getTextContent();
            String street = shop.getAttribute("street").item(0).getTextContent();
            String zip = shop.getAttribute("zip").item(0).getTextContent();
            insertFunctions.insertStore(con, name, street, zip);

        } catch (Exception e) {
            e.printStackTrace();
        }

    public static void parseItems(Connection con, String filePath) throws AttributeUndefinedException {
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
                String asin = item.getAttribute("asin");
                String pgroup = item.getAttribute("pgroup");
                String salesrankStr = item.getAttribute("salesrank");
                String picture = item.getAttribute("picture");

                // Titel auslesen
                Node titleNode = item.getElementsByTagName("title").item(0);
                String title = (titleNode != null) ? titleNode.getTextContent() : null;

                // Validierung Pflichtfelder
                if (asin == null || asin.isEmpty()) {
                    Throw new AttributeUndefinedException("Produkt", "asin", "ASIN darf nicht leer sein");
                    continue;
                }
                if (pgroup == null || pgroup.isEmpty()) {
                    Throw new AttributeUndefinedException("Produkt", "pgroup", "Produktgruppe darf nicht leer sein");
                    continue;
                }
                if (title == null || title.isEmpty()) {
                    Throw new AttributeUndefinedException("Produkt", "title", "Titel darf nicht leer sein");
                    continue;
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
                String picture = item.getAttribute("picture");

// optional, kein Pflichtfeld
                if (picture == null || picture.isEmpty()) {
                picture = null;
                    }
                }

                // Produkt in DB speichern
                insertFunctions.insertProdukt(con, asin, title, salesrank, picture);

                // Je nach pgroup Spezialisierung einlesen
                switch (pgroup) {
                    case "Book"  -> parseBook(con, item, asin);
                    case "Music" -> parseMusic(con, item, asin);
                    case "DVD"   -> parseDVD(con, item, asin);
                    default -> logError("Produkt", "pgroup", asin, "Unbekannte Produktgruppe: " + pgroup);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

}
    public static void parseBook(Connection con, Element item, String asin) throws AttributeUndefinedException {
        Element bookspec = (Element) item.getElementsByTagName("bookspec").item(0);}
}