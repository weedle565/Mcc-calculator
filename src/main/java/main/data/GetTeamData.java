package main.data;

import main.Main;
import org.apache.poi.ss.usermodel.Row;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GetTeamData {

    Main main = new Main("url");

    public void loadSite(String mcc) throws IOException {
        try {
            Document doc = Jsoup.connect("https://mcchampionship.fandom.com/wiki/MC_Championship_" + mcc).get();



            getUrl(mcc, doc);
        } catch(HttpStatusException e) {
            System.out.println("That person cannot be found on the wiki! Check capitalisation and whether they are a current participant.");
            main.ask();
        }
    }

    private void getUrl(String mcc, Document doc){

        String[] players = new String[40];

        System.out.println(mcc);
        int counter = 0;
        int arrayCounter = 0;

        Elements rows = doc.select("tr");

        for(Element r : rows){

            System.out.println(counter);

            if(counter > 24 && arrayCounter < 40) {
                r.text();
                String str = r.text();
                String[] splitter = str.split(" ");

                try {
                    players[arrayCounter] = splitter[1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    arrayCounter++;
                    counter++;
                    continue;
                }
                arrayCounter++;
            }

            counter++;

        }

        System.out.println(Arrays.toString(players));

        getPlayerAverage(players);

    }

    private void getPlayerAverage(String[] players) {

        GetPlayerData playerData = new GetPlayerData();
        Main main = new Main("");
        int[] scores;

        for (String player : players) {

            try {
                Document doc = Jsoup.connect("https://mcchampionship.fandom.com/wiki/" + player).get();
                scores = playerData.getAverages(doc);
                main.calculateAverage(scores, player);
            } catch (IOException e) {
                System.out.println("That person cannot be found on the wiki! Check capitalisation and whether they are a current participant.");
                main.ask();
            }
        }

        main.ask();
    }

}
