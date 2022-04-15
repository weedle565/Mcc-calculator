package main.data;

import main.Main;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class GetTeamData {

    Main main = new Main("url");

    public void loadSite(String mcc) throws IOException {
        //Connect to the website with the last number being the param
        try {
            Document doc = Jsoup.connect("https://mcchampionship.fandom.com/wiki/MC_Championship_" + mcc).get();

            getUrl(mcc, doc);
        } catch (HttpStatusException e) {
            System.out.println("That tournament cannot be found on the wiki! Check capitalisation and whether they are a current participant.");
            main.ask();
        }
    }

    private void getUrl(String mcc, Document doc) {
        //40 Players, store all names in here from the previous site
        String[] players = new String[40];

        int counter = 0;
        int arrayCounter = 0;


        if (mcc.equals("21")) {
            players = nextMCC(doc, players);
        } else {
            //tr rows
            Elements rows = doc.select("tr");
            for (Element r : rows) {

                //24 the number where this is taking the right values, was trial and error, only want 40 values
                if (counter > 2 && arrayCounter < 40) {
                    System.out.println(r.text());
                    //Split the text as its 3 words, I only want the middle
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
        }


        System.out.println(Arrays.toString(players));

        getPlayerAverage(players);

    }

    private String[] nextMCC(Document doc, String[] playerArray) {
        List<String> players = new ArrayList<>();

        int counter = 0;

        String[] lowerCaseSplitter;
        String lowerCase;

        TeamNames[] teamNames = TeamNames.values();

        Elements rows = doc.select("td");

        for (Element d : rows) {

            lowerCaseSplitter = d.text().toLowerCase(Locale.ROOT).split(" ");
            ;
            lowerCase = lowerCaseSplitter[0];

            if (!d.text().equals("TBA") && !d.text().equalsIgnoreCase("mcc 20")) {
                if (d.text().compareToIgnoreCase(Arrays.toString(TeamNames.values())) != 0) {

                    for (TeamNames t : teamNames) {

                        if (!lowerCase.equalsIgnoreCase(t.toString())) {
                            players.add(d.text());
                        }
                        break;
                    }
                }

            }


            if (players.size() == 19) {
                for (int i = players.size() - 1; i > 0; i--) {
                    if (i % 2 == 1) {
                        players.set(i, "test");
                    }
                }
            }

        }
        players.removeIf(player -> player.equals("test"));
        players.subList(10, players.size()).clear();

        System.out.println(players.size());


        for (String player : players) {

            if (counter < 39) {
                String[] split = player.replaceAll("[^\\p{ASCII}]", "").split(" ");
                playerArray[counter] = split[0];
                counter++;
                playerArray[counter] = split[1];
                counter++;
                playerArray[counter] = split[2];
                counter++;
                playerArray[counter] = split[3];
                counter++;
            }
        }

        return playerArray;
    }


    private void getPlayerAverage(String[] players) {

        GetPlayerData playerData = new GetPlayerData();
        Main main = new Main("");
        int[] scores;

        for (String player : players) {

            try {
                //get the player data from the wiki
                Document doc = Jsoup.connect("https://mcchampionship.fandom.com/wiki/" + player).get();
                scores = playerData.getAverages(doc, 14);
                main.calculateAverage(scores, player);
            } catch (IOException e) {
                System.out.println("That person cannot be found on the wiki! Check capitalisation and whether they are a current participant.");
                main.ask();
            }
        }

        main.ask();
    }

}
