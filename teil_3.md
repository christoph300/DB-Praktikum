# Aufgabenstellung
Diese abschließende Teilaufgabe des Datenbank-Praktikums umfasst die Erstellung einer Konsolenanwendung oder GUI-Applikation. Ihre Aufgabe ist die Implementierung dieser Schnittstelle unter Verwendung von Hibernate, um der Applikation den Zugriff auf die Datenbank zu ermöglichen. Ziel ist es, Ihnen die Technik von 3-Ebenen- Anwendungen (Frontend, Mittelschicht, Backend/Datenbank) näher zu bringen, weitere Möglichkeiten des datenbankunabhängigen Zugriffs mittels erweiterte SQL-Konzepte (z.B. Objekt-Relationales-Mapping mittels Hibernate) anzuwenden.

## Die Applikation

Die Anwendung selbst hat keinen direkten Zugriff auf die Datenbank, sondern lädt zur Laufzeit ein datenbankspezifisches Modul, welches eine vorgegebene Schnittstelle (Interface) implementiert und als Mittelschicht den Zugriff auf die Datenbank realisiert. Die Aufgabe des Frontends, entweder ein Konsolenmenü oder eine GUI soll die Eingabe von Parametern ermöglichen und die ensprechenden Aufrufe der Methoden der Mittelschicht realisieren, deren Ergebnisse entsprechend dargestellt werden (ASCII oder grafisch). 

## Implementierungsrichtlinien

Es sollen keine Parameter im Quellcode fest vorgegeben werden. Hierzu gehören auch der Klassenname des JDBC-Treibers, die JDBC-URL, Logininformationen usw. Hierfür sollte eine property Datei bereitgestellt werden, die beim Start der Applikation eingelesen wird. Dies ist eine wesentliche Voraussetzung, um ein datenbankunabhängiges Programm zu erstellen. Ein Datenbankwechsel kann dann im günstigsten Fall einfach durch Ändern der Parameter in der Property-Datei vollzogen werden. 


## Testat

Zum Fertigstellungstermin soll die Applikation voll funktionsfähig gemäß Aufgabenstellung nutzbar sein. Hierzu gehört auch das zu erstellende Hibernate-Mapping. Zum Testat ist das Hibernate-Mapping sowie die Applikation mit den zu implementierenden Methoden vorzustellen.


## Anforderung

Die Applikation soll folgende Methoden umfassen. Definieren Sie hierfür vorab ein Interface welches alle Methoden umfasst. Für die Bestimmung der Ergebnisse soll **kein SQL** verwendet werden, sondern lediglich Hibernate inklusive der Hibernate Query Language.


**Methoden der Schnittstelle**


    init

Hier sollte die Datenbankverbindung für die anderen Methodenaufrufe erstellt, sowie weitere Aktionen, die zur Initialisierung notwendig sind, ausgeführt werden. Alle notwendigen Parameter sollen aus dem übergebenen Property-Objekt entnommen werden.
    
    finish
Damit die Mittelschicht alle Ressourcen kontrolliert wieder freigeben kann, wird diese Methode bei Beendigung der Anwendung aufgerufen. Hier sollten speziell die Datenbankobjekte wieder freigegeben werden.

    getProduct
Für eine bestimmte Produkt-Id werden mit dieser Methode die Detailinformationen des Produkts ermittelt.

    getProducts(String pattern)
Diese Methode soll eine Liste der in der Datenbank enthaltenen Produkte, deren Titel mit dem übergebenen Pattern übereinstimmen, zurückliefern. Beachten Sie, dass im Falle von pattern=null die komplette Liste zurückgeliefert wird. Das Pattern kann SQL-Wildcards enthalten.

Hinweis: der Patternvergleich kann mittels des SQL-Operators like durchgeführt werden.
    
    getCategoryTree
Diese Methode ermittelt den kompletten Kategorienbaum durch Rückgabe des Wurzelknotens. Jeder Knoten ist dabei vom Typ Category und kann eine Liste von Unterknoten (d.h. Unterkategorien) enthalten.

    getProductsByCategoryPath
Nach Angabe einer Kategorie (definiert durch den Pfad von der Wurzel zu sich selbst) soll die Liste der zugeordneten Produkte ermittelt werden. Die Angabe des Pfades ist notwendig, da der Kategorienname allein nicht eindeutig ist.

    getTopProducts
Diese Methode liefert eine Liste aller Produkte zurück, die unter den Top k sind basierend auf dem Rating.

    getSimilarCheaperProduct
Diese Methode liefert für ein Produkt(Id) eine List von Produkten, die ähnlich und billiger sind als das spezifizierte.     

    addNewReview
Die Rahmenapplikation erlaubt sowohl das Ansehen als auch Hinzufügen von Reviews. MIt Hilfe der Methode wird ein neues Review in der Datenbank gespeichert.

    getTrolls
Die Methode soll eine Liste von Nutzern ausgeben, deren Durchschnittsbewertung unter einem spezifizierten Rating ist.
    
    getOffers
Für das übergegebene Produkt(Id) werden alle verfügbaren Angebote zurückgeliefert.


