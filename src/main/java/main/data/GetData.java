package main.data;

import main.Main;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GetData {

    Main main = new Main("url");

    private int[] scores = new int[50];

    public void loadSite(String url) throws IOException {
        try {
            Document doc = Jsoup.connect("https://mcchampionship.fandom.com/wiki/" + url).get();
            getUrl(url, doc);
        } catch(HttpStatusException e) {
            System.out.println("That person cannot be found on the wiki! Check capitalisation and whether they are a current participant.");
            main.ask();
        }
    }

    private void getUrl(String url, Document d)  {

        int[] scores = new int[50];
        int arrayCounter = 0;

        Elements rows = d.select("tr");

            for (Element row : rows) {
                Elements columns = row.select("td");

                for (Element column : columns) {
                    if (row.text().contains("Special Events") || row.text().equals("")) {
                        break;
                    }
                    try {
                        char[] counter = columns.text().toCharArray();

                        int count1 = 0, count2 = 0;

                        for (char character : counter) {
                            if (character == ')') {
                                break;
                            }
                            count2++;
                        }

                        String removed;

                        try {
                            removed = columns.text().substring(count2 + 1);
                            counter = removed.toCharArray();

                            count2 = 0;

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

                            String score = removed.substring(count1, count2).replace("(", "").replace(",", "");

                            if (arrayCounter > 1) {
                                if (!score.equals(String.valueOf(scores[arrayCounter - 1]))) {
                                    try {
                                        scores[arrayCounter] = Integer.parseInt(score);
                                        arrayCounter++;

                                    } catch (NumberFormatException e) {
                                        System.out.println("This is to indicate an error, but im lazy so oh well thing works");
                                    }
                                }
                            } else {
                                scores[arrayCounter] = Integer.parseInt(score);
                                arrayCounter++;
                            }

                            //@ToDo System.out.println(score + " " + scores[mainCounter] + " " + (score.equals(String.valueOf(scores[mainCounter]))));

                            if (row.text().contains("Special Events")) {
                                break;
                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            e.getSuppressed();
                        }
                    } catch (NumberFormatException e){

                        List<Integer> list = IntStream.of(scores).boxed().collect(Collectors.toList());

                        int i = list.size();
                        list.remove(i-1);
                        scores = list.stream().mapToInt(Integer::intValue).toArray();
                    }
                }

                if (row.text().contains("Special Events") || row.text().equals("")) {
                    break;
                }

            }
            main.printAverage(url, scores);

            setScores(scores);
        }


    public void setScores(int[] scores){
        this.scores = scores;
    }

    public int[] getScores(){
        return scores;
    }

}
