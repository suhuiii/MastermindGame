package game.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;



class BoardPanel extends JPanel {
    private static int noOfRows;
    private static int noOfCols;

    public BoardPanel(int noRows, int noCols, int leftRightPadding, int topBottomPadding){
        noOfRows = noRows;
        noOfCols = noCols;

        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new GridLayout(noOfRows, noOfCols +1, leftRightPadding, topBottomPadding));

        createButtons();
        setOpaque(false);

    }

    private void createButtons() {
        for (int i = 0; i < noOfRows ; i++) {
            for (int j = 0; j < noOfCols ; j++) {
                RoundButton button = new RoundButton();
                button.setForeground(Color.lightGray);
                add(button);
            }
        }
    }

    public void changeColorOfPegAtIndex(Color color, int row, int col){
        int index = row * noOfCols + col;
        getComponent(index).setBackground(color);
    }

    public void clearColorOfPegAtIndex(int row, int col){
        int index = row * noOfCols + col;
        getComponent(index).setBackground(null);
    }
}
