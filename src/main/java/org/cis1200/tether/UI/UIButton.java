package org.cis1200.tether.UI;

import javax.swing.*;
import java.awt.*;

public class UIButton extends JPanel {

    private int x;
    private int y;
    private int left;
    private int top;
    private int width;
    private int height;
    private Image image;

    public UIButton(int x, int y, int width, int height, Image image) {
        setBounds(x - 10, y - 10, width + 10, height + 10);
        setOpaque(false);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.left = 10;
        this.top = 10;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.drawImage(image, left, top, width, height, null);
    }

    public void bounce(boolean out) {
        if (out) {
            left = 0;
            top = 0;
            width += 20;
            height += 20;
        } else {
            left = 10;
            top = 10;
            width -= 20;
            height -= 20;
        }
        repaint();
    }

//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(width, height);
//    }
}
