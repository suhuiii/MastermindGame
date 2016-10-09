package game;

import java.util.*;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class MasterMind {

    public enum Colors {
        color1, color2, color3, color4, color5, color6, color7, color8, color9, color10
    }

    public enum FeedbackPeg{
        MATCH_NONE, MATCH, MATCH_IN_POSITION
    }

    private List<Colors> secretCode = new ArrayList<>(CODESIZE);

    private static final int CODESIZE = 5;

    private static final int maxGuessesAllowed = 20;

    private int noOfGuessesMade;
    private boolean gameWon;

    public MasterMind() {
        init();
    }

    private void init(){
        generateSecretCode();
    }

    public static int getMaxGuessesAllowed() {
        return maxGuessesAllowed;
    }

    public static int getCODESIZE() {
        return CODESIZE;
    }

    public int getNoOfGuessesMade() {
        return noOfGuessesMade;
    }

    void setNoOfGuessesMade(int noOfGuesses) {
        noOfGuessesMade = noOfGuesses;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    void setSecretCode(Colors... varargs){

            secretCode = Arrays.asList(varargs);
    }

    public List<Colors> getSecretCode(){
        return secretCode;
    }

    void generateSecretCode(){
        secretCode = IntStream.range(0, CODESIZE)
                              .mapToObj(i -> getRandomColor())
                              .collect(toList());
    }

    Colors getRandomColor() {
        return Colors.values()[new Random().nextInt(Colors.values().length - 1)];
    }

    public List<FeedbackPeg> guess(Colors... guessColors) {

         if(guessColors.length < CODESIZE || guessColors.length > CODESIZE || noOfGuessesMade >= maxGuessesAllowed || isGameWon())
           return new ArrayList<>();

         List<FeedbackPeg> feedbackPegs =
           new ArrayList<>(Collections.nCopies(5, FeedbackPeg.MATCH_NONE));

         for(int i = 0; i < CODESIZE; i++) {
           if(guessColors[i] == secretCode.get(i)) {
             feedbackPegs.set(i, FeedbackPeg.MATCH_IN_POSITION);
           }
         }

         for(int i = 0; i < CODESIZE; i++) {
           for(int j = 0; j < CODESIZE; j++) {
             if(guessColors[i] == secretCode.get(j) &&
               feedbackPegs.get(j) == FeedbackPeg.MATCH_NONE &&
               feedbackPegs.get(i) != FeedbackPeg.MATCH_IN_POSITION) {
                 feedbackPegs.set(j, FeedbackPeg.MATCH);
                 break;
             }
           }
         }

         gameWon = Collections.frequency(feedbackPegs, FeedbackPeg.MATCH_IN_POSITION) == CODESIZE;

         noOfGuessesMade++;

         return feedbackPegs.stream()
                            .sorted()
                            .collect(toList());
    }
}