package game.ui;

import game.MasterMind;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class MasterMindFrame extends JFrame {
    private MasterMind masterMind;

    private final static int noOfColors = MasterMind.Colors.values().length;
    private final static int GUESSES_ALLOWED = MasterMind.getMaxGuessesAllowed();
    private final static int CODESIZE = MasterMind.getCODESIZE();

    private static BoardPanel boardPanel;
    private static BoardPanel feedbackPanel;
    private static JPanel inputPanel;
    private static BoardPanel secretPanel;
    private JLabel turnsLeftLabel;
    private JLabel instructionsLabel;

    private int guessCounter;
    private int turnNumber;
    private int fullMatch;
    private int halfMatch;

    private boolean gameEnded = false;

    private List<MasterMind.Colors> guessList = new ArrayList<>();

    private Map<String, Color> colors;

    public MasterMindFrame(){
        setSize(800, 900);
        setVisible(true);
        setResizable(false);
        setTitle("MasterMind");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(new Color(255,255,0));
    }

    public static void main(String[] args){
        new MasterMindFrame();
    }
    @Override
    protected void frameInit(){
        super.frameInit();

        masterMind = new MasterMind();

        fullMatch = 0;
        halfMatch = 0;

        colors = new HashMap<String, Color>(){
            {
                put("color1", new Color(146,47,69));
                put("color2", new Color(229,194,107));
                put("color3", new Color(68, 54, 0));
                put("color4", new Color(176,180,0));
                put("color5", new Color(103,116,64));
                put("color6", new Color(1,211,188));
                put("color7", new Color(0,144,140));
                put("color8", new Color(14,79,170));
                put("color9", new Color(210,145,255));
                put("color10", new Color(207,0,130));
            }
        };

        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(250,250,250));
        initializePanels();

        addContainer(turnsLeftLabel, 2, 0, 1, 1, 1.0, 0.025, GridBagConstraints.VERTICAL);
        addContainer(instructionsLabel, 0, 3, 2, 1, 1.0, 0.025, GridBagConstraints.VERTICAL);
        addContainer(new JLabel("Your Guess"), 0, 1, 1, 1, 1.0, 0.025,GridBagConstraints.VERTICAL);
        addContainer(new JLabel("Result"), 1, 1, 1, 1, 1.0, 0.025,GridBagConstraints.VERTICAL);

        addContainer(secretPanel, 0, 4, 2, 1, 0, 0.1, GridBagConstraints.VERTICAL);
        addContainer(boardPanel, 0, 2, 1, 1, 1.0, 0.8, GridBagConstraints.VERTICAL);
        addContainer(feedbackPanel, 1, 2, 1, 1, 1.0, 0.8, GridBagConstraints.VERTICAL);
        addContainer(inputPanel, 2, 2, 1, 1, 1.0, 0.8, GridBagConstraints.VERTICAL);

        pack();
    }

    private void addContainer(Component component, int gridx, int gridy,int gridwidth, int gridheight, double weightx, double weighty, int fill) {
        Insets insets = new Insets(1, 1, 1, 1);
        add(component, new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, GridBagConstraints.CENTER, fill, insets, 0, 0));
    }

    private void initializePanels(){
        initializeInputPanel();

        feedbackPanel = new BoardPanel(GUESSES_ALLOWED, CODESIZE, 12, 6);
        boardPanel = new BoardPanel(GUESSES_ALLOWED, CODESIZE,  12, 6);

        secretPanel = new BoardPanel(1, CODESIZE, 10, 5);
        secretPanel.add(new JLabel("Secret"));
        Border margin = new EmptyBorder(10,10,10,10);
        Border border =new LineBorder(Color.lightGray, 1, true);
        secretPanel.setBorder(new CompoundBorder(border, margin));
        for (int i = 0; i < CODESIZE ; i++) {
            secretPanel.changeColorOfPegAtIndex(Color.darkGray, 0, i);
        }

        turnsLeftLabel = new JLabel();
        updateTurnsLeftLabel();

        instructionsLabel = new JLabel();
        updateInstructionsLabel("INPROGRESS");
    }

    private void initializeInputPanel(){
        inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new GridLayout(noOfColors + 3, 1, 5, 5));

        for (int i = 0; i < noOfColors; i++) {
            String colorName = MasterMind.Colors.values()[i].toString();
            JButton colorButton = new JButton();
            colorButton.setBackground(colors.get(colorName));
            colorButton.setFocusPainted(false);
            colorButton.setBorder(new LineBorder(colors.get(colorName),1, true));
            inputPanel.add(colorButton);

            colorButton.addActionListener(new colorChosenListener());
        }

        JButton submit = new JButton(new ImageIcon("res/submitBtn.png"));
        submit.setFocusPainted(false);

        submit.addActionListener(new submitListener());
        inputPanel.add(submit);


        JButton undo = new JButton(new ImageIcon("res/undoBtn.png"));
        undo.setFocusPainted(false);

        undo.addActionListener(new undoListener());
        inputPanel.add(undo);

        JButton giveUp =  new JButton("Give Up?");
        giveUp.addActionListener(new giveUpListener());
        giveUp.setFocusPainted(false);

        inputPanel.add(giveUp);
    }

    private void gameEndRoutine() {
        String message;

        if(gameEnded)
        {
            showSecretCode();

            if(masterMind.isGameWon()){
                updateInstructionsLabel("WON");
                message = "You Won! ";
            }
            else{
                updateInstructionsLabel("GAMEOVER");
                message = "Not this time.. ";
            }

            int response = JOptionPane.showConfirmDialog(null, message + "Restart?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                this.dispose();
                new MasterMindFrame();
            }
            else if(response == JOptionPane.NO_OPTION)
                System.exit(0);
        }
    }

    private void showSecretCode() {
        List<MasterMind.Colors> secretCode = masterMind.getSecretCode();

        for (int i = 0; i < CODESIZE ; i++) {
            Color color = colors.get(secretCode.get(i).toString());
            secretPanel.changeColorOfPegAtIndex(color, 0, i);
        }
    }

    private void updateInstructionsLabel(String status) {
        String message;
        switch (status)
        {
            case "INPROGRESS": message = String.format("Place a color at position %d", guessCounter + 1); break;
            case "WON": message = String.format("You Won with %d Guesses!", turnNumber); break;
            case "SUBMIT": message = String.format("%d Correct Color and Position, %d Correct Color, Incorrect Position", fullMatch, halfMatch); break;
            case "GAMEOVER": message = "Game Over"; break;
            case "INVALID": message = "Error! Place 5 Pegs and try again"; break;

            default:
                message = "";
        }
        instructionsLabel.setText(message);
    }

    private void updateTurnsLeftLabel(){
        turnsLeftLabel.setText("Number Of Turns Left:    " + String.valueOf(GUESSES_ALLOWED - turnNumber));
    }

    private class submitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){

            Boolean successful = populateFeedbackPegs();

            if(successful) {
                turnNumber++;
                guessCounter = 0;
                guessList.clear();

                updateTurnsLeftLabel();
                updateInstructionsLabel("SUBMIT");

                if(masterMind.isGameWon())
                {
                    gameEnded = true;
                    gameEndRoutine();
                }else if(turnNumber > GUESSES_ALLOWED - 1)
                {
                    gameEnded = true;
                    gameEndRoutine();
                }
            } else{
                updateInstructionsLabel("INVALID");
            }
        }

        private Boolean populateFeedbackPegs() {
            List<MasterMind.FeedbackPeg> feedbackPegs = masterMind.guess(guessList.toArray(new MasterMind.Colors[guessList.size()]));
            int pegColCounter = 0;
            Color color = Color.WHITE;

            if(feedbackPegs.isEmpty())
                return false;
            else {
                fullMatch = 0;
                halfMatch = 0;
                for (MasterMind.FeedbackPeg feedbackPeg : feedbackPegs) {
                    switch (feedbackPeg.name()) {
                        case "MATCH":
                            color = Color.lightGray;
                            halfMatch++;
                            break;
                        case "MATCH_IN_POSITION":
                            color = Color.BLACK;
                            fullMatch++;
                            break;
                        case "MATCH_NONE":
                            color = Color.WHITE;
                            break;
                    }
                    feedbackPanel.changeColorOfPegAtIndex(color, turnNumber, pegColCounter++);
                }
            }
            return true;
        }
    }

    private class colorChosenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            if(gameEnded)
                return;

            if(guessCounter >= CODESIZE) {
                guessCounter = CODESIZE - 1;
                guessList.remove(guessCounter);
            }

            JButton button = (JButton) actionEvent.getSource();

            boardPanel.changeColorOfPegAtIndex(button.getBackground(), turnNumber, guessCounter);
            guessList.add(colorToColorsEnum(button.getBackground()));

            updateInstructionsLabel("INPROGRESS");
            guessCounter++;
        }
    }

    private class undoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            guessCounter--;

            if(guessCounter <= 0) {
                guessCounter = 0;
                guessList.clear();
            }
            else
                guessList.remove(guessCounter);

            boardPanel.clearColorOfPegAtIndex(turnNumber, guessCounter);
            updateInstructionsLabel("INPROGRESS");
        }
    }

    private class giveUpListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to give up?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                gameEnded = true;
                gameEndRoutine();
            }
        }
    }

    private MasterMind.Colors colorToColorsEnum(Color color){
        for(MasterMind.Colors colorOfPegs : MasterMind.Colors.values())
        {
            if(colors.get(colorOfPegs.toString()).getRGB() == color.getRGB()){
                return colorOfPegs;
            }
        }

        return null;
    }
}

