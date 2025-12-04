package org.cis1200.tether.screens;

import org.cis1200.tether.ScreenManager;
import org.cis1200.tether.UI.UIButton;
import org.cis1200.tether.utility.SpriteSheetLoader;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class TitleScreen extends JPanel {

    ScreenManager screenManager;

    BufferedImage bgImage;
    TexturePaint texture;
    UIButton play_button;
    UIButton instructions_button;

    public TitleScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        setLayout(null);
        setBounds(0, 0, 1000, 500);
        bgImage = SpriteSheetLoader.loadImage("files/Blue.png");
        texture = new TexturePaint(bgImage, new Rectangle(0, 0, bgImage.getWidth(), bgImage.getHeight()));
        play_button = new UIButton(400, 200, 200, 200,
                SpriteSheetLoader.loadImage("files/Play.png"));
        play_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                screenManager.setScreen(ScreenManager.Screen.LEVEL_1);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                play_button.bounce(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                play_button.bounce(false);
            }
        });
        this.add(play_button);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(texture);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        char[] title = "TETHERED".toCharArray();
        g.setFont(new Font("Arial", Font.ITALIC, 96));
        g.setColor(Color.black);
        g.drawChars(title, 0, title.length, 250, 150);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 500);
    }
}
