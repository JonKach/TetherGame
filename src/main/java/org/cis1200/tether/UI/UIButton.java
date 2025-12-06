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
    private String sentence;
    private int bounce;
    private boolean bounced;

    public UIButton(int x, int y, int width, int height, int bounce, Image image, String sentence) {
        setBounds(x - bounce, y - bounce, width + bounce, height + bounce);
        setOpaque(false);
        this.x = x;
        this.y = y;
        this.width = width - bounce;
        this.height = height - bounce;
        this.image = image;
        this.left = bounce;
        this.top = bounce;
        this.sentence = sentence;
        this.bounce = bounce;
        this.bounced = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (sentence == null && image != null) {
            g.drawImage(image, left, top, width, height, null);
        } else if (sentence != null) {
            g.setColor(Color.white);
            g.fillRect(left + 1, top + 1, width - 2, height - 2);
            g.setColor(Color.black);
            g.drawRect(left, top, width, height);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            if (!bounced) {
                g.drawString(sentence, (width) / 2 - 5 * sentence.length(), (height) / 2 + 12);
            } else {
                g.drawString(
                        sentence, (width) / 2 - 5 * sentence.length() - bounce / 2,
                        (height) / 2 + 12 - bounce / 2
                );
            }
        }
    }

    public void bounce(boolean out) {
        if (out) {
            left = 0;
            top = 0;
            width += 2 * bounce;
            height += 2 * bounce;
            bounced = true;
        } else {
            left = bounce;
            top = bounce;
            width -= 2 * bounce;
            height -= 2 * bounce;
            bounced = false;
        }
        repaint();
    }

    // @Override
    // public Dimension getPreferredSize() {
    // return new Dimension(width, height);
    // }
}
