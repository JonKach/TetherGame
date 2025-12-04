package org.cis1200.tether.screens;

import org.cis1200.tether.ScreenManager;
import org.cis1200.tether.UI.UIButton;
import org.cis1200.tether.utility.SpriteSheetLoader;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class LevelsScreen extends JPanel {

    ScreenManager screenManager;

    BufferedImage bgImage;
    TexturePaint texture;

    public LevelsScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        setLayout(null);
        setBounds(0, 0, 1000, 500);
        bgImage = SpriteSheetLoader.loadImage("files/Blue.png");
        texture = new TexturePaint(bgImage, new Rectangle(0, 0, bgImage.getWidth(), bgImage.getHeight()));
        UIButton levelOneButton = new UIButton(250, 200, 100, 100, 5,
                SpriteSheetLoader.loadImage("files/01.png"), null);
        UIButton levelTwoButton = new UIButton(450, 200, 100, 100, 5,
                SpriteSheetLoader.loadImage("files/02.png"), null);
        UIButton levelThreeButton = new UIButton(650, 200, 100, 100, 5,
                SpriteSheetLoader.loadImage("files/03.png"), null);
        setupLevelButton(levelOneButton, ScreenManager.Screen.LEVEL_1);
        setupLevelButton(levelTwoButton, ScreenManager.Screen.LEVEL_2);
        setupLevelButton(levelThreeButton, ScreenManager.Screen.LEVEL_3);
        UIButton loadFromSaveButton = new UIButton(300, 400, 400, 50, 5, null,
                "Load From Save");
        loadFromSaveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                screenManager.setScreen(screen); TODO
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loadFromSaveButton.bounce(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loadFromSaveButton.bounce(false);
            }
        });
        this.add(levelOneButton);
        this.add(levelTwoButton);
        this.add(levelThreeButton);
        this.add(loadFromSaveButton);
    }

    private void setupLevelButton(UIButton levelButton, ScreenManager.Screen screen) {
        levelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                screenManager.setScreen(screen);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                levelButton.bounce(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                levelButton.bounce(false);
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(texture);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        char[] title = "LEVELS".toCharArray();
        g.setFont(new Font("Arial", Font.ITALIC, 72));
        g.setColor(Color.black);
        g.drawChars(title, 0, title.length, 355, 150);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 500);
    }
}
