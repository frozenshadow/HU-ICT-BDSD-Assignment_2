# HU-ICT BDSD Assignment 2

## Gebruik
Deze applicatie is eenvoudig uit te voeren via Hadoop middels een enkele JAR.
Echter, er zijn nog wel wat additionele bestanden nodig. Het algoritme dient namelijk getraind te worden.
In de map `trainingset` binnen deze repo zijn hier al twee sets (Engels en Nederlands) opgenomen. Je kunt deze map downloaden en gebruiken óf zelf een aantal bestanden opgeven.
Zolang je via de optie `-t` maar een map meegeeft.
In de `input` map kun je vervolgens een tekst bestand plaatsen die je wilt identificeren. De `output` map wordt alleen gebruikt door Hadoop, maar zal geen nuttige informatie voor de eindgebruiker bevatten.
De nuttige info, namelijk het aantal lijnen voor de betreffende taal verschijnt aan het einde in de console.

Het commando ziet er dan als volgt uit: 
```
bin/hadoop jar HU-SD-BDSD_Assignment_2.jar com.company.Main input output -t trainingset
```

**LET OP!:** het uitvoeren kan een geruime tijd duren (+- 3 min.). Zolang je Hadoop logs voorbij ziet komen werkt alles nog.

## Screens