package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class Main {

    static int[] scores = new int[20];

    private static void getUrl(String url) throws IOException {

        int arrayCounter = 0;
        int mainCounter = 0;

        URL loadedURL = new URL(url);

        Document doc = Jsoup.connect(url).get();
        Elements rows = doc.select("tr");

        for(Element row : rows){
            Elements columns = row.select("td");
            for(Element column : columns) {

                char[] counter = columns.text().toCharArray();

                int numOfOpening, numOfClosing, count1 = 0, count2 = 0;

                for (char character : counter) {
                    if (character == '(') {
                        break;
                    }
                    count1++;
                }

                for (char character : counter) {
                    if (character == ')') {
                        break;
                    }
                    count2++;
                }

                String score = columns.text().substring(count1, count2).replace("(", "").replace(",", "");

                if(row.text().contains("Special Events")){
                    System.out.println("yes");
                    break;
                }

                if(arrayCounter > 1) {
                    if (!score.equals(String.valueOf(scores[arrayCounter - 1]))) {
                        try {
                            scores[arrayCounter] = Integer.parseInt(score);
                            arrayCounter++;
                            //System.out.println("adding");
                            System.out.println(Arrays.toString(scores));

                        } catch (NumberFormatException e) {
                            e.addSuppressed(e);
                        }
                    }
                } else {
                    scores[arrayCounter] = Integer.parseInt(score);
                    arrayCounter++;
                }
            //    System.out.println(score + " " + scores[mainCounter] + " " + (score.equals(String.valueOf(scores[mainCounter]))));

            }

            if(row.text().contains("Special Events")){
                System.out.println("yes");
                break;
            }

            System.out.println(" ");
        }


    }

    public static void main(String[] main) throws IOException {
        getUrl("https://mcchampionship.fandom.com/wiki/Illumina");
    }


}
