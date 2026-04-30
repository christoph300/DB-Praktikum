# Aufgabenstellung

In diesem Teil wird der erste Schritt der Erstellung einer vollständigen DB-Anwendung durchgeführt, der Datenbank-Entwurf. Dazu ist zunächst ein Relationenmodell zu erstellen. Ausgehend davon kann die Datenbank erstellt werden und die Daten importiert werden.

Hinweis: In der Aufgabenstellung sind die Informationen, die die zu modellierende Miniwelt beschreiben, bewußt nicht so präzise gewählt, daß Sie sie direkt (Satz für Satz) zum Entwurf benutzen könnten, sondern eher so unstrukturiert und informell, wie sie von Nicht-Fachleuten an einen DB-Administrator herangetragen werden.

## Szenario
[Media Store Szenario](szenario.md) 

## Aufgabe 1a

Entwerfen Sie ein Relationenmodell, dessen Modellierung die inhaltlichen Anforderungen aus der Miniweltbeschreibung erfüllt und dabei alle Angaben in ein gemeinsames Schema integriert. Benennen Sie die notwendigen Relationen, Attribute, Primärschlüssel als auch Fremdschlüssel. Versuchen Sie möglichst, Redundanzen in der Informationsdarstellung zu vermeiden.

Überlegen Sie, welche zusätzliche Integritätsbedingungen für ein semantisch fehlerloses Laden und Verändern/Löschen/Einfügen von Daten in die Datenbank sinnvoll sind. 

## Aufgabe 1b
Schema laden: Erstellen von SQL-Anweisungen (DDL) zum Laden des Datenbankschemas. Erstellen Sie für Attribute, über die in vielen Anfragen selektiert oder ein JOIN ausgeführt wird mittels CREATE INDEX einen Index um Anfragen zu beschleunigen (falls Attribut nicht im Primärschlüssel).

Daten laden:
- **Variante 1** (Java-Ladeprogramm): Schreiben Sie als Ladeprogramm ein Java-Programm, welches Rohdatensätze aus XML- sowie zeichenseparierten-Dateien (CSV) einliest und im Falle syntaktischer und semantischer Korrektheit in Ihre Datenbank einspeichert.

- **Variante 2** (DB2 XML-Funktionalität): Erstellen Sie geeignete Anfragen in SQL inkl. postgres XML-Erweiterung, um die XML-Daten in ihr relationales Modell zu füllen. 

Die Konsistenz eines Satzes muss festgestellt werden, indem zum einen die Korrektheit des Satzes an sich untersucht wird und zum anderen seine Verträglichkeit mit den bereits in der Datenbank enthaltenen Daten geprüft wird. Als inkonsistent erkannte Sätze sollen dabei zusammen mit einer sinnvollen Begründung der Ablehnung in eine zu erzeugende Datei oder Datenbanktabelle ausgegeben werden.

Betrachten Sie die unten aufgeführten, potentiellen Ursachen für Inkonsistenzen und überlegen Sie sich, welche Konsistenzprüfungen beim Laden der DB durchgeführt werden müssen und wie/wann diese in die Datenbank eingebracht werden müssen. Wählen Sie eine sinnvolle Ladereihenfolge der Datensätze, um alle eventuellen Abhängigkeiten zwischen zu ladenden und bereits in der Datenbank existierenden Sätzen berücksichtigen zu können.

Potentielle Ursachen für Inkonsistenzen sind:

- Syntaxfehler
- Duplikatsätze
- Datentypen müssen eingehalten werden bzw. bedürfen einer Konvertierung
- NULL-Werte erkennen/beachten
- Fehler inhaltlicher, auch relationenübergreifender Art
- Werte müssen sinnvoll sein, z.B. bzgl. des aktuellen Datums, Altersangaben, etc.

## Abgabe
**Für Teil 1a**

Relationenmodell mit Primärschlüssel und Fremdschlüsselbeziehungen

**Für Teil 1b**

- Datei zur Erzeugung Ihres Datenbankschemas, inklusive aller dort enthaltenen Statements zur Integritätssicherung
- kommentiertes Java-Ladeprogramm bzw. SQL Statements zur XML-Transformation
- Datei/Relation mit den abgelehnten Datensätzen, die für jede gefundene Inkonsistenz folgendes (oder ein ähnliches) Format aufweisen soll:
  - ERROR: Entityname (Stadt,Land,Produkt,…), Name des fehlerhaften Attributs, System- oder selbst formulierte Fehlermeldung
  - Dieses Fehlerprotokoll soll durch Ihr Ladeprogramm automatisch erzeugt werden. Wie viele Fehler welcher Art gab es jeweils?


  

 


