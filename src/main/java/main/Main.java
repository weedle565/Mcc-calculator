package main;

import main.data.GetPlayerData;
import main.data.GetTeamData;
import main.word.WriteToDocument;

import java.io.*;
import java.util.*;

public class Main {

    static List<String> scoreList = new ArrayList<>();

    private Main(){
        ask();
    }

    public Main(String useless){
        //Dummy lmao
    }

    public void ask(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("What would you like to do?");

        String function = scanner.nextLine();

        if(function.equalsIgnoreCase("sort")){

            sort();

        } else if(function.equalsIgnoreCase("average")) {

            System.out.println("Who would you like to get the average off: ");
            String person = scanner.nextLine();

            GetPlayerData getData = new GetPlayerData();
            try {

                getData.loadSite(person);

            } catch (IOException e){

                e.printStackTrace();

            }

        } else if(function.equalsIgnoreCase("exit")){
            System.exit(0);
        } else if(function.equalsIgnoreCase("doc")){
            new WriteToDocument(scoreList);
        } else if(function.equalsIgnoreCase("mcc")){

            System.out.println("Which mcc: ");
            String mcc = scanner.nextLine();

            GetTeamData team = new GetTeamData();

            try {
                team.loadSite(mcc);
            } catch (IOException e){
                e.printStackTrace();
            }

        } else {
            System.out.println("Unknown function, please try again");
            ask();
        }
    }

    public void sort(){
        scoreList.sort(Comparator.comparing(this::extractDouble));
        System.out.println(scoreList);
        ask();
    }

    double extractDouble(String s) {
        String num = s.replaceAll("^[^:]*:", "");
        // return 0 if no digits found
        return num.isEmpty() ? 0 : Double.parseDouble(num);
    }

    public void printAverage(String name, int[] scores){
        calculateAverage(scores, name);
        ask();
    }

    public void calculateAverage(int[] scores, String name){
        int avg = 0;
        int counter = 0;
        int thisMustBeHereCauseWeird = 0;

        for(int score : scores){
            if(score != 0 && thisMustBeHereCauseWeird != 0){
                avg += score;
                counter++;
            }
            thisMustBeHereCauseWeird++;
        }

        avg = avg/counter;

        System.out.println("The average of " + name + " is: " + avg + "\n");
        scoreList.add(name + ": " + avg);
        System.out.println(scoreList.toString());
    }

    public static void main(String[] main) {
        new Main();
    }


}
