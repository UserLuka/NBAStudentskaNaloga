package com.codetriage.scraper;
import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {

    static boolean zeroOne = true, zeroTwo = false, zeroThree = false;

    public static void main(String[] args) {
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Vnesite iskanega igralca: ");
            String iskanIgralec = br.readLine().toLowerCase().trim();
            String brezSumnikov = "";

            //Znebimo se šumnikov
            for(int i = 0; i < iskanIgralec.length(); i++){
                switch (iskanIgralec.charAt(i)){
                    case 'č':
                        brezSumnikov += "c";
                        break;
                    case 'ć':
                        brezSumnikov += "c";
                        break;
                    case 'š':
                        brezSumnikov += "s";
                        break;
                    case 'ž':
                        brezSumnikov += "z";
                        break;
                    default:
                        brezSumnikov += iskanIgralec.charAt(i);
                        break;
                }
            }

            String zacasna = "";
            //Znebimo se morebitnih pik v imenu
            for(int i = 0; i < brezSumnikov.length(); i++){
                if(brezSumnikov.charAt(i) == '.'){
                    zacasna += "";
                }
                else{
                    zacasna += brezSumnikov.charAt(i);
                }
            }
            brezSumnikov = zacasna;

            //Nastavimo zastavo za povezavo
            Link link = new Link();
            link.createLink(brezSumnikov);

            String ime = "", priimek = "";
            boolean flag = true;

            //Ločimo ime in priimek, da ju lahko uporabimo za povezavo
            for(int i = 0; i < brezSumnikov.length(); i++){
                if(brezSumnikov.charAt(i) == ' ')
                    flag = false;
                if(flag)
                    ime += brezSumnikov.charAt(i);
                else
                    priimek += brezSumnikov.charAt(i);
            }

            //Odstranimo predsldek pred priimkom
            priimek = priimek.trim();

            //Ustvarimo povezavo do strani
            String fileName = "https://www.basketball-reference.com/players/";

            //Dodatek za k povezavi
            fileName += priimek.charAt(0)+"/";

            //Okrajšamo priimek za povezavo
            for(int i = 0; i < 5; i++){
                fileName += priimek.charAt(i);
            }
            //Okrajšamo ime za povezavo
            for(int i = 0; i < 2; i++){
                fileName += ime.charAt(i);
            }

            //Metoda za dodatek k povezavi
            if(zeroOne)
                fileName += "01.html";
            else if(zeroTwo)
                fileName += "02.html";
            else if(zeroThree)
                fileName += "03.html";
            else
                fileName += "04.html";

            //Povezemo se na stran
            Document doc = Jsoup.connect(fileName).get();

            //Kličemo skladišče po ID elementa
            Elements skladisce = doc.select("#div_per_game");

            //Kličemo elemente po skladišču po elementu tr
            Elements table = skladisce.select("tr");

            //foreach zanjka za ponovitve po skladišču
            for(Element skald : table){
                //Leto gledanega povprečja
                String repositoryYear = skald.select("th[data-stat=season]").text();
                //Povprečno število v sezoni leta xxxx-xx
                String repositoryIssues = skald.select("td[data-stat=fg3a_per_g]").text();
                //Izpis leta in povprečja zadetkov
                System.out.println(repositoryYear+" "+repositoryIssues);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}