# Die Miniwelt "Media Store"

In der Datenbank sollen Informationen eines "Media Stores" abgebildet werden.

Der "Media Store" enthält eine Vielzahl von Produkten, die durch Titel, Rating, Verkaufsrang, eindeutige Produktnummer und (optional) ein Bild gekennzeichnet sind. Die in unserer Miniwelt betrachteten Produkte sind Bücher, Musik-CDs und DVDs. Für Bücher werden Autoren, Seitenzahl, Erscheinungsdatum, ISBN-Nummer und der herausgebende Verlag gespeichert. DVDs besitzen neben einem Format, einer Laufzeit und einem Region-Code auch beteiligte Personen (Actor, Creator und Director). Musik-CDs stammen von mindestens einem Künstler und haben außer Label und Erscheinungsdatum auch eine Liste von Titeln, die durch ihren Namen gegeben sind.

Zur besseren Orientierung im Produktkatalog ist jedes Produkt einer Kategorie oder mehreren Kategorien zugeordnet. Jede Kategorie hat einen Namen. Des Weiteren sind die Kategorien hierarchisch angeordnet, d.h. eine Kategorie kann eine oder mehrere Unterkategorien besitzen. Kategorien, die selbst keine Unterkategorie einer anderen Kategorie sind, werden Hauptkategorien genannt. Zu jedem Produkt kann es eine Menge ähnlicher Produkte geben.

Produkte im "Media Store" werden von verschiedenen Fillialen (charakterisiert durch Namen und Anschrift) zu unterschiedlichen Konditionen (Preis, Verfügbarkeit, Zustand) angeboten, so dass Kunden diese über einen Online-Shop kaufen können. Die Verfügbarkeit ist dadurch gekennzeichnet, ob ein Preis vorliegt oder nicht. Jeder Kunde kann - nach Angabe von Lieferadresse und Kontonummer - mehrere Produkte zu verschiedenen Zeiten kaufen. Der Online-Shop des "Media Stores" bietet Kunden weiterhin die Möglichkeit, Produkte zu bewerten. Dazu können sie eine Kundenrezension schreiben und für das Produkt ein bis fünf Punkte vergeben.

## Hinweise


- Die Kategoriennamen sind nicht eindeutig, d.h. es kann zwei verschiedene Kategorien mit gleichem Namen geben. Wie kann man dennoch eine eindeutige Kennung für eine Kategorie definieren?
- Klären Sie, ob die Relation “ähnliche Produkte” symmetrisch ist und ob dies Auswirkungen auf Ihr Modell und auf spätere Anfragen hat.
- Wie soll das Rating für ein Produkt aus den Kundenrezensionen ermittelt werden?

- Wie bei jedem Programmentwurf sollten Sie auch hier schon an spätere Tests Ihrer Datenbank denken. Bedenken Sie deshalb schon bei der konzeptionellen Arbeit, daß Sie in Teilaufgabe 2 u.a. folgende oder ähnliche umgangssprachlich formulierte Testfragen unter Zuhilfenahme passender SQL-Statements beantworten sollen:

    1. Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst?

    2. Nennen Sie die 5 besten Produkte jeder Hauptkategorie sortiert nach dem durchschnittlichem Rating.

    3. Für welche Produkte gibt es im Moment kein Angebot?

    4. Für welche Produkte ist das teuerste Angebot mehr als doppelt so teuer wie das preiswerteste?

    5. Welche Produkte haben sowohl mindestens eine sehr schlechte (Punktzahl: 1) als auch mindestens eine sehr gute (Punktzahl: 5) Bewertung?

    6. Für wieviele Produkte gibt es gar keine Rezension?

    7. Nennen Sie alle Rezensenten, die mehr als 10 Rezensionen geschrieben haben.

    8. Geben Sie eine duplikatfreie und alphabetisch sortierte Liste der Namen aller Buchautoren an, die auch an DVDs oder Musik-CSs beteiligt sind.

    9. Wie hoch ist die durchschnittliche Anzahl von Liedern einer Musik-CD?

    10. Für welche Produkte gibt es ähnliche Produkte in einer anderen Hauptkategorie?

    11. Wieviele Kunden haben ein Produkt doppelt gekauft?

    12. Wie hoch ist der durschnittliche Preis eines Warenkorbs, d.h. die durchschnittliche Summe der Preise aller Produkte, die zur gleichen Zeit gekauft wurden?

    13. Welche Produkte werden in allen Fillialen angeboten? Hinweis: Ihre Query muss so formuliert werden, dass sie für eine beliebige Anzahl von Fillialen funktioniert.

    14. In wieviel Prozent der Fälle der Frage 13 gibt es in Leipzig das preiswerteste Angebot?

Hinweis: Soweit nicht anders angegeben, soll bei Produktlisten eine duplikatfreie und alphabetisch geordnete Liste der Produktnummern erstellt werden.
