package com.adam.c.johnson;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        try{
            //Prompt the use for the file location.
            System.out.println("Please Enter the file location:");

            //Read the input
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String fileName = stdin.readLine();

            //Create a new document object based on this file class
            Document document = new Document(fileName);

            //Run the report which prints to console
            document.RunReport();

        }catch (Exception e){
            //Wrap it all in a try-catch to make sure we never just, and the program can at least close gracefully.
            System.out.println("Unexpected Error: " + e.getMessage());
        }
    }



}
