package com.adam.c.johnson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
* The main class for representing the document read in from the command line
* */
public class Document {
    private String filePath;
    private List<Paragraph> paragraphs;

    Document(String path){
        this.filePath = path; //Add this to the constructor so that we could possibly use this for other things
        paragraphs = new LinkedList<>();//Init the List
    }


    //This function loads the file and returns true or false after the loading is complete.
    //Params - None
    //Returns - None
    private boolean loadFile(){
        try{
            String strParagraph; //Temp string to hold the file lines (aka paragraphs)
            Paragraph paragraph; //Paragraph object used to process the file's paragraphs
            List<String> sentences; //Temp List for accepting values from the Sentence Detector

            //Use FileReader and BufferedReader since they are the most efficient text file options.
            //Will be most capable of handling large files like full books (at least of the native java options).
            FileReader inFileReader = new FileReader(this.filePath);
            BufferedReader inBufferedReader = new BufferedReader(inFileReader);

            //Start reading through the file
            while((strParagraph = inBufferedReader.readLine()) != null){
                if(strParagraph.isBlank() || strParagraph.isEmpty()){
                    //We don't want to count any lines that have no data. example double spaced paragraphs that end \n\n
                    continue;
                }

                //Clear out the last paragraph's data
                paragraph = new Paragraph();

                //Use the sentence detector to get the sentences from a given paragraph
                sentences = new mySentenceDetector(strParagraph).getSentences();

                //Add the sentences for the paragraph
                paragraph.setSentences(sentences);

                //Now that the paragraph has been created, we can add it to the document
                paragraphs.add(paragraph);

                /*NOTE:
                * It would be more efficient to do all of the calculations for the report here (Only go through the data once)
                * but this would be bad design, as it would limit the programs ability to expand with other functionality.
                *
                * Thus, I've kept the reading and calculations separate.
                * */
            }


            //Yay, the file is read in with no issues
            return true;

        }catch(Exception e){
            //Depending on what the user audience was, we could just log this or display and log.
            //Non-technical users wouldn't receive this message, but technical users would.
            //For demo purposes, just print instead of logging
            System.out.println("Unexpected error occurred while attempting to load " + this.filePath + ". Details: " + e.getMessage());

            //Consider file-read errors to be fatal in processing a document, and return false.
            System.out.println("Unable to load file!");
            return false;
        }
    }


    /*
    * Function for running a report of total number of Paragraphs, Sentences, and Word.
    *
    * Loads file from the fileName property abd prints totals to console
    *
    * Params - None
    * Returns - None
    * */
    public void RunReport(){
        try{
            //If the paragraphs object is empty, try and load the file
            if(paragraphs.isEmpty()){
                if(!this.loadFile()) {
                    //We were unable to properly load the file. Dont try and run the report.
                    System.out.println("Unable to run the report!");
                    return;
                }
            }

            //We've successfully loaded the file at this point, run the report
            int paragraphCount = paragraphs.size(); //Holds the total number of paragraphs
            int sentenceCount = 0; //Holds the total number of sentences
            int wordCount = 0; //Holds the total number of words

            //Loop through each paragraph
            for(Paragraph paragraph : paragraphs){
                //Add the number of sentences in this paragraph to the running total
                sentenceCount += paragraph.getSentenceCount();

                //Add the number of words in this paragraph to the running total
                wordCount += paragraph.getWordCount();
            }

            //Print the report to the console
            System.out.println("     REPORT:");
            System.out.println("-----------------");
            System.out.println("Paragraphs: " + paragraphCount);
            System.out.println(" Sentences: " + sentenceCount);
            System.out.println("     Words: " + wordCount);

        }catch(Exception e){
            //Just like in loadFile(), whether we display this error is based on the intended audience
            System.out.println("Unexpected error occurred while running report for " + this.filePath + ". Details: " + e.getMessage());
        }

    }






    /*
    * Private inner class used to represent the individual paragraphs of the document.
    *
    * Since the main part of the program only needs to get information about the doc
    * as a whole, and not work with individual paragraphs, we can leave this private.
    *
    * Should the need arise, we can move this to its own file and make it public.
    *
    *
    * This class should be highly flexible for changes in the future. Examples:
    *   We could easily add searching functions
    *   We could make sub-classes for specific types of paragraphs like headers or footers
    *   etc.
    *
    * I could have made a new class for Sentence, but this application does not require that level of break down.
    * All we would need to do is change the List<String> items to be List<Sentence>
    * */
    private class Paragraph{
        //Holds the individual sentences we extracted from the full text
        //I like using Lists as opposed to Arrays or HashMaps for their flexibility in moving things once (inserts)
        private List<String> sentences;

        //Constructor
        Paragraph(){
            sentences = new LinkedList<>(); //Init the list to an empty LinkedList
        }

        //Use a setter for the sentences.
        //Initially, I was going to have this class call the sentence detector to build this list
        //However, I think a better design will be to have the method that reads the data call the detector
        //I think this keeps the Paragraph class decoupled from the detector class
        public void setSentences(List<String> sentences) {
            this.sentences = sentences;
        }

        //Returns the number of sentences in a given paragraph
        private int getSentenceCount(){
            return this.sentences.size();
        }

        //Returns the number of words in a given paragraph
        private int getWordCount(){
            int count = 0;

            //Go through every sentence in this paragraph
            for(String sentence : sentences){
                //Split the sentence on areas of one or more white spaces
                //Use this instead of " " since older text tended to use two spaces after a period instead of one. Also cover other white space character issues from causing false positives.
                //Then, just add the length of that to our count
                count += sentence.split("\\s+").length;
            }

            //Return the word count we found
            return count;
        }
    }
}




