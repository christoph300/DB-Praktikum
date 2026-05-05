1. Relationenmodell erstellen

2. Parser schreiben
2.1. StoreParser:
- liest nur die beiden Stores ein(Insertfunctions)
2.2 Items parser:
- liest alle Items ein, außer die die in similiars vorkommen(keine kindsknoten)
- prüft Produktkategoriespezifische Werte(Asin, Title, Salesrank, Image)
- fügt diese Werte mittels insertfunctions ein
- called Book,DVD,CD Parser 
2.3.1 Book Parser
- prüft Buchspezifische Werte ein
- liest 
2.3.2 DVD Parser
2.3.3 CD Parser