package com.dataimport;

// Ungültige Attribut-Exception, um ungültige Werte zu kennzeichnen

public class InvalidAttributeException extends Exception {
    private String entity;
    private String attributeName;
    private String attributeValue;

    public InvalidAttributeException(String entity, String attributeName, String attributeValue) {
        this.entity = entity;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }
    @Override
    public String getMessage() {
        return "Ungültiger Wert: " + attributeValue + " für Attribut: " + attributeName + " in Entity: " + entity;
    }

    public String getEntity() {
        return entity;
    }

    public String getAttribute() {
        return attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

}
