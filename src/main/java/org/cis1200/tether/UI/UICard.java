package org.cis1200.tether.UI;

import org.cis1200.tether.ScreenManager;
import org.cis1200.tether.utility.PhysicsObject;
import org.cis1200.tether.utility.SpriteSheetLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UICard extends JPanel {

    ScreenManager screenManager;

    String sentence;
    int x;
    int y;
    int width;
    int height;

    boolean lost;

    public UICard(int x, int y, int width, int height, String sentence, boolean lost, ScreenManager screenManager) {
        setLayout(null);
        setBounds(x, y, width, height);
        this.screenManager = screenManager;
        this.sentence = sentence;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.lost = lost;

        UIButton retryButton = new UIButton(140, 130, 50, 50, 3,
                SpriteSheetLoader.loadImage("files/Restart.png"), null);
        UIButton nextButton = new UIButton(200, 130, 50, 50, 3,
                SpriteSheetLoader.loadImage("files/Next.png"), null);
        switch(screenManager.getScreen()) {
            case LEVEL_1:
                setupLevelButton(nextButton, ScreenManager.Screen.LEVEL_2, false);
                setupLevelButton(retryButton, ScreenManager.Screen.LEVEL_1, true);
                break;
            case LEVEL_2:
                setupLevelButton(nextButton, ScreenManager.Screen.LEVEL_3, false);
                setupLevelButton(retryButton, ScreenManager.Screen.LEVEL_2, true);
                break;
            case LEVEL_3:
                setupLevelButton(nextButton, ScreenManager.Screen.TITLE_SCREEN, false);
                setupLevelButton(retryButton, ScreenManager.Screen.LEVEL_3, true);
        }
        this.add(retryButton);
        this.add(nextButton);
    }

    private void setupLevelButton(UIButton levelButton, ScreenManager.Screen screen, boolean retryButton) {
        levelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!lost || retryButton) {
                    screenManager.setScreen(screen);
                } else {
                    JOptionPane.showMessageDialog(levelButton, "You have not unlocked this level!");
                }
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

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight()); // Corrected to (0, 0)

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 32));

        // Use FontMetrics to calculate centering
        FontMetrics fm = g.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(this.sentence)) / 2;
        int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

        g.drawString(this.sentence, textX, textY);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public void moveCard(int x, int y) {
        this.x += x;
        this.y += y;
        setBounds(this.x, this.y, this.width, this.height);
        this.revalidate();
        this.repaint();
    }

    public void setY(int y) {
        this.y = y;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }
}
