package game;

import game.MasterMind.Colors;
import static game.MasterMind.Colors.*;
import game.MasterMind.FeedbackPeg;
import static game.MasterMind.FeedbackPeg.*;

import org.junit.Test;
import org.junit.Before;

import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class MasterMindTest {

    private MasterMind masterMind;

    @Before
    public void setUp(){
        masterMind = new MasterMind();
    }

    @Test
    public void canary(){
        assertTrue(true);
    }

    @Test
    public void gameNotWonAtStart()
    {
        assertEquals(false, masterMind.isGameWon());
    }
    
    @Test
    public void guessAllMatchColorAndPosition() {
        List<FeedbackPeg> expected = asList(MATCH_IN_POSITION, MATCH_IN_POSITION, MATCH_IN_POSITION, MATCH_IN_POSITION, MATCH_IN_POSITION);

        masterMind.setSecretCode(color1, color2, color3, color4, color5);

        assertEquals(expected, masterMind.guess(color1, color2, color3, color4, color5));
    }

    @Test
    public void guessAllNoMatch() {
        List<FeedbackPeg> expected = asList(MATCH_NONE, MATCH_NONE, MATCH_NONE, MATCH_NONE, MATCH_NONE);

        masterMind.setSecretCode(color1, color2, color3, color4, color5);
        assertEquals(expected, masterMind.guess(color6, color7, color9, color8, color10));
    }

    @Test
    public void guessHasLessThanFiveColors() {
        List<FeedbackPeg> expected = new ArrayList<>();

        masterMind.setSecretCode(color1, color2, color3, color4, color5);
        assertEquals(expected, masterMind.guess(color1, color2, color3, color4));
    }

    @Test
    public void guessAllMatchColorNotPosition() {
        List<FeedbackPeg> expected = asList(MATCH, MATCH, MATCH, MATCH, MATCH);

        masterMind.setSecretCode(color1, color2, color3, color4, color5);
        assertEquals(expected, masterMind.guess(color2, color3, color4, color5, color1));
    }

    @Test
    public void twoGuessesMatchColorAndPositionNoSort(){
        List<FeedbackPeg> expected = asList(MATCH_NONE, MATCH_NONE, MATCH_NONE, MATCH_IN_POSITION, MATCH_IN_POSITION);

        masterMind.setSecretCode(color1, color2, color3, color4, color5);
        assertEquals(expected, masterMind.guess(color10, color10, color10, color4, color5));
    }

    @Test
    public void twoGuessesMatchColorAndPositionSortRequired(){
        List<FeedbackPeg> expected = asList(MATCH_NONE, MATCH_NONE, MATCH_NONE, MATCH_IN_POSITION, MATCH_IN_POSITION);

        masterMind.setSecretCode(color1, color2, color3, color4, color5);
        assertEquals(expected, masterMind.guess(color1, color2, color10, color10, color10));
    }

    @Test
    public void twoMatchColorAndPositionTwoMatchColorSortRequired(){
        List<FeedbackPeg> expected = asList(MATCH_NONE, MATCH, MATCH, MATCH_IN_POSITION, MATCH_IN_POSITION);

        masterMind.setSecretCode(color1, color2, color3, color4, color5);
        assertEquals(expected, masterMind.guess(color4, color2, color1, color10, color5));
    }

    @Test
    public void repeatedColorInGuessNoRepeatedColorInCode(){
        List<FeedbackPeg> expected = asList(MATCH_NONE, MATCH_NONE, MATCH_NONE, MATCH_NONE, MATCH_IN_POSITION);

        masterMind.setSecretCode(color6, color4, color5, color9, color7);
        assertEquals(expected, masterMind.guess(color5, color5, color5, color5, color5));
    }

    @Test
    public void repeatedColorInGuessOneMatchColorAndPositionOneMatchColor(){
        List<FeedbackPeg> expected = asList(MATCH_NONE, MATCH_NONE, MATCH_NONE, MATCH, MATCH_IN_POSITION);

        masterMind.setSecretCode(color2, color7, color2, color1, color4);
        assertEquals(expected, masterMind.guess(color3, color9, color2, color6, color2));
    }

    @Test
    public void repeatedColorInGuessOneNoMatchFourMatchColor(){
        List<FeedbackPeg> expected = asList(MATCH_NONE, MATCH, MATCH, MATCH, MATCH);

        masterMind.setSecretCode(color2, color2, color5, color5, color2);
        assertEquals(expected, masterMind.guess(color5, color5, color2, color2, color5));
    }

    @Test
    public void repeatedColorInGuessFourMatchColorAndPosition(){
        List<FeedbackPeg> expected = asList(MATCH_NONE, MATCH_IN_POSITION, MATCH_IN_POSITION, MATCH_IN_POSITION, MATCH_IN_POSITION);

        masterMind.setSecretCode(color2, color2, color5, color2, color2);
        assertEquals(expected, masterMind.guess(color2, color5, color5, color2, color2));
    }

    @Test
    public void randomSecretCodeIsSize5(){

        masterMind.generateSecretCode();
        assertEquals(5, masterMind.getSecretCode().size());
    }

    @Test
    public void randomCodeIsAValidColorFromPool(){
        masterMind.generateSecretCode();
        List<Colors> validColors = asList(Colors.values());

        assertTrue(validColors.containsAll(masterMind.getSecretCode()));
    }

    @Test
    public void duplicateColorsInSecretCodeAreOK(){
        masterMind = new MasterMind(){
            @Override
            Colors getRandomColor(){
                return color9;
            }
        };
        List<Colors> expected = asList(color9, color9, color9, color9, color9);
        masterMind.generateSecretCode();
        assertEquals(expected, masterMind.getSecretCode());
    }

    @Test
    public void gameGeneratesSecretCodeAtStart(){
        assertEquals(5, masterMind.getSecretCode().size());
    }

    @Test
    public void countNumberOfGuessesMade()
    {
        int numOfGuesses = 10;
        for (int i = 0; i < numOfGuesses; i++) {
            masterMind.guess(color2, color2, color2, color2, color2);
        }

        assertEquals(numOfGuesses, masterMind.getNoOfGuessesMade());
    }

    @Test
    public void noCountIfNotProperGuess(){
        masterMind.guess(color10, color10, color10, color10);

        assertEquals(0, masterMind.getNoOfGuessesMade());
    }

    @Test
    public void guessHasMoreThanFiveColors(){
        List<FeedbackPeg> PegsReturned = masterMind.guess(color10, color10, color10, color10, color10, color10);
        assertTrue(PegsReturned.isEmpty());
    }

    @Test
    public void generateARandomColor(){
        List<Colors> validColors = asList(Colors.values());
        assertTrue(validColors.contains(masterMind.getRandomColor()));
    }

    @Test
    public void guessIsNoOpIfExceededMaxNoOfTurns() {
        masterMind.setNoOfGuessesMade(20);
        assertTrue(masterMind.guess(color1, color1, color1, color1, color1).isEmpty());
    }

    @Test
    public void repeatedColorInCodeIsMoreThanInGuessMatchColor(){
        List<FeedbackPeg> expected = asList(MATCH_NONE, MATCH_NONE, MATCH_NONE, MATCH, MATCH);

        masterMind.setSecretCode(color8, color2, color5, color2, color2);
        assertEquals(expected, masterMind.guess(color2, color9, color2, color6, color1));
    }

    @Test
    public void winIfCorrectGuess(){
        masterMind.setSecretCode(color4, color6, color9, color10, color2);
        masterMind.guess(color4, color6, color9, color10, color2);
        assertTrue(masterMind.isGameWon());
    }

    @Test
    public void noWinIfIncorrectGuess(){
        masterMind.setSecretCode(color4, color6, color9, color10, color5);
        masterMind.guess(color9, color7, color1, color8, color2);
        assertFalse(masterMind.isGameWon());
    }

    @Test
    public void noWinAfterMaxTurnsExceeded(){
        masterMind.setNoOfGuessesMade(20);
        masterMind.setSecretCode(color2, color2, color2, color2, color2);
        masterMind.guess(color2, color2, color2, color2, color2);
        assertFalse(masterMind.isGameWon());
    }

    @Test
    public void guessAfterWinIsNoOp(){
        masterMind.setSecretCode(color4, color6, color9, color10, color2);
        masterMind.guess(color4, color6, color9, color10, color2);

        assertTrue(masterMind.guess(color4, color6, color9, color10, color2).isEmpty());
    }

    @Test
    public void colorGeneratedIsRandom(){
        Colors color1 = masterMind.getRandomColor();
        Colors color2 = masterMind.getRandomColor();
        assertNotEquals(color1, color2);
    }
}