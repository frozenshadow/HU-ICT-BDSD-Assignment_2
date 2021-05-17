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
![Demo](https://raw.githubusercontent.com/frozenshadow/HU-ICT-BDSD-Assignment_2/master/demo.gif)

## Bronnen
De volgende significante bronnen zijn geraadpleegd tijdens de ontwikkeling van dit stuk software:
- https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/SingleCluster.html
- http://www.javapractices.com/topic/TopicAction.do?Id=65
- https://zetcode.com/java/console/
- https://stackoverflow.com/questions/367706/how-do-i-parse-command-line-arguments-in-java
- http://commons.apache.org/proper/commons-cli/usage.html
- https://www.datasciencecentral.com/profiles/blogs/how-to-install-and-run-hadoop-on-windows-for-beginners
- https://kontext.tech/column/hadoop/447/install-hadoop-330-on-windows-10-step-by-step-guide
- http://hadooptutorial.info/mapreduce-use-case-for-n-gram-statistics/
- http://sujitpal.blogspot.com/2012/04/generating-unigram-and-bigrams-into.html
- http://tutorials.jenkov.com/java-internationalization/breakiterator.html
- http://gandhigeet.blogspot.com/2012/12/as-discussed-in-previous-post-hadoop.html
- https://stackoverflow.com/questions/12926474/hadoop-chainmapper-chainreducer
- https://mrchief2015.wordpress.com/2015/02/09/compiling-and-debugging-hadoop-applications-with-intellij-idea-for-windows/
- https://mkyong.com/java8/java-8-how-to-sort-a-map/
- https://www.edureka.co/blog/mapreduce-tutorial/
- https://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html
- https://stackoverflow.com/questions/27349743/hadoop-multiple-inputs
- https://www.baeldung.com/java-find-map-max
- https://beginnersbook.com/2017/10/java-8-filter-a-map-by-keys-and-values/
- https://stackoverflow.com/questions/19486077/java-fastest-way-to-read-through-text-file-with-2-million-lines
