package com.example.tombolator;

import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaDao;
import com.example.tombolator.tombolas.Tombola;
import com.example.tombolator.tombolas.TombolaDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StashScript implements Runnable {

    private static List<Media> MEDIA_CACHE;
    private final TomboApplication context;

    String mediaContentString;

    public StashScript(TomboApplication context) {
        this.context = context;
    }

    @Override
    public void run() {

        if(MEDIA_CACHE == null) {
            setUpAndSaveMediaToDatabase();
            MEDIA_CACHE = getMediaFromDatabase();
        }

        setUpTombolas(MEDIA_CACHE);
    }

    private void setUpAndSaveMediaToDatabase() {

        MediaDao mediaDao = context.getTomboDb().mediaDao();
        mediaDao.nukeTable();

        try {
            for(Media media : createMediaList())
                mediaDao.insertMedia(media);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Media> getMediaFromDatabase() {

        return context.getTomboDb().mediaDao().getAllMedia();
    }

    private void setUpTombolas(List<Media> mediaList) {

        TombolaDao tombolaDao = context.getTomboDb().tombolaDao();
        tombolaDao.nukeTable();

        Tombola tombola = new Tombola();

        tombola.setName("Disney+ Filme");
        tombola.setCreationTimestamp(System.currentTimeMillis());
        tombola.setType(Tombola.Type.REUSE);

        for(Media media : mediaList) {
            if(media.getContentType().equals(Media.ContentType.MOVIE))
                tombola.addMedia(media);
        }

        tombolaDao.insertTombola(tombola);
    }

    private List<Media> createMediaList() throws IOException {

        /* Loading from cache. */
        if(MEDIA_CACHE != null)
            return MEDIA_CACHE;

        MEDIA_CACHE = new ArrayList<>();

        String mediaContentString = createMediaContentAsString();

        String[] lines = mediaContentString.split("\n");

        for (int i=0; i<lines.length; i++) {

            String line = lines[i];
            String[] mediaValues = line.split(";");

            try {

                String name = mediaValues[0];
                String title = mediaValues[1];
                String numberAsString = mediaValues[2];
                String author = mediaValues[3];
                String carrierType = mediaValues[4];
                String contentType = mediaValues[5];

                int number = numberAsString.isEmpty() ? 1 : Integer.parseInt(numberAsString);

                Media media = new Media(name, title, number, author ,carrierType, contentType);
                MEDIA_CACHE.add(media);

            }catch (ArrayIndexOutOfBoundsException e) {
                /* TODO: Add logger entry. */
                System.err.println("On line " + i + " the number of elements does not match the" +
                        " variables fetched");
                System.err.println(e);
            }
        }

        return MEDIA_CACHE;
    }

    private String createMediaContentAsString() {

        /* Loading from cache. */
        if(mediaContentString != null)
            return mediaContentString;

        mediaContentString = "Bibi Blocksberg;Die Schloßgespenster;8;;CD;Hörspiel\n" +
                "Bibi Blocksberg;in Amerika;14;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Auf dem Hexenberg;18;;CD;Hörspiel\n" +
                "Bibi Blocksberg;zieht um;21;;CD;Hörspiel\n" +
                "Bibi Blocksberg;3× schwarzer Kater;22;;CD;Hörspiel\n" +
                "Bibi Blocksberg;in der Ritterzeit;30;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Der Hexenfluch;35;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Karla gibt nicht auf;46;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Das Reitturnier;47;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Der Hexengeburtstag;49;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Der blaue Brief;57;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Freitag, der 13.;73;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Schubia dreht durch;76;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Das Schmusekätzchen;80;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Hexspruch mit Folgen;82;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Die Klassenreise;83;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Die verhexte Zeitreise;94;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Die Prinzessinnen von Thunderstorm;98;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Die große Hexenparty;100;;CD;Hörspiel\n" +
                "Bibi Blocksberg;und Piraten-Lilly;101;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Der Hexenschatz;103;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Der Familienausflug;108;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Urlaub in der Hexenpension;115;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Im Wald der Hexenbesen;116;;CD;Hörspiel\n" +
                "Bibi Blocksberg;Die Besenflugprüfung;117;;CD;Hörspiel\n" +
                "Bibi & Tina;Amadeus ist krank;2;;CD;Hörspiel\n" +
                "Bibi & Tina;Papis Pony;11;;CD;Hörspiel\n" +
                "Bibi & Tina;Gefahr für Falkenstein;40;;CD;Hörspiel\n" +
                "Bibi & Tina;Das Kürbisfest;50;;CD;Hörspiel\n" +
                "Bibi & Tina;Besuch aus Spanien;51;;CD;Hörspiel\n" +
                "Bibi & Tina;Freddy in der Klemme;52;;CD;Hörspiel\n" +
                "Bibi & Tina;Die Überraschungsparty;56;;CD;Hörspiel\n" +
                "Bibi & Tina;Das große Teamspringen;57;;CD;Hörspiel\n" +
                "Bibi & Tina;Holgers Versprechen;62;;CD;Hörspiel\n" +
                "Bibi & Tina;Abschied von Amadeus;65;;CD;Hörspiel\n" +
                "Bibi & Tina;Ausritt mit Folgen;69;;CD;Hörspiel\n" +
                "Bibi & Tina;Spuk im Wald;74;;CD;Hörspiel\n" +
                "Bibi & Tina;Das Gestüt in England;78;;CD;Hörspiel\n" +
                "Bibi & Tina;Der Pferde-Treck;81;;CD;Hörspiel\n" +
                "Bibi & Tina;Die Reiterspiele;82;;CD;Hörspiel\n" +
                "Bibi & Tina;Freddy verliebt sich;83;;CD;Hörspiel\n" +
                "Bibi & Tina;Der Weihnachtsmarkt;84;;CD;Hörspiel\n" +
                "Bibi & Tina;Das Geheimnis der Alten Mühle;85;;CD;Hörspiel\n" +
                "Bibi & Tina;Das große Unwetter;87;;CD;Hörspiel\n" +
                "Bibi & Tina;Ein schlimmer Verdacht;88;;CD;Hörspiel\n" +
                "Bibi & Tina;Der neue Reiterhof;90;;CD;Hörspiel\n" +
                "Bibi & Tina;Der Freundschaftstag;91;;CD;Hörspiel\n" +
                "Bibi & Tina;Mission Alex;92;;CD;Hörspiel\n" +
                "Bibi & Tina;Graf für einen Tag;94;;CD;Hörspiel\n" +
                "Bibi & Tina;Vollmond über Falkenstein;95;;CD;Hörspiel\n" +
                "Bibi & Tina;Reiten verboten!;96;;CD;Hörspiel\n" +
                "Benjamin Blümchen;als Wetterelefant;1;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;in Afrika;4;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;und die Schule;6;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;und das Schloss;10;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;im Krankenhaus;13;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;träumt;16;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;Der Skiurlaub;17;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;als Schornsteinfeger;18;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;und Bibi Blocksberg;20;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;als Weihnachtsmann;21;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;als Koch;23;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;auf dem Bauernhof;27;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;wird verhext;36;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;kauft ein;39;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;zieht aus;40;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;als Pirat;41;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;als Bäcker;44;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;hilft den Tieren;46;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;kriegt ein Geschenk;48;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;wird reich;53;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;ist krank;54;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;als Bürgermeister;57;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;Die Wünschelrute;58;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;Der Doppelgänger;60;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;Otto ist krank;61;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;als Ballonfahrer;66;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;als Zoodirektor;69;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;und Bibi in Indien;70;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;und der Weihnachtsmann;73;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;singt Weihnachtslieder;74;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;und die Eisprinzessin;77;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;Die neue Zooheizung;80;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;Das Zoojubiläum;90;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;Die Gespensterkinder;97;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;Ottos neue Freundin, Teil 1;100;;Kassette;Hörspiel\n" +
                "Benjamin Blümchen;Ottos neue Freundin, Teil 2;101;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Hexen gibt es doch;1;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Hexerei in der Schule;2;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Die Zauberlimonade;3;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der Bankräuber;4;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Ein verhexter Urlaub;5;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Die Kuh im Schlafzimmer;6;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;heilt den Bürgermeister;7;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;verliebt sich;9;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Bibis neue Freundin;10;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der Schulausflug;11;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;hat Geburtstag;12;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Ein verhexter Sonntag;13;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Die schwarzen Vier;15;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Das Schulfest;16;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der kleine Hexer;17;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Das Sportfest;19;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Papa ist weg;20;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;und der Autostau;23;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;und der Supermarkt;24;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;reißt aus;25;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der neue Hexenbesen;29;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Auf der Märcheninsel;31;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;als Prinzessin;32;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Die weißen Enten;36;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das Fohlen;1;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Papi lernt reiten;3;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das Zirkuspony;4;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das Heiderennen;5;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Der Abschied;6;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Tina in Gefahr;7;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Der Hufschmied;8;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Der fliegende Sattel;9;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das Zeltlager;10;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Der Liebesbrief;12;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Der rote Hahn;15;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Alle lieben Knuddel;16;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das Herbst-Turnier;17;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Der große Bruder;19;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Mami siegt;20;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Ein Pferd für Tante Paula;23;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das Weihnachtsfest;25;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Die Osterferien;26;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Der Pferdegeburtstag;27;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Die wilde Meute;28;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das sprechende Pferd;29;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Eine Freundin für Felix;30;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Die Tierärztin;31;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das Schmusepony;32;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Alex und das Internat;33;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das Gespensterpferd;34;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Die falsche Freundin;35;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das Pferd in der Schule;36;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Der Pferdetausch;37;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Der Glücksbringer;38;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das Findel-Fohlen;39;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Die Superponys;42;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Konkurrenz für Alex;43;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Der Verehrer;44;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das Liebeskraut;46;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Die Schnitzeljagd-Falle;47;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Ein ungebetener Gast;48;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Die Pferdeprinzessin;49;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Der verhexte Wanderritt;53;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Das Amulett der Gräfin;54;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Die rätselhafte Schatzsuche;58;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Allein im Schloss;66;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Die Urlaubsüberraschung;68;;Kassette;Hörspiel\n" +
                "Bibi & Tina;Verloren im Schnee;73;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der Flohmarkt;37;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Die Weihnachtsmänner;38;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Unverhofftes Wiedersehen;39;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;und die Vampire;40;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Ohne Mami geht es nicht;41;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der Reiterhof – Teil 1;43;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der Reiterhof – Teil 2;44;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;ist krank;48;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;im Orient;50;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der Superhexspruch;53;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Die Computerhexe;54;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Mamis Geburtstag;55;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der Wetterfrosch;56;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;und das Dino-Ei;58;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;und Dino;59;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der Geisterkater;60;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Das Wettfliegen;65;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Das verhexte Osterei;66;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Verhexte Weihnachten;69;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Das gestohlene Hexenkraut;70;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Die Hexenschule;71;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der verhexte Kalender;72;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der Brieffreund;74;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Die neue Lehrerin;75;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;im Hexeninternat;77;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;und Elea Eluanda;78;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Papis Geburtstag;79;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Das Hexenhoroskop;84;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Kann Papi hexen?;86;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Die Junghexenbande;89;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Oma Grete sorgt für Wirbel;90;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Maritas Geheimnis;91;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Das geheimnisvolle Schloss;92;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Das traurige Einhorn;96;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Überraschung für Mami;97;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der verhexte Bürgermeister;104;;Kassette;Hörspiel\n" +
                "Bibi Blocksberg;Der Hexenbesen-Dieb;111;;Kassette;Hörspiel\n" +
                "Doug;Der 1. Film;;;Streaming;Film\n" +
                "Basil, der große Mäusedetektiv;;;;Streaming;Film\n" +
                "Winnie Puuh;;;;Streaming;Film\n" +
                "Winnie Puuh;Die vielen Abenteuer von Winnie Puuh;;;Streaming;Film\n" +
                "Winnie Puuh;Spaß im Frühling;;;Streaming;Film\n" +
                "Heffalump;Ein neuer Freund für Winnie Puuh;;;Streaming;Film\n" +
                "Tiggers großes Abenteuer;;;;Streaming;Film\n" +
                "Tinkerbell;;;;Streaming;Film\n" +
                "Tinkerbell;Ein Sommer voller Abenteuer;;;Streaming;Film\n" +
                "Tinkerbell;und die Legende vom Nimmerbiest;;;Streaming;Film\n" +
                "Tinkerbell;Die Suche nach dem verlorenen Schatz;;;Streaming;Film\n" +
                "Oben;;;;Streaming;Film\n" +
                "Große Pause;Die geheime Mission;;;Streaming;Film\n" +
                "Große Pause;Fünftklässler;;;Streaming;Film\n" +
                "Manolo und das Buch des Lebens;;;;Streaming;Film\n" +
                "Anastasia;;;;Streaming;Film\n" +
                "Ferkels großes Abenteuer;;;;Streaming;Film\n" +
                "Tarzan;;;;Streaming;Film\n" +
                "Rapunzel;Neu verföhnt;;;Streaming;Film\n" +
                "Mary Poppins 1;;;;Streaming;Film\n" +
                "Mary Poppins 2;;;;Streaming;Film\n" +
                "Alice im Wunderland;;;;Streaming;Film\n" +
                "101 Dalmatiner;;;;Streaming;Film\n" +
                "Bärenbrüder;;;;Streaming;Film\n" +
                "Schneewittchen;;;;Streaming;Film\n" +
                "Zoomania;;;;Streaming;Film\n" +
                "Vaiana;;;;Streaming;Film\n" +
                "Das Dschungelbuch;;;;Streaming;Film\n" +
                "Dornröschen;;;;Streaming;Film\n" +
                "Aristocats;;;;Streaming;Film\n" +
                "Die Eiskönigin;Völlig unferforen;;;Streaming;Film\n" +
                "Aladdin;;;;Streaming;Film\n" +
                "Die Schöne und das Biest;;;;Streaming;Film\n" +
                "Die Schöne und das Biest;Weihnachtszauber;;;Streaming;Film\n" +
                "Die Hexe und der Zauberer;;;;Streaming;Film\n" +
                "Die Unglaublichen;;;;Streaming;Film\n" +
                "Bernhard & Bianca;;;;Streaming;Film\n" +
                "Coco;;;;Streaming;Film\n" +
                "Cinderella;;;;Streaming;Film\n" +
                "Verwünscht;;;;Streaming;Film\n" +
                "Pocahontas;;;;Streaming;Film\n" +
                "Mulan;;;;Streaming;Film\n" +
                "Peter Pan;;;;Streaming;Film\n" +
                "Robin Hood;;;;Streaming;Film\n" +
                "Susi & Strolch;;;;Streaming;Film\n" +
                "Hercules;;;;Streaming;Film\n" +
                "Atlantis;;;;Streaming;Film\n" +
                "Ralph Reichts;Chaos im Netz;;;Streaming;Film\n" +
                "Lilo & Stitch;;;;Streaming;Film\n" +
                "Der König der Löwen;;;;Streaming;Film\n" +
                "Küss den Frosch;;;;Streaming;Film\n" +
                "Alles steht Kopf;;;;Streaming;Film\n"+
                "High School Musical 1, 2 & 3;;;;Streaming;Film\n" +
                "Die Bücherdiebin;;;;Streaming;Film\n" +
                "Pretty Women;;;;Streaming;Film\n" +
                "Christopher Robin;;;;Streaming;Film\n" +
                "Bibi & Tina - Voll verhext!;;;;Streaming;Film\n" +
                "Vier zauberhafte Schwestern;;;;Streaming;Film\n" +
                "Der König der Löwen;;;;Streaming;Film\n" +
                "Bibi & Tina;;;;Streaming;Film\n" +
                "Die gute Fee;;;;Streaming;Film\n" +
                "Die Familie Stone - Verloben verboten;;;;Streaming;Film\n" +
                "10 Dinge, die ich an Dir hasse;;;;Streaming;Film\n" +
                "Bibi & Tina - Tohuwabohu Total;;;;Streaming;Film\n" +
                "Mrs. Doubtfire;;;;Streaming;Film\n" +
                "Steinzeit Junior;;;;Streaming;Film\n" +
                "Noelle;;;;Streaming;Film\n" +
                "Into the Woods;;;;Streaming;Film\n" +
                "Rubinrot;;;;Streaming;Film\n" +
                "Bibi & Tina - Mädchen gegen Jungs;;;;Streaming;Film\n" +
                "Susi & Strolch;;;;Streaming;Film\n" +
                "Guardians of the Galaxy;;;;Streaming;Film\n" +
                "Der geheime Club der zweitgeborenen Royals;;;;Streaming;Film\n" +
                "Harry Potter;;;;Streaming;Film\n" +
                "Black Panther;;;;Streaming;Film\n" +
                "Captain Marvel;;;;Streaming;Film\n" +
                "Dumbo;;;;Streaming;Film\n" +
                "Der Nussknacker und die vier Reiche;;;;Streaming;Film\n" +
                "Das Zeiträtsel;;;;Streaming;Film\n" +
                "Mulan;;;;Streaming;Film\n" +
                "Die Schöne und das Biest;;;;Streaming;Film\n" +
                "Avengers;;;;Streaming;Film\n" +
                "Ant-Man;;;;Streaming;Film\n" +
                "Alice im Wunderland;;;;Streaming;Film\n" +
                "Descendants;;;;Streaming;Film\n" +
                "Die Liga der außergewöhnlichen Gentleman;;;;Streaming;Film\n";

        return mediaContentString;
    }
}