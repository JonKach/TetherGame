package org.cis1200.tether.UI;

import org.cis1200.tether.utility.SpriteSheetLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BackgroundPanel extends JPanel {

    BufferedImage bgImage;
    TexturePaint texture;

    public BackgroundPanel(Background background) {
        changeBackground(background);
    }

    public void changeBackground(Background background) {
        switch (background) {
            case BLUE:
                bgImage = SpriteSheetLoader.loadImage("files/Blue.png");
                break;
            case BROWN:
                bgImage = SpriteSheetLoader.loadImage("files/Brown.png");
                break;
            default:
                bgImage = SpriteSheetLoader.loadImage("files/Blue.png");
        }
        texture = new TexturePaint(
                bgImage, new Rectangle(
                        0, 0,
                        bgImage.getWidth(), bgImage.getHeight()
                )
        );
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(texture); // Set the paint to our TexturePaint
        g2d.fillRect(0, 0, getWidth(), getHeight()); // Fill the entire panel with the tiled pattern
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 500);
    }

    public enum Background {
        BLUE,
        BROWN
    }
}
