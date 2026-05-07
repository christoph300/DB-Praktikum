
-- Basisklasse Produkt
CREATE TABLE Produkt(
                    PNr VARCHAR(50) PRIMARY KEY,
                    Titel VARCHAR(255),
                    Rating FLOAT,
                    Verkaufsrang INT,
                    Bild TEXT 
)
-- Spezialisierung Buch
CREATE TABLE Buch(
                    PNr VARCHAR(50) PRIMARY KEY,
                        REFERENCES Produkt(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,
                    ISBN VARCHAR(20) NOT NULL,
                    Erscheinungsdatum DATE,
                    Seitenzahl INT
)
-- Autor-Tabelle für die Beziehung zwischen Buch und Autor
CREATE TABLE Autor(
                    PNr VARCHAR(50),
                        REFERENCES Buch(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    Name VARCHAR(255) NOT NULL,
                    PRIMARY KEY (PNr, Name)
)
-- Verlag-Tabelle für die Beziehung zwischen Buch und Verlag
CREATE TABLE Verlag(
                    PNr VARCHAR(50),
                        REFERENCES Buch(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    Verlag VARCHAR(255) NOT NULL,
                    PRIMARY KEY (PNr, Verlag)
)
-- Spezialisierung Musik-CD
CREATE TABLE MusikCD(
                    PNr VARCHAR(50) PRIMARY KEY,
                        REFERENCES Produkt(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,
                    Erscheinungsdatum DATE
)
-- Künstler-Tabelle für die Beziehung zwischen Musik-CD und Künstler
CREATE TABLE Kuenstler(
                    PNr VARCHAR(50),
                        REFERENCES MusikCD(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    Name VARCHAR(255) NOT NULL,
                    PRIMARY KEY (PNr, Name)
)
-- Label-Tabelle für die Beziehung zwischen Musik-CD und Label
CREATE TABLE Label(
                    PNr VARCHAR(50),
                        REFERENCES MusikCD(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    Labelname VARCHAR(255) NOT NULL,
                    PRIMARY KEY (PNr, Labelname)
)
-- Song-Tabelle für die Beziehung zwischen Musik-CD und Song
CREATE TABLE Song(
                    PNr VARCHAR(50),
                        REFERENCES MusikCD(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    Songtitel VARCHAR(255) NOT NULL,
                    PRIMARY KEY (PNr, Songtitel)
)
-- Spezialisierung DVD
CREATE TABLE DVD(
                    PNr VARCHAR(50) PRIMARY KEY,
                        REFERENCES Produkt(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,
                    Format VARCHAR(50),
                    Laufzeit INT,
                    RegionCode VARCHAR(10)
)
-- Actor-Tabelle für die Beziehung zwischen DVD und actor
CREATE TABLE Actor(
                    PNr VARCHAR(50),
                        REFERENCES DVD(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    Name VARCHAR(255) NOT NULL,
                    PRIMARY KEY (PNr, Name)
)
-- Creator-Tabelle für die Beziehung zwischen DVD und Creator
CREATE TABLE Creator(
                    PNr VARCHAR(50),
                        REFERENCES DVD(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    Name VARCHAR(255) NOT NULL,
                    PRIMARY KEY (PNr, Name)
)
-- Director-Tabelle für die Beziehung zwischen DVD und Director
CREATE TABLE Director(
                    PNr VARCHAR(50),
                        REFERENCES DVD(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    Name VARCHAR(255) NOT NULL,
                    PRIMARY KEY (PNr, Name)
)
-- Kategorie-Tabelle für die Hierarchie der Kategorien
CREATE TABLE Kategorie(
                    KatID SERIAL PRIMARY KEY,
                    Oberkategorie VARCHAR(255)
                        REFERENCES Kategorie(KatID)
                        ON DELETE SET NULL
                        ON UPDATE CASCADE,
                    Name VARCHAR(255) NOT NULL
)
-- Beziehungstabelle für die Zuordnung von Produkten zu Kategorien
CREATE TABLE ProduktKategorie(
                    PNr VARCHAR(50) NOT NULL,
                        REFERENCES Produkt(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    KatID INT NOT NULL,
                        REFERENCES Kategorie(KatID)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,
                    PRIMARY KEY (PNr, KatID)
)
-- Tabelle für die Filialen
CREATE TABLE Filiale(
                    FilialID SERIAL PRIMARY KEY,
                    FilialName VARCHAR(255),
                    Strasse VARCHAR(255),
                    Plz VARCHAR(10),

)
-- Tabelle für die Bestände der Produkte in den Filialen
CREATE TABLE Bestand(
                    BestandsID SERIAL PRIMARY KEY,
                    PNr VARCHAR(50) NOT NULL,
                        REFERENCES Produkt(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    FilialID INT NOT NULL,
                        REFERENCES Filiale(FilialID)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,   
                    Preis DECIMAL(10, 2),
                    Zustand VARCHAR(50),
                    Waehrung VARCHAR(10)

)
-- Tabelle für die Kunden
CREATE TABLE Kunde(
                    Username VARCHAR(255) PRIMARY KEY,
)
-- Tabelle für die Käufe der Kunden
CREATE TABLE Kauf(
                    KaufID SERIAL PRIMARY KEY,
                    Username VARCHAR(255) NOT NULL,
                        REFERENCES Kunde(Username)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    PNr VARCHAR(50) NOT NULL,
                        REFERENCES Produkt(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    Kaufzeit TIMESTAMP,
                    FilialID INT,
                        REFERENCES Filiale(FilialID)
                        ON DELETE SET NULL
                        ON UPDATE CASCADE,
                    Menge INT,
                    Preis DECIMAL(10, 2) NOT NULL CHECK (Preis >= 0),
                    Kontonummer VARCHAR(20),
                    Straße VARCHAR(255),
                    PLZ VARCHAR(10),
                    Ort VARCHAR(255)
)
-- Tabelle für Bewertungen der Kunden zu Produkten
CREATE TABLE Bewertung(
                    BewertungID SERIAL PRIMARY KEY,
                    Username VARCHAR(255) NOT NULL,
                        REFERENCES Kunde(Username)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    PNr VARCHAR(50) NOT NULL,
                        REFERENCES Produkt(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    Bewertung FLOAT,
                    Kommentar TEXT,
                    Bewertungsdatum DATE
)
-- Tabelle für ähnliche Produkte
CREATE TABLE produkt_aehnlich(
                    PNr1 VARCHAR(50) NOT NULL,
                        REFERENCES Produkt(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    PNr2 VARCHAR(50) NOT NULL,
                        REFERENCES Produkt(PNr)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,  
                    PRIMARY KEY (PNr1, PNr2)
)

CREATE TABLE ErrorDaten(
                    ErrorID SERIAL PRIMARY KEY,
                    EntityName VARCHAR(255),
                    ErrorAttribute VARCHAR(255),
                    Fehlerbeschreibung TEXT NOT NULL,
                    Fehlerklasse VARCHAR(255)
                     
)

CREATE INDEX IF NOT EXISTS idx_produkt_pnr ON Produkt(PNr);
CREATE INDEX IF NOT EXISTS idx_buch_pnr ON Buch(PNr);
CREATE INDEX IF NOT EXISTS idx_musikcd_pnr ON MusikCD(PNr);
CREATE INDEX IF NOT EXISTS idx_dvd_pnr ON DVD(PNr);
CREATE INDEX IF NOT EXISTS idx_kategorie_katid ON Kategorie(KatID);
CREATE INDEX IF NOT EXISTS idx_filiale_filialid ON Filiale(FilialID);
CREATE INDEX IF NOT EXISTS idx_kunde_username ON Kunde(Username);
CREATE INDEX IF NOT EXISTS idx_kauf_kaufid ON Kauf(KaufID);

