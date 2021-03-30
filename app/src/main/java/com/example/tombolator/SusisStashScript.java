package com.example.tombolator;

import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SusisStashScript implements Runnable {

    private final TomboDbApplication context;

    String mediaContentString;
    List<Media> mediaList;

    public SusisStashScript(TomboDbApplication context) {
        this.context = context;
    }

    @Override
    public void run() {

        MediaDao mediaDao = context.getTomboDb().mediaDao();
        mediaDao.nukeTable();

        try {
            for(Media media : createMediaList()) {
                mediaDao.insertMedia(media);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Media> createMediaList() throws IOException {

        /* Loading from cache. */
        if(mediaList != null)
            return mediaList;

        mediaList = new ArrayList<>();

        String mediaContentString = createMediaContentAsString();

        String[] lines = mediaContentString.split("\n");

        for (String line : lines) {

            String[] mediaValues = line.split(";");

            String name = mediaValues[0];
            String title = mediaValues[1];
            int number = Integer.parseInt(mediaValues[2]);
            String type = mediaValues[3];

            Media media = new Media(name, title, number ,type);
            mediaList.add(media);
        }

        return mediaList;
    }

    private String createMediaContentAsString() {

        /* Loading from cache. */
        if(mediaContentString != null)
            return mediaContentString;

        mediaContentString = "Bibi Blocksberg;Die Schloßgespenster;8;CD\n" +
                "Bibi Blocksberg;in Amerika;14;CD\n" +
                "Bibi Blocksberg;Auf dem Hexenberg;18;CD\n" +
                "Bibi Blocksberg;zieht um;21;CD\n" +
                "Bibi Blocksberg;3× schwarzer Kater;22;CD\n" +
                "Bibi Blocksberg;in der Ritterzeit;30;CD\n" +
                "Bibi Blocksberg;Der Hexenfluch;35;CD\n" +
                "Bibi Blocksberg;Karla gibt nicht auf;46;CD\n" +
                "Bibi Blocksberg;Das Reitturnier;47;CD\n" +
                "Bibi Blocksberg;Der Hexengeburtstag;49;CD\n" +
                "Bibi Blocksberg;Der blaue Brief;57;CD\n" +
                "Bibi Blocksberg;Freitag, der 13.;73;CD\n" +
                "Bibi Blocksberg;Schubia dreht durch;76;CD\n" +
                "Bibi Blocksberg;Das Schmusekätzchen;80;CD\n" +
                "Bibi Blocksberg;Hexspruch mit Folgen;82;CD\n" +
                "Bibi Blocksberg;Die Klassenreise;83;CD\n" +
                "Bibi Blocksberg;Die verhexte Zeitreise;94;CD\n" +
                "Bibi Blocksberg;Die Prinzessinnen von Thunderstorm;98;CD\n" +
                "Bibi Blocksberg;Die große Hexenparty;100;CD\n" +
                "Bibi Blocksberg;und Piraten-Lilly;101;CD\n" +
                "Bibi Blocksberg;Der Hexenschatz;103;CD\n" +
                "Bibi Blocksberg;Der Familienausflug;108;CD\n" +
                "Bibi Blocksberg;Urlaub in der Hexenpension;115;CD\n" +
                "Bibi Blocksberg;Im Wald der Hexenbesen;116;CD\n" +
                "Bibi Blocksberg;Die Besenflugprüfung;117;CD\n" +
                "Bibi & Tina;Amadeus ist krank;2;CD\n" +
                "Bibi & Tina;Papis Pony;11;CD\n" +
                "Bibi & Tina;Gefahr für Falkenstein;40;CD\n" +
                "Bibi & Tina;Das Kürbisfest;50;CD\n" +
                "Bibi & Tina;Besuch aus Spanien;51;CD\n" +
                "Bibi & Tina;Freddy in der Klemme;52;CD\n" +
                "Bibi & Tina;Die Überraschungsparty;56;CD\n" +
                "Bibi & Tina;Das große Teamspringen;57;CD\n" +
                "Bibi & Tina;Holgers Versprechen;62;CD\n" +
                "Bibi & Tina;Abschied von Amadeus;65;CD\n" +
                "Bibi & Tina;Ausritt mit Folgen;69;CD\n" +
                "Bibi & Tina;Spuk im Wald;74;CD\n" +
                "Bibi & Tina;Das Gestüt in England;78;CD\n" +
                "Bibi & Tina;Der Pferde-Treck;81;CD\n" +
                "Bibi & Tina;Die Reiterspiele;82;CD\n" +
                "Bibi & Tina;Freddy verliebt sich;83;CD\n" +
                "Bibi & Tina;Der Weihnachtsmarkt;84;CD\n" +
                "Bibi & Tina;Das Geheimnis der Alten Mühle;85;CD\n" +
                "Bibi & Tina;Das große Unwetter;87;CD\n" +
                "Bibi & Tina;Ein schlimmer Verdacht;88;CD\n" +
                "Bibi & Tina;Der neue Reiterhof;90;CD\n" +
                "Bibi & Tina;Der Freundschaftstag;91;CD\n" +
                "Bibi & Tina;Mission Alex;92;CD\n" +
                "Bibi & Tina;Graf für einen Tag;94;CD\n" +
                "Bibi & Tina;Vollmond über Falkenstein;95;CD\n" +
                "Bibi & Tina;Reiten verboten!;96;CD\n" +
                "Benjamin Blümchen;als Wetterelefant;1;Kassette\n" +
                "Benjamin Blümchen;in Afrika;4;Kassette\n" +
                "Benjamin Blümchen;und die Schule;6;Kassette\n" +
                "Benjamin Blümchen;und das Schloss;10;Kassette\n" +
                "Benjamin Blümchen;im Krankenhaus;13;Kassette\n" +
                "Benjamin Blümchen;träumt;16;Kassette\n" +
                "Benjamin Blümchen;Der Skiurlaub;17;Kassette\n" +
                "Benjamin Blümchen;als Schornsteinfeger;18;Kassette\n" +
                "Benjamin Blümchen;und Bibi Blocksberg;20;Kassette\n" +
                "Benjamin Blümchen;als Weihnachtsmann;21;Kassette\n" +
                "Benjamin Blümchen;als Koch;23;Kassette\n" +
                "Benjamin Blümchen;auf dem Bauernhof;27;Kassette\n" +
                "Benjamin Blümchen;wird verhext;36;Kassette\n" +
                "Benjamin Blümchen;kauft ein;39;Kassette\n" +
                "Benjamin Blümchen;zieht aus;40;Kassette\n" +
                "Benjamin Blümchen;als Pirat;41;Kassette\n" +
                "Benjamin Blümchen;als Bäcker;44;Kassette\n" +
                "Benjamin Blümchen;hilft den Tieren;46;Kassette\n" +
                "Benjamin Blümchen;kriegt ein Geschenk;48;Kassette\n" +
                "Benjamin Blümchen;wird reich;53;Kassette\n" +
                "Benjamin Blümchen;ist krank;54;Kassette\n" +
                "Benjamin Blümchen;als Bürgermeister;57;Kassette\n" +
                "Benjamin Blümchen;Die Wünschelrute;58;Kassette\n" +
                "Benjamin Blümchen;Der Doppelgänger;60;Kassette\n" +
                "Benjamin Blümchen;Otto ist krank;61;Kassette\n" +
                "Benjamin Blümchen;als Ballonfahrer;66;Kassette\n" +
                "Benjamin Blümchen;als Zoodirektor;69;Kassette\n" +
                "Benjamin Blümchen;und Bibi in Indien;70;Kassette\n" +
                "Benjamin Blümchen;und der Weihnachtsmann;73;Kassette\n" +
                "Benjamin Blümchen;singt Weihnachtslieder;74;Kassette\n" +
                "Benjamin Blümchen;und die Eisprinzessin;77;Kassette\n" +
                "Benjamin Blümchen;Die neue Zooheizung;80;Kassette\n" +
                "Benjamin Blümchen;Das Zoojubiläum;90;Kassette\n" +
                "Benjamin Blümchen;Die Gespensterkinder;97;Kassette\n" +
                "Benjamin Blümchen;Ottos neue Freundin, Teil 1;100;Kassette\n" +
                "Benjamin Blümchen;Ottos neue Freundin, Teil 2;101;Kassette\n" +
                "Bibi Blocksberg;Hexen gibt es doch;1;Kassette\n" +
                "Bibi Blocksberg;Hexerei in der Schule;2;Kassette\n" +
                "Bibi Blocksberg;Die Zauberlimonade;3;Kassette\n" +
                "Bibi Blocksberg;Der Bankräuber;4;Kassette\n" +
                "Bibi Blocksberg;Ein verhexter Urlaub;5;Kassette\n" +
                "Bibi Blocksberg;Die Kuh im Schlafzimmer;6;Kassette\n" +
                "Bibi Blocksberg;heilt den Bürgermeister;7;Kassette\n" +
                "Bibi Blocksberg;verliebt sich;9;Kassette\n" +
                "Bibi Blocksberg;Bibis neue Freundin;10;Kassette\n" +
                "Bibi Blocksberg;Der Schulausflug;11;Kassette\n" +
                "Bibi Blocksberg;hat Geburtstag;12;Kassette\n" +
                "Bibi Blocksberg;Ein verhexter Sonntag;13;Kassette\n" +
                "Bibi Blocksberg;Die schwarzen Vier;15;Kassette\n" +
                "Bibi Blocksberg;Das Schulfest;16;Kassette\n" +
                "Bibi Blocksberg;Der kleine Hexer;17;Kassette\n" +
                "Bibi Blocksberg;Das Sportfest;19;Kassette\n" +
                "Bibi Blocksberg;Papa ist weg;20;Kassette\n" +
                "Bibi Blocksberg;und der Autostau;23;Kassette\n" +
                "Bibi Blocksberg;und der Supermarkt;24;Kassette\n" +
                "Bibi Blocksberg;reißt aus;25;Kassette\n" +
                "Bibi Blocksberg;Der neue Hexenbesen;29;Kassette\n" +
                "Bibi Blocksberg;Auf der Märcheninsel;31;Kassette\n" +
                "Bibi Blocksberg;als Prinzessin;32;Kassette\n" +
                "Bibi Blocksberg;Die weißen Enten;36;Kassette\n" +
                "Bibi & Tina;Das Fohlen;1;Kassette\n" +
                "Bibi & Tina;Papi lernt reiten;3;Kassette\n" +
                "Bibi & Tina;Das Zirkuspony;4;Kassette\n" +
                "Bibi & Tina;Das Heiderennen;5;Kassette\n" +
                "Bibi & Tina;Der Abschied;6;Kassette\n" +
                "Bibi & Tina;Tina in Gefahr;7;Kassette\n" +
                "Bibi & Tina;Der Hufschmied;8;Kassette\n" +
                "Bibi & Tina;Der fliegende Sattel;9;Kassette\n" +
                "Bibi & Tina;Das Zeltlager;10;Kassette\n" +
                "Bibi & Tina;Der Liebesbrief;12;Kassette\n" +
                "Bibi & Tina;Der rote Hahn;15;Kassette\n" +
                "Bibi & Tina;Alle lieben Knuddel;16;Kassette\n" +
                "Bibi & Tina;Das Herbst-Turnier;17;Kassette\n" +
                "Bibi & Tina;Der große Bruder;19;Kassette\n" +
                "Bibi & Tina;Mami siegt;20;Kassette\n" +
                "Bibi & Tina;Ein Pferd für Tante Paula;23;Kassette\n" +
                "Bibi & Tina;Das Weihnachtsfest;25;Kassette\n" +
                "Bibi & Tina;Die Osterferien;26;Kassette\n" +
                "Bibi & Tina;Der Pferdegeburtstag;27;Kassette\n" +
                "Bibi & Tina;Die wilde Meute;28;Kassette\n" +
                "Bibi & Tina;Das sprechende Pferd;29;Kassette\n" +
                "Bibi & Tina;Eine Freundin für Felix;30;Kassette\n" +
                "Bibi & Tina;Die Tierärztin;31;Kassette\n" +
                "Bibi & Tina;Das Schmusepony;32;Kassette\n" +
                "Bibi & Tina;Alex und das Internat;33;Kassette\n" +
                "Bibi & Tina;Das Gespensterpferd;34;Kassette\n" +
                "Bibi & Tina;Die falsche Freundin;35;Kassette\n" +
                "Bibi & Tina;Das Pferd in der Schule;36;Kassette\n" +
                "Bibi & Tina;Der Pferdetausch;37;Kassette\n" +
                "Bibi & Tina;Der Glücksbringer;38;Kassette\n" +
                "Bibi & Tina;Das Findel-Fohlen;39;Kassette\n" +
                "Bibi & Tina;Die Superponys;42;Kassette\n" +
                "Bibi & Tina;Konkurrenz für Alex;43;Kassette\n" +
                "Bibi & Tina;Der Verehrer;44;Kassette\n" +
                "Bibi & Tina;Das Liebeskraut;46;Kassette\n" +
                "Bibi & Tina;Die Schnitzeljagd-Falle;47;Kassette\n" +
                "Bibi & Tina;Ein ungebetener Gast;48;Kassette\n" +
                "Bibi & Tina;Die Pferdeprinzessin;49;Kassette\n" +
                "Bibi & Tina;Der verhexte Wanderritt;53;Kassette\n" +
                "Bibi & Tina;Das Amulett der Gräfin;54;Kassette\n" +
                "Bibi & Tina;Die rätselhafte Schatzsuche;58;Kassette\n" +
                "Bibi & Tina;Allein im Schloss;66;Kassette\n" +
                "Bibi & Tina;Die Urlaubsüberraschung;68;Kassette\n" +
                "Bibi & Tina;Verloren im Schnee;73;Kassette\n" +
                "Bibi Blocksberg;Der Flohmarkt;37;Kassette\n" +
                "Bibi Blocksberg;Die Weihnachtsmänner;38;Kassette\n" +
                "Bibi Blocksberg;Unverhofftes Wiedersehen;39;Kassette\n" +
                "Bibi Blocksberg;und die Vampire;40;Kassette\n" +
                "Bibi Blocksberg;Ohne Mami geht es nicht;41;Kassette\n" +
                "Bibi Blocksberg;Der Reiterhof – Teil 1;43;Kassette\n" +
                "Bibi Blocksberg;Der Reiterhof – Teil 2;44;Kassette\n" +
                "Bibi Blocksberg;ist krank;48;Kassette\n" +
                "Bibi Blocksberg;im Orient;50;Kassette\n" +
                "Bibi Blocksberg;Der Superhexspruch;53;Kassette\n" +
                "Bibi Blocksberg;Die Computerhexe;54;Kassette\n" +
                "Bibi Blocksberg;Mamis Geburtstag;55;Kassette\n" +
                "Bibi Blocksberg;Der Wetterfrosch;56;Kassette\n" +
                "Bibi Blocksberg;und das Dino-Ei;58;Kassette\n" +
                "Bibi Blocksberg;und Dino;59;Kassette\n" +
                "Bibi Blocksberg;Der Geisterkater;60;Kassette\n" +
                "Bibi Blocksberg;Das Wettfliegen;65;Kassette\n" +
                "Bibi Blocksberg;Das verhexte Osterei;66;Kassette\n" +
                "Bibi Blocksberg;Verhexte Weihnachten;69;Kassette\n" +
                "Bibi Blocksberg;Das gestohlene Hexenkraut;70;Kassette\n" +
                "Bibi Blocksberg;Die Hexenschule;71;Kassette\n" +
                "Bibi Blocksberg;Der verhexte Kalender;72;Kassette\n" +
                "Bibi Blocksberg;Der Brieffreund;74;Kassette\n" +
                "Bibi Blocksberg;Die neue Lehrerin;75;Kassette\n" +
                "Bibi Blocksberg;im Hexeninternat;77;Kassette\n" +
                "Bibi Blocksberg;und Elea Eluanda;78;Kassette\n" +
                "Bibi Blocksberg;Papis Geburtstag;79;Kassette\n" +
                "Bibi Blocksberg;Das Hexenhoroskop;84;Kassette\n" +
                "Bibi Blocksberg;Kann Papi hexen?;86;Kassette\n" +
                "Bibi Blocksberg;Die Junghexenbande;89;Kassette\n" +
                "Bibi Blocksberg;Oma Grete sorgt für Wirbel;90;Kassette\n" +
                "Bibi Blocksberg;Maritas Geheimnis;91;Kassette\n" +
                "Bibi Blocksberg;Das geheimnisvolle Schloss;92;Kassette\n" +
                "Bibi Blocksberg;Das traurige Einhorn;96;Kassette\n" +
                "Bibi Blocksberg;Überraschung für Mami;97;Kassette\n" +
                "Bibi Blocksberg;Der verhexte Bürgermeister;104;Kassette\n" +
                "Bibi Blocksberg;Der Hexenbesen-Dieb;111;Kassette\n";

        return mediaContentString;
    }
}