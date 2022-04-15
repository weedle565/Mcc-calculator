package main.data;

import main.Main;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GetPlayerData {

    Main main = new Main("url");

    private int[] scores = new int[50];

    public GetPlayerData(){}

    public void loadSite(String url, int num) throws IOException {
        try {
            Document doc = Jsoup.connect("https://mcchampionship.fandom.com/wiki/" + url).get();
            getUrl(url, doc, num);
        } catch(HttpStatusException e) {
            System.out.println("That person cannot be found on the wiki! Check capitalisation and whether they are a current participant.");
            main.ask();
        }
    }

    private void getUrl(String url, Document d, int num)  {

        getAverages(d, num);

        main.printAverage(url, scores);

        setScores(scores);
    }

    public int[] getAverages(Document d, int num){
        int[] scores = new int[50];
        int arrayCounter = 0;

        int mccNum = 0;

        Elements rows = d.select("tr");

        for (Element row : rows) {
            Elements columns = row.select("td");

            //Splitting to check mcc number, where to stop is specified in main
            String text = row.text();
            text = text.replace("*", "");
            String[] splitter = text.split(" ");
            if(splitter[0] != null && splitter[0].equalsIgnoreCase("mc")) {
                mccNum = Integer.parseInt(splitter[2]);
            }

            if(mccNum >= num) {
                for (Element ignored : columns) {
                    //If the reader is where the values stop, break
                    if (row.text().contains("Special Events") || row.text().equals("")) {
                        break;
                    }
                    try {
                        char[] counter = columns.text().toCharArray();

                        int count1 = 0, count2 = 0;

                        //Find the location in the string where the first ) is (its a very long string)
                        for (char character : counter) {
                            if (character == ')') {
                                break;
                            }
                            count2++;
                        }

                        String removed;

                        try {
                            //Isolate the number by itself, this is done like this as the number is the second number in the string and the number can be either 3 digits or 4 digits
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

                            //Add the specific score to an array
                            if (arrayCounter > 1) {
                                if (!score.equals(String.valueOf(scores[arrayCounter - 1]))) {
                                    try {
                                        scores[arrayCounter] = Integer.parseInt(score);
                                        arrayCounter++;

                                    } catch (NumberFormatException e) {
                                        //Dunno why this happens lmfao
                                        System.out.println("This is to indicate an error, but im lazy so oh well thing works");
                                    }
                                }
                            } else {
                                scores[arrayCounter] = Integer.parseInt(score);
                                arrayCounter++;
                            }


                            //Check the break again
                            if (row.text().contains("Special Events")) {
                                break;
                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            e.getSuppressed();
                        }
                    } catch (NumberFormatException e) {

                        //Remove any 0's that were added to the end of the list
                        List<Integer> list = IntStream.of(scores).boxed().collect(Collectors.toList());

                        int i = list.size();
                        list.remove(i - 1);
                        scores = list.stream().mapToInt(Integer::intValue).toArray();
                    }
                }

                if (row.text().contains("Special Events") || row.text().equals("")) {
                    break;
                }
            }
        }

        return scores;
    }

    public void setScores(int[] scores){
        this.scores = scores;
    }

}
