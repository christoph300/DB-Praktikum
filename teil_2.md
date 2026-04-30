# Aufgabenstellung

In diesem Teil soll soll demonstriert werden, dass die Datenbank den bei der Informationsanalyse deutlich gewordenen Anforderungen genügt.


Es sind Anfragen auf der Datenbank durchzuführen. In einem zweiten Schritt der Teilaufgabe sollen Änderungsoperationen auf der geladenen Datenbank durchgeführt werden. Dabei ist insbesondere auf die Wahrung der Konsistenz zu achten.

## 2a) Anfragen auf der Datenbank

In diesem Teil sollen einige SQL-Anfragen auf der von Ihnen erzeugten Datenbank durchgeführt werden.

Folgende Fragen sollen mittels SQL beantwortet werden. Die SQL-Statements und die Ergebnissätze sind als Textdatei gut lesbar formatiert abzulegen.



1. Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst? Hinweis: Geben Sie das Ergebnis in einer 3-spaltigen Relation aus.

2. Nennen Sie die 5 besten Produkte jedes Typs (Buch, Musik-CD, DVD) sortiert nach dem durchschnittlichem Rating. Hinweis: Geben Sie das Ergebnis in einer einzigen Relation mit den Attributen Typ, ProduktNr, Rating aus.

3. Für welche Produkte gibt es im Moment kein Angebot?

4. Für welche Produkte ist das teuerste Angebot mehr als doppelt so teuer wie das preiswerteste?

5. Welche Produkte haben sowohl mindestens eine sehr schlechte (Punktzahl: 1) als auch mindestens eine sehr gute (Punktzahl: 5) Bewertung?

6. Für wieviele Produkte gibt es gar keine Rezension?

7. Nennen Sie alle Rezensenten, die mindestens 10 Rezensionen geschrieben haben.

8. Geben Sie eine duplikatfreie und alphabetisch sortierte Liste der Namen aller Buchautoren an, die auch an DVDs oder Musik-CDs beteiligt sind.

9. Wie hoch ist die durchschnittliche Anzahl von Liedern einer Musik-CD?

10. Für welche Produkte gibt es ähnliche Produkte in einer anderen Hauptkategorie? Hinweis: Eine Hauptkategorie ist eine Produktkategorie ohne Oberkategorie. Erstellen Sie eine rekursive Anfrage, die zu jedem Produkt dessen Hauptkategorie bestimmt.

11. Welche Produkte werden in allen Filialen angeboten? Hinweis: Ihre Query muss so formuliert werden, dass sie für eine beliebige Anzahl von Filialen funktioniert. Hinweis: Beachten Sie, dass ein Produkt mehrfach von einer Filiale angeboten werden kann (z.B. neu und gebraucht).

12. In wieviel Prozent der Fälle der Frage 11 gibt es in Leipzig das preiswerteste Angebot?

## 2b) Änderungen in der erzeugten Datenbank


In die Datenbank können durch Nutzer neue Rezensionen mit Produktbewertungen eingetragen werden. Dabei ist die Konsistenz der Daten dahingehend zu wahren, dass die durchschnittliche Bewertung eines Produktes automatisch angepasst werden muss. Implementieren Sie dazu ein entsprechendes Verfahren. Hinweis: Die Durchschnittsbewertung muss als Attribut in der Produkttabelle abgelegt werden. Eine dynamische Berechnung mit Views ist also nicht erwünscht.

# Abgabe

Zum Zeitpunkt der Fertigstellung (spätestens 24h vor dem Testat) sollen die nachfolgend beschriebenen Dateien verfügbar ihrem Betreuer per E-Mail zugänglich gemacht worden sein.


Für Teil 2a

- Eine Datei, die die SQL-Anfragen und Antworten enthält. Die Anfragen sind zu nummerieren und gut lesbar zu formatieren.

Für Teil 2b

- Datei mit notwendigen SQL-Statements (für ein ausgewähltes Beispiel)

- Datei mit Anweisungen zur Integritätssicherung


