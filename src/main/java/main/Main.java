package main;

import main.data.GetData;

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
/*
    private void init(){

        Scanner scanner = new Scanner(System.in);

    }
*/
    public void ask(){

        GetData getData = new GetData();
        Scanner scanner = new Scanner(System.in);

        System.out.println("What would you like to do?");

        String function = scanner.nextLine();

        if(function.equalsIgnoreCase("sort")){

            sort();

        } else if(function.equalsIgnoreCase("average")) {

            System.out.println("Who would you like to get the average off: ");
            String person = scanner.nextLine();

            try {

                getData.loadSite(person);

            } catch (IOException e){

                e.printStackTrace();

            }

        } else if(function.equalsIgnoreCase("exit")){
            System.exit(1);
        } else {
            System.out.println("Unknown function, please try again");
            ask();
        }
    }

    public void sort(){
        scoreList.sort(Comparator.comparing(this::extractDouble));
        System.out.println(scoreList);
    }

    double extractDouble(String s) {
        String num = s.replaceAll("[^\\d.]", "");
        // return 0 if no digits found
        return num.isEmpty() ? 0 : Double.parseDouble(num);
    }

    public void printAverage(String name, int[] scores){

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
        ask();
    }

    public static void main(String[] main) {
        new Main();
    }


}
