package org.cis1200.tether;

import org.cis1200.tether.UI.BackgroundPanel;
import org.cis1200.tether.UI.UIView;
import org.cis1200.tether.screens.LevelsScreen;
import org.cis1200.tether.screens.TitleScreen;
import org.cis1200.tether.world.World;

import javax.swing.*;
import java.awt.*;

public class ScreenManager extends JPanel {

    JPanel currScreen;
    Screen currentScreenEnum;

    public ScreenManager() {
        setOpaque(false);
        setFocusable(true);
        setLayout(new BorderLayout());
        setScreen(Screen.TITLE_SCREEN);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("yo");
    }

    public void setScreen(Screen screen) {
        this.removeAll();
        switch (screen) {
            case TITLE_SCREEN:
                currScreen = new TitleScreen(this);
                currentScreenEnum = Screen.TITLE_SCREEN;
                SwingUtilities.invokeLater(currScreen::requestFocusInWindow);
                break;
            case LEVEL_1:
                newLevel("files/level_1.txt", 50, 350, 60, 350);
                currentScreenEnum = Screen.LEVEL_1;
                break;
            case LEVEL_2:
                newLevel("files/level_2.txt", 50, 350, 60, 350);
                currentScreenEnum = Screen.LEVEL_2;
                break;
            case LEVEL_3:
                newLevel("files/level_3.txt", 50, 350, 60, 350);
                currentScreenEnum = Screen.LEVEL_3;
                break;
            case LEVELS_SCREEN:
                currScreen = new LevelsScreen(this);
                currentScreenEnum = Screen.LEVELS_SCREEN;
                SwingUtilities.invokeLater(currScreen::requestFocusInWindow);
                break;
            default:
                currentScreenEnum = Screen.DEFAULT;
                currScreen = new JPanel();
                currScreen.setBackground(Color.white);
                currScreen.add(new JLabel("Default Screen"));
        }
        this.add(currScreen, BorderLayout.CENTER);
        this.revalidate();
        repaint();
    }

    private void newLevel(String filename, int p1x, int p1y, int p2x, int p2y) {
        currScreen = new BackgroundPanel(BackgroundPanel.Background.BROWN);
        currScreen.setLayout(new OverlayLayout(currScreen));
        currentScreenEnum = Screen.LEVEL_1;

        final UIView ui = new UIView(this);
        ui.setPreferredSize(new Dimension(1000, 500));
        ui.setAlignmentX(0.5f);
        ui.setAlignmentY(0.5f);

        final World world = new World(filename, p1x, p1y, p2x, p2y, ui);
        world.setPreferredSize(new Dimension(1000, 500));
        world.setAlignmentX(0.5f);
        world.setAlignmentY(0.5f);

        currScreen.add(ui);
        currScreen.add(world);
        SwingUtilities.invokeLater(world::requestFocusInWindow);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 500);
    }

    public Screen getScreen() {
        return currentScreenEnum;
    }

    public enum Screen {
        DEFAULT,
        TITLE_SCREEN,
        LEVELS_SCREEN,
        LEVEL_1,
        LEVEL_2,
        LEVEL_3,
    }

    public Screen getNextLevel() {
        switch (getScreen()) {
            case LEVEL_1:
                return Screen.LEVEL_2;
            case LEVEL_2:
                return Screen.LEVEL_3;
            case LEVEL_3:
                return Screen.TITLE_SCREEN;
            default:
                return Screen.DEFAULT;
        }
    }
}
