package org.cis1200.tether.UI;

import org.cis1200.tether.utility.PhysicsObject;

import java.awt.*;

public class UICard extends PhysicsObject {

    String sentence;

    public UICard(int x, int y, int width, int height, String sentence) {
        super(x, y, width, height, 10);
        this.sentence = sentence;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect((int) getPx(), (int) getPy(), getWidth(), getHeight());
        g.setColor(Color.BLACK);
        char[] sentence = this.sentence.toCharArray();
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawChars(sentence, 0, sentence.length - 1,
                (int) getPx() + getWidth() / 2 - 70, (int) getPy()  + getHeight() / 2 + 15);
    }
}
