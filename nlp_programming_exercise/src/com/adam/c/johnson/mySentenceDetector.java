package com.adam.c.johnson;

//Import the Apache package
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/*
* I imported an open source NLP package from Apache called OpenNLP (which seems to be ranked as one of the best free java NLP packages from my research).
* This package already has a pre-trained model for sentence detection, and I'll incorporate it into my
* project for this task. Granted it is still not 100% accurate, in all cases.
*
* */
public class mySentenceDetector {
    private String paragraph; //The actual paragraph that is passed to be checked
    private List<String> sentences; //An array of the sentences contained in that paragraph

    //Constructor to receive a paragraph
    mySentenceDetector(String para){
        this.paragraph = para;
    }

    //Method for returning all of the sentences in the provided paragraph
    //Params - None
    //Returns - List<String> representing the sentences that were in the provided string.
    public List<String> getSentences() {
        try {
            this.sentences = sentenceDetect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sentences;
    }

    private List<String> sentenceDetect() throws IOException {
        //Refer to model file "en-sent.bin"
        InputStream sentDetectModelStream = new FileInputStream("en-sent.bin");

        //Create a new sentence Model
        SentenceModel model = new SentenceModel(sentDetectModelStream);

        //Load the model we will use for sentence detection
        SentenceDetectorME sentDetector = new SentenceDetectorME(model);

        //Fix an issue I noticed in the Apache package
        this.paragraph = cleanSentencesStartingWithQuotation(this.paragraph);

        //Detect sentences in the paragraph and add them to the list
        List<String> sentences = Arrays.asList(sentDetector.sentDetect(this.paragraph));

        //Close the Model
        sentDetectModelStream.close();

        //Return the array of sentence;
        return sentences;
    }

    /*
    * This function shouldn't be needed, but is required for the detector to run properly for the provided test case.
    * In an production project, I would fix the underlying issue in the package. However, since I don't have
    * the time to dissect their library, this will have to do.
    *
    *
    * Underlying Strategy in the Package:
    *
    * The OpenNLP Sentence Detector can detect that a punctuation character marks the end of a sentence or not.
    * In this sense a sentence is defined as the longest white space trimmed character sequence between two punctuation marks.
    * The first and last sentence make an exception to this rule. The first non whitespace character is assumed to be the begin of a sentence, and the last
    * non whitespace character is assumed to be a sentence end. The sample text below should be segmented into its sentences.
    *
    * (https://opennlp.apache.org/docs/1.8.1/manual/opennlp.html#tools.sentdetect)
    *
    *
    *
    * Issue:
    * If a sentence starts with a " it doesn't recognizes that as the start of a new sentence. Instead it adds it on to the previous sentence.
    *
    *
    *
    * Notes:
    * These sentences will now be missing the " to start. I could always write a quotation balance checking function that would re-add this to uneven strings.
    * Since working with the sentences themselves isn't part of this project's parameters, I have omitted this function as it isn't needed.
    *
    *
    *
    *
    * Params- String s: A line of text
    * Returns- String s: that text with all '. "' replaces by '. '
    *
    * */
    private String cleanSentencesStartingWithQuotation(String s){
        s = s.replaceAll("[.] \"", ". ");
        return s;
    }
}
