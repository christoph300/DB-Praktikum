package com.dataimport.ErrorHandling;


// Fehlende Attribut-Exception, um fehlende Pflichtattribute zu kennzeichnen

public class MissingAttributeException extends Exception {
    private String entity;
    private String attribute;

    public MissingAttributeException(String entity, String attribute) {
        this.entity = entity;
        this.attribute = attribute;
    }

    @Override
    public String getMessage() {
        return "Fehlendes Attribut: " + attribute + " in Entity: " + entity;
    }

    public String getEntity() {
        return entity;
    }

    public String getAttribute() {
        return attribute;
    }
     
}
