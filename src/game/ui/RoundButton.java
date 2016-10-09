package game.ui;

import javax.swing.*;
import java.awt.*;

class RoundButton extends JButton {

    public RoundButton() {
        super();

        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);

        setContentAreaFilled(false);
    }

    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(getBackground());
        }
        Graphics2D g2 = (Graphics2D) g;

        g2.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        super.paintComponent(g2);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawOval(0, 0, getSize().width-1,
                getSize().height-1);
    }

}
